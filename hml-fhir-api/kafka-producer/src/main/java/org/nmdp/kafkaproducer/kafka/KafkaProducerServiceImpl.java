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

import org.apache.log4j.Logger;
import org.nmdp.kafkaproducer.config.KafkaProducerConfiguration;
import org.nmdp.kafkaproducer.config.KafkaMessageProducerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KafkaProducerServiceImpl<K, M> implements KafkaProducerService<K, M> {

    private final KafkaMessageProducer<K, M> producer;
    private final static Logger LOG = Logger.getLogger(KafkaProducerServiceImpl.class);

    @Autowired
    public KafkaProducerServiceImpl(@Qualifier("kafkaProducerConfiguration") KafkaProducerConfiguration kafkaConfig) {
        KafkaMessageProducerConfiguration config = new KafkaMessageProducerConfiguration(kafkaConfig.getProducerConfiguration());
        producer = new KafkaMessageProducer<>(config);
    }

    @Override
    public void produceKafkaMessages(List<M> kafkaMessages, String topic, K key) {
        producer.send(kafkaMessages, key, topic);
    }
}
