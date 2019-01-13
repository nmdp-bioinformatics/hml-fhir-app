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

import com.google.gson.*;
import io.swagger.util.Json;
import org.apache.log4j.Logger;

import org.bson.Document;
import org.nmdp.hmlfhir.ConvertFhirToHml;
import org.nmdp.hmlfhir.ConvertFhirToHmlImpl;
import org.nmdp.hmlfhirconverterapi.config.ApplicationProperties;
import org.nmdp.hmlfhirconverterapi.dao.FhirRepository;
import org.nmdp.hmlfhirconverterapi.dao.custom.FhirCustomRepository;
import org.nmdp.hmlfhirconverterapi.util.Serializer;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;
import org.nmdp.hmlfhirmongo.config.MongoConfiguration;
import org.nmdp.hmlfhirmongo.mongo.MongoFhirDatabase;

import org.nmdp.hmlfhirmongo.mongo.MongoFhirSubmissionDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FhirServiceImpl extends MongoServiceBase implements FhirService {

    private final Yaml yaml;
    private static final Logger LOG = Logger.getLogger(FhirServiceImpl.class);
    private final FhirCustomRepository customRepository;
    private final FhirRepository repository;
    private final MongoFhirDatabase fhirDatabase;
    private final MongoFhirSubmissionDatabase fhirSubmissionDatabase;
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

    @Autowired
    public FhirServiceImpl(@Qualifier("fhirCustomRepository") FhirCustomRepository customRepository, @Qualifier("fhirRepository") FhirRepository repository) {
        this.yaml = new Yaml();
        this.customRepository = customRepository;
        this.repository = repository;

        MongoConfiguration mc = null;

        try {
            ApplicationProperties applicationProperties = new ApplicationProperties();
            File file = new File(applicationProperties.getMongoConfigurationPath());
            URL url = file.toURL();
            try (InputStream is = url.openStream()) {
                mc = yaml.loadAs(is, MongoConfiguration.class);
            }
        } catch (IOException ex) {
            LOG.error(ex);
        } finally {
            this.fhirDatabase = new MongoFhirDatabase(mc);
            this.fhirSubmissionDatabase = new MongoFhirSubmissionDatabase(mc);
        }
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

    @Override
    public ByteArrayOutputStream getJsonBundle(String id) throws IOException {
        try {
            String json = Serializer.toJson(getBundle(id));
            JsonObject obj = gson.fromJson(json, JsonObject.class);
            JsonArray array = obj.get("submissionResult").getAsJsonArray();

            return toStream(Arrays.asList(array));
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }

    private ByteArrayOutputStream toStream(List<JsonArray> jsonArrays) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for (JsonArray json : jsonArrays) {
            Iterator iterator = json.iterator();

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                while (iterator.hasNext()) {
                    JsonArray inner = (JsonArray) iterator.next();
                    Iterator innerIterator = inner.iterator();

                    while (innerIterator.hasNext()) {
                        String s = innerIterator.next().toString();
                        JsonPrimitive jsonPrimitive = gson.fromJson(s, JsonPrimitive.class);
                        JsonObject obj = gson.fromJson(jsonPrimitive.getAsString(), JsonObject.class);
                        String str = gson.toJson(obj);
                        String newStr = str
                                .replace("\\\"", "\"")
                                .replace("\"{", "{")
                                .replace("}\"", "}")
                                .replace("\\\\\"", "\\\"")
                                .replace("<0x00>", "");
                        ZipEntry zipEntry = new ZipEntry(String.format("%s.fhir.json", getFileName(obj)));
                        ByteBuffer buffer = StandardCharsets.UTF_8.encode(newStr);

                        zipOutputStream.putNextEntry(zipEntry);
                        zipOutputStream.write(buffer.array());
                        zipOutputStream.closeEntry();
                    }
                }
            }
        }

        return byteArrayOutputStream;
    }

    private String getFileName(JsonObject obj) {
        JsonArray entry = obj.get("entry").getAsJsonArray();
        JsonObject specimen = getSpecimen(entry);

        if (specimen == null) {
            return "file";
        }

        JsonObject resource = specimen.get("resource").getAsJsonObject();
        JsonArray identifier = resource.get("identifier").getAsJsonArray();
        JsonObject id = identifier.get(0).getAsJsonObject();

        return id.get("value").getAsString();
    }

    private JsonObject getSpecimen(JsonArray array) {
        Iterator iterator = array.iterator();

        while (iterator.hasNext()) {
            JsonObject obj = (JsonObject) iterator.next();
            JsonObject resource = obj.get("resource").getAsJsonObject();

            if (resource.get("resourceType").getAsString().equals("Specimen")) {
                return obj;
            }
        }

        return null;
    }

    private Document getBundle(String id) throws Exception {
        return fhirSubmissionDatabase.get(id);
    }

    private Document getFhirFromMongo(String id) throws Exception {
        return fhirDatabase.get(id);
    }
}
