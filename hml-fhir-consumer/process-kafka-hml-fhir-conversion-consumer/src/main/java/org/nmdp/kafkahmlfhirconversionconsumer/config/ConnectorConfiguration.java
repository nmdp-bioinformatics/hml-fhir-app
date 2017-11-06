package org.nmdp.kafkahmlfhirconversionconsumer.config;

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

import java.io.Closeable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.codahale.metrics.MetricRegistry;

import org.nmdp.kafkaconsumer.consumer.KafkaMessageConsumer;
import org.nmdp.kafkahmlfhirconversionconsumer.handler.HmlFhirConverter;

public class ConnectorConfiguration {
    private String id;
    private String sourceBroker;
    private String sourceTopic;
    private String targetCluster;
    private String targetTable = "%SOURCE%";
    private int threads = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceBroker() {
        return sourceBroker;
    }

    public void setSourceBroker(String sourceBroker) {
        this.sourceBroker = sourceBroker;
    }

    public String getSourceTopic() {
        return sourceTopic;
    }

    public void setSourceTopic(String sourceTopic) {
        this.sourceTopic = sourceTopic;
    }

    public String getTargetCluster() {
        return targetCluster;
    }

    public void setTargetCluster(String targetCluster) {
        this.targetCluster = targetCluster;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public List<KafkaMessageConsumer> createConnectors(
            List<Closeable> closeables,
            MetricRegistry metrics,
            Map<String, KafkaBrokerConfiguration> kafkaConfigMap,
            Map<String, String> consumerDefaults,
            String consumerGroupPrefix) throws Exception {

        KafkaBrokerConfiguration sourceBrokerConfig = kafkaConfigMap.get(sourceBroker);
        if (sourceBrokerConfig == null) {
            throw new IllegalArgumentException("No broker found with ID " + sourceBroker);
        }

        HmlFhirConverter handler = new HmlFhirConverter();
        closeables.add(handler);

        List<KafkaMessageConsumer> consumers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            String clientId = id + "/" + i + "@" + InetAddress.getLocalHost().getHostName();

            consumers.add(sourceBrokerConfig.createKafkaMessageConsumer(
                    closeables, metrics, handler, sourceTopic, id, clientId, consumerDefaults, consumerGroupPrefix));
        }

        return consumers;
    }
}
