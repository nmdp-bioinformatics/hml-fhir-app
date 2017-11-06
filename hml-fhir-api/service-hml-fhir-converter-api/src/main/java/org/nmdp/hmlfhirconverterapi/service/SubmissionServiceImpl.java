package org.nmdp.hmlfhirconverterapi.service;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 7/31/17.
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

import org.bson.Document;

import org.yaml.snakeyaml.Yaml;

import org.apache.log4j.Logger;

import org.nmdp.hmlfhirconverterapi.dao.FhirSubmissionRepository;
import org.nmdp.hmlfhirconverterapi.dao.custom.FhirSubmissionCustomRepository;
import org.nmdp.hmlfhirmongo.mongo.MongoFhirSubmissionDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service
public class SubmissionServiceImpl extends BaseService implements SubmissionService {

    private final Yaml yaml;
    private static final Logger LOG = Logger.getLogger(SubmissionServiceImpl.class);
    private final FhirSubmissionCustomRepository customRepository;
    private final FhirSubmissionRepository repository;
    private final MongoFhirSubmissionDatabase database;

    @Autowired
    public SubmissionServiceImpl(@Qualifier("fhirSubmissionCustomRepository") FhirSubmissionCustomRepository customRepository,
        @Qualifier("fhirSubmissionRepository") FhirSubmissionRepository repository) {
        this.customRepository = customRepository;
        this.repository = repository;
        this.yaml = new Yaml();
        org.nmdp.hmlfhirmongo.config.MongoConfiguration config = null;

        try {
            URL url = new URL("file:." + "/src/main/resources/mongo-configuration.yaml");
            try (InputStream is = url.openStream()) {
                config = yaml.loadAs(is, org.nmdp.hmlfhirmongo.config.MongoConfiguration.class);
            }
        } catch(Exception ex) {
            LOG.error("Error instantiating ConversionStatus database.", ex);
        } finally {
            this.database = new MongoFhirSubmissionDatabase(config);
        }
    }

    @Override
    public Document getFhirSubmission(String id) throws Exception {
        try {
            return convertId(database.get(id));
        } catch (Exception ex) {
            LOG.error("Error reading Submission from Mongo.", ex);
            throw ex;
        }
    }
}
