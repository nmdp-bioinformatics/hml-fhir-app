package org.nmdp.kafkaproducer.util;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/18/17.
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
import org.nmdp.servicekafkaproducermodel.models.KafkaMessage;
import org.nmdp.servicekafkaproducermodel.models.KafkaMessagePayload;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertToKafkaMessage {

    private final static Logger LOG = Logger.getLogger(ConvertToKafkaMessage.class);

    public static <T> List<KafkaMessage> transform(Map<String, T> dtos, String key) {
        return dtos.entrySet().stream()
            .filter(Objects::nonNull)
            .map(e -> createKafkaMessage(e.getValue(), e.getKey(), key))
            .collect(Collectors.toList());
    }

    public static <T> List<KafkaMessage> transform(List<T> dtos, String key) {
        return dtos.stream()
            .filter(Objects::nonNull)
            .map(e -> createKafkaMessage(e, null, key))
            .collect(Collectors.toList());
    }

    private static <T> KafkaMessage createKafkaMessage(T t, String id, String key) {
        KafkaMessage message = new KafkaMessage();

        try {
            KafkaMessagePayload<T> payload = new KafkaMessagePayload<>(t, id);
            message = new KafkaMessage(new Date(),
                    UUID.randomUUID().toString(), key, payload);
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return message;
    }
}
