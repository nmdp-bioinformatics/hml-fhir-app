package org.nmdp.hmlfhirconverterapi.controller;

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


import io.swagger.api.fhir.FhirApi;

import org.apache.log4j.Logger;

import org.nmdp.hmlfhirconverterapi.config.KafkaConfig;
import org.nmdp.hmlfhirconverterapi.service.FhirService;
import org.nmdp.hmlfhirconverterapi.util.FileConverter;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;
import org.nmdp.kafkaproducer.kafka.KafkaProducerService;

import org.nmdp.kafkaproducer.util.ConvertToKafkaMessage;
import org.nmdp.servicekafkaproducermodel.models.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/fhir")
@CrossOrigin
public class FhirController implements FhirApi {

    private static final Logger LOG = Logger.getLogger(FhirController.class);
    private final FhirService fhirService;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaConfig kafkaConfig;
    //

    @Autowired
    public FhirController(FhirService fhirService, KafkaProducerService kafkaProducerService) {
        this.fhirService = fhirService;
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaConfig = KafkaConfig.getConfig();
    }

    @Override
    @RequestMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Callable<ResponseEntity<Boolean>> convertFhirFileToHml(@RequestBody MultipartFile file) {
        try {
            List<FhirMessage> fhirMessages = fhirService.convertByteArrayToFhirMessages(file.getBytes());
            Map<String, FhirMessage> dbFhirs = fhirService.writeFhirToMongoConversionDb(fhirMessages);
            List<KafkaMessage> kafkaMessages = ConvertToKafkaMessage.transform(dbFhirs, kafkaConfig.getMessageKey());
            kafkaProducerService.produceKafkaMessages(kafkaMessages, kafkaConfig.getFhirToHmlTopic(), kafkaConfig.getKey());
            return () -> new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error in file upload fhir to hml conversion.", ex);
            return () -> new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PATCH)
    public Callable<ResponseEntity<Boolean>> convert(@RequestBody String xml) {
        try {
            List<FhirMessage>fhirMessages = fhirService.convertStringToFhirMessages(xml);
            Map<String, FhirMessage> dbFhirs = fhirService.writeFhirToMongoConversionDb(fhirMessages);
            List<KafkaMessage> kafkaMessages = ConvertToKafkaMessage.transform(dbFhirs, kafkaConfig.getMessageKey());
            kafkaProducerService.produceKafkaMessages(kafkaMessages, kafkaConfig.getFhirToHmlTopic(), kafkaConfig.getKey());
            return () -> new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error in file upload fhir to hml conversion.", ex);
            return () -> new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/bundle/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.GET)
    public @ResponseBody Callable<ResponseEntity> downloadBundle(@PathVariable String id) {
        try {
            ByteArrayOutputStream stream = fhirService.getJsonBundle(id);
            return () -> new ResponseEntity(stream.toByteArray(),
                    getHeadersForDownload(id, "zip", "application/json"), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error downloading bundle", ex);
            return () -> new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.GET)
    public @ResponseBody Callable<ResponseEntity> downloadJson(@PathVariable String id) {
        try {
            return () -> new ResponseEntity(FileConverter.convertStringToBytes(fhirService.getJsonFhir(id)),
                    getHeadersForDownload(id, "json", "application/json"), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error downloading json.", ex);
            return () -> new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/{id}/xml", produces = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.GET)
    public @ResponseBody Callable<ResponseEntity> downloadXml(@PathVariable String id) {
        try {
            return () -> new ResponseEntity(FileConverter.convertStringToBytes(fhirService.getXmlFhir(id)),
                    getHeadersForDownload(id, "xml", "text/xml"), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error downloading json.", ex);
            return () -> new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders getHeadersForDownload(String fileName, String extension, String contentType) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("content-disposition", "attachment; filename=\"" + fileName + ".fhir." + extension + "\"");
        headers.add("Content-Type", contentType);

        return headers;
    }
}
