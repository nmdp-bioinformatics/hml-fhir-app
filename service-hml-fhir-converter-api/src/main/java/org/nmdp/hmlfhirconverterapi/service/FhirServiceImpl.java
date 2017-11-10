package org.nmdp.hmlfhirconverterapi.service;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/8/17.
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

import org.apache.log4j.Logger;

import org.bson.Document;
import org.nmdp.hmlfhir.ConvertFhirToHml;
import org.nmdp.hmlfhir.ConvertFhirToHmlImpl;
import org.nmdp.hmlfhirconverterapi.dao.FhirRepository;
import org.nmdp.hmlfhirconverterapi.dao.custom.FhirCustomRepository;
import org.nmdp.hmlfhirconverterapi.util.Serializer;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;
import org.nmdp.hmlfhirmongo.mongo.MongoFhirDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FhirServiceImpl extends MongoServiceBase implements FhirService {

    private final Yaml yaml;
    private static final Logger LOG = Logger.getLogger(FhirServiceImpl.class);
    private final FhirCustomRepository customRepository;
    private final FhirRepository repository;
    private final MongoFhirDatabase fhirDatabase;

    @Autowired
    public FhirServiceImpl(@Qualifier("fhirCustomRepository") FhirCustomRepository customRepository, @Qualifier("fhirRepository") FhirRepository repository) {
        this.yaml = new Yaml();
        this.customRepository = customRepository;
        this.repository = repository;
        this.fhirDatabase = createFhirDatabase();
    }

    @Override
    public Map<String, FhirMessage> writeFhirToMongoConversionDb(List<FhirMessage> fhirMessages) {
        List<FhirMessage> ids = new ArrayList<>();

        for (FhirMessage fhirMessage : fhirMessages) {
            ids.add(fhirDatabase.save(fhirMessage));
        }

        return super.writeFhirConversionStatusToMongo(ids);
    }

    @Override
    public List<FhirMessage> convertByteArrayToFhirMessages(byte[] bytes) throws Exception {
        try {
            ConvertFhirToHml converter = new ConvertFhirToHmlImpl();
            List<FhirMessage> fhirMessages = new ArrayList<>();
            fhirMessages.add(converter.toDto(new String(bytes), null));

            return fhirMessages;
        } catch (Exception ex) {
            LOG.error("Error converting file to FhirMessage.", ex);
            throw ex;
        }
    }

    @Override
    public List<FhirMessage> convertStringToFhirMessages(String xml) throws Exception {
        try {
            ConvertFhirToHml converter = new ConvertFhirToHmlImpl();
            List<FhirMessage> fhirMessages = new ArrayList<>();
            fhirMessages.add(converter.toDto(xml, null));

            return fhirMessages;
        } catch (Exception ex) {
            LOG.error("Error converting file to FhirMessage.", ex);
            throw ex;
        }
    }

    @Override
    public String getJsonFhir(String id) {
        try {
            return Serializer.toJson(getFhirFromMongo(id), id);
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }

    @Override
    public String getXmlFhir(String id) {
        try {
            return Serializer.toXml(getFhirFromMongo(id));
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }

    private MongoFhirDatabase createFhirDatabase() {
        org.nmdp.hmlfhirmongo.config.MongoConfiguration config = null;

        try {
            URL url = new URL("file:." + "/src/main/resources/mongo-configuration.yaml");

            try (InputStream is = url.openStream()) {
                config = yaml.loadAs(is, org.nmdp.hmlfhirmongo.config.MongoConfiguration.class);
            }

            return new MongoFhirDatabase(config);
        } catch (Exception ex) {
            LOG.error(ex);
            return new MongoFhirDatabase(null);
        }
    }

    private Document getFhirFromMongo(String id) throws Exception {
        return fhirDatabase.get(id);
    }
}
