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

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.codahale.metrics.MetricRegistry;

import org.nmdp.kafkaconsumer.handler.KafkaMessageHandler;
import org.nmdp.kafkaconsumer.consumer.KafkaMessageConsumer;
import org.nmdp.kafkaconsumer.config.KafkaConsumerProperties;

public class KafkaBrokerConfiguration {
    private String id;
    private String hosts;
    private Map<String, String> consumerConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public Map<String, String> getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(Map<String, String> consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public KafkaMessageConsumer createKafkaMessageConsumer(List<Closeable> closeables, MetricRegistry metrics,
            KafkaMessageHandler handler, String sourceTopic, String routeId, String clientId,
            Map<String, String> consumerDefaults, String consumerGroupPrefix) throws Exception {

        Map<String, String> config = new HashMap<>();
        if (consumerDefaults != null) {
            config.putAll(consumerDefaults);
        }
        if (consumerConfig != null) {
            config.putAll(consumerConfig);
        }

        Properties props = buildConsumerProperties(config);
        KafkaConsumerProperties properties = KafkaConsumerProperties.builder()
                .configure(props, null)
                .consumerGroup(consumerGroupPrefix + routeId)
                .clientId(clientId)
                .build();

        KafkaMessageConsumer consumer = new KafkaMessageConsumer(id, sourceTopic, handler, metrics, properties);

        consumer.start();
        closeables.add(consumer);
        return consumer;
    }

    private Properties buildConsumerProperties(Map<String, String> config) {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String keyTranslated = camelCaseToDotSyntax(key);
            props.setProperty(keyTranslated, value);
        }

        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getHosts());
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        return props;
    }

    private String camelCaseToDotSyntax(String key) {
        StringBuilder buf = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                if (buf.length() > 0) {
                    buf.append(".");
                }
                buf.append(Character.toLowerCase(c));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
