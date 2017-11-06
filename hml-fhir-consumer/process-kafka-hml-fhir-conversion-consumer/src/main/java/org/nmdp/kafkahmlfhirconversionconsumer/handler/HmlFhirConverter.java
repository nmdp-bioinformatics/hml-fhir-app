package org.nmdp.kafkahmlfhirconversionconsumer.handler;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/30/17.
 * <p>
 * process-kafka-hml-fhir-conversion-consumer
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

import org.nmdp.hmlfhir.ConvertHmlToFhir;
import org.nmdp.hmlfhir.ConvertHmlToFhirImpl;
import org.nmdp.hmlfhir.deserialization.Deserializer;
import org.nmdp.hmlfhir.deserialization.HmlDeserializer;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirmongo.mongo.MongoConversionStatusDatabase;
import org.nmdp.kafkaconsumer.handler.KafkaMessageHandler;
import org.nmdp.hmlfhirmongo.config.MongoConfiguration;
import org.nmdp.hmlfhirmongo.mongo.MongoFhirDatabase;
import org.nmdp.hmlfhirmongo.mongo.MongoHmlDatabase;

import org.nmdp.kafkahmlfhirconversionconsumer.http.FhirSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

@Singleton
public class HmlFhirConverter implements KafkaMessageHandler, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(HmlFhirConverter.class);
    private static final ThreadLocal<DecimalFormat> DF = ThreadLocal.withInitial(() -> new DecimalFormat("####################"));
    private static final ThreadLocal<GsonBuilder> OBJECT_MAPPER = ThreadLocal.withInitial(GsonBuilder::new);
    private static final Yaml yaml = new Yaml();

    private final MongoConfiguration mongoConfiguration;
    private final MongoConversionStatusDatabase mongoConversionStatusDatabase;
    private final MongoHmlDatabase mongoHmlDatabase;
    private final MongoFhirDatabase mongoFhirDatabase;
    private final ConvertHmlToFhir CONVERTER;
    private final ConcurrentMap<String, LinkedBlockingQueue<WorkItem>> workQueueMap;
    private final FhirSubmission fhirSubmission;

    public HmlFhirConverter() throws IOException {
        Deserializer deserializer = new HmlDeserializer();
        CONVERTER = new ConvertHmlToFhirImpl(deserializer);
        workQueueMap = new ConcurrentHashMap<>();
        URL url = new URL("file:." + "/src/main/resources/mongo-configuration.yaml");

        try (InputStream is = url.openStream()) {
            mongoConfiguration = yaml.loadAs(is, MongoConfiguration.class);
        }

        mongoHmlDatabase = new MongoHmlDatabase(mongoConfiguration);
        mongoFhirDatabase = new MongoFhirDatabase(mongoConfiguration);
        mongoConversionStatusDatabase = new MongoConversionStatusDatabase(mongoConfiguration);
        fhirSubmission = new FhirSubmission();
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
            queue.add(new WorkItem(payload));
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
            work.stream()
                .filter(Objects::nonNull)
                .forEach(item -> convertHmlToFhir(item));
        } catch (Exception ex) {
            LOG.error("Error processing table: ", ex);
        }
    }

    private void convertHmlToFhir(WorkItem item) {
        try {
            Gson gson = OBJECT_MAPPER.get().create();
            String kafkaMessage = new String(item.getPayload());
            JsonObject kafkaJson = gson.fromJson(kafkaMessage, JsonObject.class);
            JsonObject kafkaPayloadJson = kafkaJson.getAsJsonObject("payload");
            Hml hml = null;
            String conversionStatusId = kafkaPayloadJson.get("modelId").getAsString();
            org.bson.Document conversionStatus = getConversionStatus(conversionStatusId);

            if (kafkaPayloadJson.has("model")) {
                JsonObject hmlJson = new JsonObject();
                hmlJson.add("hml", kafkaPayloadJson.getAsJsonObject("model"));
                hml = CONVERTER.convertToDto(hmlJson);}
            else {
                hml = getHmlFromMongo(kafkaPayloadJson.get("modelId").toString());
            }

            writeFhirToMongo(CONVERTER.convert(hml), conversionStatus);
            fhirSubmission.postSubmission(conversionStatusId);
        } catch (Exception ex) {
            LOG.error("Error converting HML to FHIR.", ex);
        }
    }

    private org.bson.Document getConversionStatus(String conversionId) throws Exception {
        try {
            return mongoConversionStatusDatabase.get(conversionId);
        } catch (Exception ex) {
            LOG.error("Error retrieving status from mongo", ex);
            throw ex;
        }
    }

    private Hml getHmlFromMongo(String hmlId) throws Exception {
        try {
            org.bson.Document document = mongoHmlDatabase.get(hmlId);
            String json = document.toJson();
            return CONVERTER.convertToDto(json, null);
        } catch (Exception ex) {
            LOG.error("Error retrieving record from mongo", ex);
            throw ex;
        }
    }

    private void writeFhirToMongo(FhirMessage fhir, org.bson.Document conversionStatus) throws Exception {
        try {
            fhir = mongoFhirDatabase.save(fhir);
            updateConversionStatusCompleted(fhir, conversionStatus.get("_id").toString(), true);
        } catch (Exception ex) {
            LOG.error("Error writing record to mongo", ex);
            throw ex;
        }
    }

    private void updateConversionStatusCompleted(FhirMessage fhir, String id, Boolean success) {
        try {
            mongoConversionStatusDatabase.update(id, success, fhir);
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
        private final byte[] payload;

        public WorkItem(byte[] payload) {
            this.payload = payload;
        }

        public byte[] getPayload() {
            return payload;
        }
    }
}
