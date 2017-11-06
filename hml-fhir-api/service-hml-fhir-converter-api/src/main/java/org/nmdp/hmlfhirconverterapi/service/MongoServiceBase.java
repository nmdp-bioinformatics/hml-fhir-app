package org.nmdp.hmlfhirconverterapi.service;

import org.apache.log4j.Logger;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;
import org.nmdp.hmlfhirmongo.config.MongoConfiguration;
import org.nmdp.hmlfhirmongo.models.ConversionStatus;
import org.nmdp.hmlfhirmongo.models.Status;
import org.nmdp.hmlfhirmongo.mongo.MongoConversionStatusDatabase;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/22/17.
 * <p>
 * service-hml-fhir-converter-api
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

public abstract class MongoServiceBase {

    protected final MongoConversionStatusDatabase statusDatabase;
    private final Yaml yaml;
    private final static Logger LOG = Logger.getLogger(MongoServiceBase.class);

    public MongoServiceBase() {
        this.yaml = new Yaml();
        this.statusDatabase = createStatusDatabase();
    }

    protected Map<String, FhirMessage> writeFhirConversionStatusToMongo(List<FhirMessage> fhirMessages) {
        Map<String, FhirMessage> ids = new HashMap<>();

        for (FhirMessage fhirMessage : fhirMessages) {
            ConversionStatus status = new ConversionStatus(fhirMessage.getId(), Status.QUEUED, 0);
            ids.put(statusDatabase.save(status).getId(), fhirMessage);
        }

        return ids;
    }

    protected Map<String, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml> writeHmlConversionStatusToMongo(List<org.nmdp.hmlfhirconvertermodels.dto.hml.Hml> hmls) {
        Map<String, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml> ids = new HashMap<>();

        for (org.nmdp.hmlfhirconvertermodels.dto.hml.Hml hml : hmls) {
            ConversionStatus status = new ConversionStatus(hml.getId(), Status.QUEUED, 0);
            ids.put(statusDatabase.save(status).getId(), hml);
        }

        return ids;
    }

    private MongoConversionStatusDatabase createStatusDatabase() {
        org.nmdp.hmlfhirmongo.config.MongoConfiguration config = null;

        try {
            URL url = new URL("file:." + "/src/main/resources/mongo-configuration.yaml");

            try (InputStream is = url.openStream()) {
                config = yaml.loadAs(is, org.nmdp.hmlfhirmongo.config.MongoConfiguration.class);
            }

            return new MongoConversionStatusDatabase(config);
        } catch (Exception ex) {
            LOG.error(ex);
            return new MongoConversionStatusDatabase(null);
        }
    }
}
