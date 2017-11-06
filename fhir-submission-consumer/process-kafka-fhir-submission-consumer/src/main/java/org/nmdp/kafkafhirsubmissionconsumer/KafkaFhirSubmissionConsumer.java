package org.nmdp.kafkafhirsubmissionconsumer;

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

import org.nmdp.kafkafhirsubmissionconsumer.kafka.FhirSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class KafkaFhirSubmissionConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaFhirSubmissionConsumer.class);

    public static void main(String[] args) throws Throwable {
        FhirSubmission khbw = null;
        URL consumerConfigUrl;

        try {
            consumerConfigUrl = new URL("file:." + "/src/main/resources/consumer-configuration.yaml");
            khbw = new FhirSubmission(consumerConfigUrl);
        } catch (Exception ex) {
            if (khbw != null) {
                LOG.error("Exception thrown: " + ex + "\nShutting Down.");
                khbw.shutdown();
            }
        }
    }
}
