package org.nmdp.kafkafhirsubmissionconsumer.handler;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/9/17.
 * <p>
 * process-kafka-fhir-submission-consumer
 * Copyright (c) 2012-2017 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import javax.inject.Singleton;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.bson.Document;
import org.nmdp.fhirsubmission.FhirSubmission;
import org.nmdp.fhirsubmission.util.FhirMessageUtil;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirmongo.mongo.MongoConversionStatusDatabase;
import org.nmdp.hmlfhirmongo.mongo.MongoFhirSubmissionDatabase;
import org.nmdp.kafkaconsumer.handler.KafkaMessageHandler;
import org.nmdp.hmlfhirmongo.config.MongoConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

@Singleton
public class FhirSubmissionHandler implements KafkaMessageHandler, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(FhirSubmissionHandler.class);
    private static final ThreadLocal<DecimalFormat> DF = ThreadLocal.withInitial(() -> new DecimalFormat("####################"));
    private static final ThreadLocal<GsonBuilder> OBJECT_MAPPER = ThreadLocal.withInitial(GsonBuilder::new);
    private static final Yaml yaml = new Yaml();

    private final MongoConfiguration mongoConfiguration;
    private final MongoConversionStatusDatabase mongoConversionStatusDatabase;
    private final MongoFhirSubmissionDatabase mongoFhirSubmissionDatabase;
    private final ConcurrentMap<String, LinkedBlockingQueue<WorkItem>> workQueueMap;

    public FhirSubmissionHandler() throws IOException {
        workQueueMap = new ConcurrentHashMap<>();
        URL url = new URL("file:." + "/src/main/resources/mongo-configuration.yaml");

        try (InputStream is = url.openStream()) {
            mongoConfiguration = yaml.loadAs(is, MongoConfiguration.class);
        }

        this.mongoConversionStatusDatabase = new MongoConversionStatusDatabase(mongoConfiguration);
        this.mongoFhirSubmissionDatabase = new MongoFhirSubmissionDatabase(mongoConfiguration);
    }

    private String getSenderKey(String topic, int partition) {
        return topic + "-" + DF.get().format(partition);
    }

    private LinkedBlockingQueue<WorkItem> getWorkQueue(String senderKey) {
        return workQueueMap.computeIfAbsent(senderKey, k -> new LinkedBlockingQueue<>());
    }

    @Override
    public void process(String topic, int partition, long offset, byte[] key, byte[] payload) throws Exception {
        String senderKey = getSenderKey(topic, partition);
        LinkedBlockingQueue<WorkItem> queue = getWorkQueue(senderKey);

        try {
            queue.add(new WorkItem(convertPayloadToFhirMessage(payload)));
        } catch (Exception e) {
            LOG.error("Error parsing message " + topic + "-" + DF.get().format(partition) + ":" + DF.get().format(offset), e);
            return;
        }
    }

    public void commit(String topic, int partition, long offset) throws Exception {
        String senderKey = getSenderKey(topic, partition);
        LinkedBlockingQueue<WorkItem> queue = getWorkQueue(senderKey);
        List<WorkItem> work = new ArrayList<>(queue.size());
        queue.drainTo(work);

        try {
            commitWork(work);
        } catch (Exception ex) {
            LOG.error("Error committing work: ", ex);
        }
    }

    private void commitWork(List<WorkItem> work) throws IOException {
        try {
            FhirMessageUtil util = new FhirMessageUtil();
            List<org.nmdp.hmlfhirmongo.models.FhirSubmission> fhirSubmissions = new ArrayList<>();

            for (WorkItem item : work) {
                FhirMessage message = item.getPayload();
                org.nmdp.hmlfhirmongo.models.FhirSubmission submission = util.submit(message);

                submission.setFhirMessage(message);
                fhirSubmissions.add(submission);
            }

            writeFhirResources(fhirSubmissions);
        } catch (Exception ex) {
            LOG.error("Error processing table: ", ex);
        }
    }

    private FhirMessage convertPayloadToFhirMessage(byte[] payload) {
        FhirMessage fhirMessage = null;

        try {
            Gson gson = OBJECT_MAPPER.get().create();
            String kafkaMessage = new String(payload);
            JsonObject kafkaJson = gson.fromJson(kafkaMessage, JsonObject.class);
            JsonObject kafkaPayloadJson = kafkaJson.getAsJsonObject("payload");
            fhirMessage = gson.fromJson(kafkaPayloadJson.get("model").getAsString(), FhirMessage.class);
        } catch (Exception ex) {
            LOG.error("Error converting HML to FHIR.", ex);
        }

        return fhirMessage;
    }

    private void writeFhirResources(List<org.nmdp.hmlfhirmongo.models.FhirSubmission> results) throws Exception {
        try {
            for (org.nmdp.hmlfhirmongo.models.FhirSubmission result : results) {
                FhirMessage fhirMessage = result.getFhirMessage();
                String submissionId = mongoFhirSubmissionDatabase.save(result).getId();
                Document conversionStatus = getConversionStatusByFhirId(fhirMessage.getFhirId());
                String conversionId = conversionStatus.get("_id").toString();
                updateConversionStatusCompleted(fhirMessage, conversionId, submissionId, true);
            }
        } catch (Exception ex) {
            LOG.error("Error updating mongo with submission results.", ex);
            throw ex;
        }
    }

    private org.bson.Document getConversionStatusByFhirId(String fhirId) throws Exception {
        try {
            return mongoConversionStatusDatabase.getByFhirId(fhirId);
        } catch (Exception ex) {
            LOG.error("Error retrieving status from mongo", ex);
            throw ex;
        }
    }

    private void updateConversionStatusCompleted(FhirMessage fhir, String id, String submissionId, Boolean success) {
        try {
            mongoConversionStatusDatabase.update(id, success, fhir, submissionId);
        } catch (Exception ex) {
            LOG.error("Error updating mongo complete", ex);
        }
    }

    @Override
    public void rollback(String topic, int partition, long offset) throws Exception {
        String senderKey = getSenderKey(topic, partition);
        LinkedBlockingQueue<WorkItem> queue = getWorkQueue(senderKey);
        queue.clear();
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {

        }
    }

    @Override
    public String toString() {
        return "HmlFhirConverter[]";
    }

    private static class WorkItem {
        private final FhirMessage fhirMessage;

        public WorkItem(FhirMessage fhirMessage) {
            this.fhirMessage = fhirMessage;
        }

        public FhirMessage getPayload() {
            return fhirMessage;
        }
    }
}
