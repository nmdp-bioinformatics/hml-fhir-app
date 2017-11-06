package org.nmdp.kafkaproducer.kafka;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/16/17.
 * <p>
 * service-kafka-producer
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
import java.util.*;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import org.nmdp.kafkaproducer.config.KafkaMessageProducerConfiguration;
import org.nmdp.servicekafkaproducermodel.models.KafkaMessage;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class KafkaMessageProducer<K, M> implements Closeable {

    private final Producer<K, M> producer;
    private static final Logger LOG = Logger.getLogger(KafkaMessageProducer.class);

    public KafkaMessageProducer(KafkaMessageProducerConfiguration config) {
        producer = new KafkaProducer<>(config.getProperties());
    }

    public void send(List<M> messages, K key, String topic) {
        List<KafkaProducerCallback> callbacks = new ArrayList<>();
        List<ProducerRecord<K, M>> records =  messages.stream()
                .filter(Objects::nonNull)
                .map(message -> new ProducerRecord<>(topic, key, message))
                .collect(Collectors.toList());

        for (ProducerRecord record : records) {
            KafkaProducerCallback callback = new KafkaProducerCallback();
            producer.send(record, callback);
            callbacks.add(callback);
        }

        producer.flush();

        while (!messageProductionComplete(callbacks)) {
            LOG.info("Sending kafka messages.");
        }
    }

    @Override
    public void close() {
        producer.close();
    }

    private Boolean messageProductionComplete(List<KafkaProducerCallback> callbacks) {
        return !callbacks.stream()
                .filter(Objects::nonNull)
                .anyMatch(callback -> !callback.getIsComplete());
    }
}
