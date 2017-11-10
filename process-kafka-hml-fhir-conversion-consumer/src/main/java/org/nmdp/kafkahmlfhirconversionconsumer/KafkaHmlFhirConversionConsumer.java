package org.nmdp.kafkahmlfhirconversionconsumer;

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

import org.nmdp.kafkahmlfhirconversionconsumer.config.ApplicationProperties;
import org.nmdp.kafkahmlfhirconversionconsumer.kafka.KafkaHmlFhirConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaHmlFhirConversionConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaHmlFhirConversionConsumer.class);
    private static final String APPLICATION_PROPERTIES_PATH = "/application.properties";

    public static void main(String[] args) throws Throwable {
        KafkaHmlFhirConverter khbw = null;
        String applicationPropertiesPath = APPLICATION_PROPERTIES_PATH;
        LOG.info("Starting application.");

        if (args.length > 0) {
            applicationPropertiesPath = args[0];
        }

        try {
            ApplicationProperties applicationProperties = new ApplicationProperties(applicationPropertiesPath);
            khbw = new KafkaHmlFhirConverter(applicationProperties);
        } catch (Exception ex) {
            if (khbw != null) {
                LOG.error("Exception thrown: " + ex + "\nShutting Down.");
                khbw.shutdown();
            }
        }
    }
}
