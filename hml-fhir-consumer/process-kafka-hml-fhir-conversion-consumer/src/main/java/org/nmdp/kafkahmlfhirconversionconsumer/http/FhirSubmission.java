package org.nmdp.kafkahmlfhirconversionconsumer.http;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/8/17.
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

import org.apache.log4j.Logger;

import org.nmdp.kafkahmlfhirconversionconsumer.config.UrlConfiguration;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FhirSubmission {

    private static final Logger LOG = Logger.getLogger(FhirSubmission.class);
    private static final String CONTROLLER = "submission";
    private static final String GET = "GET";

    private UrlConfiguration configuration;

    public FhirSubmission() {
        Yaml yaml = new Yaml();

        try {
            URL configUrl = new URL("file:." + "/src/main/resources/url-config.yaml");

            try (InputStream stream = configUrl.openStream()) {
                this.configuration = yaml.loadAs(stream, UrlConfiguration.class);
            }
        } catch (Exception ex) {
            LOG.error(ex);
            this.configuration = null;
        }
    }

    public void postSubmission(String statusId) throws IOException {
        URL url = new URL(configuration.getConverterApiUrl() + CONTROLLER + "/" + statusId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(GET);
        connection.getResponseCode();
    }
}
