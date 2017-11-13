package org.nmdp.hmlfhirconverterapi.controller;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nmdp.hmlfhirconverterapi.config.KafkaConfig;
import org.nmdp.hmlfhirconverterapi.service.FhirService;
import org.nmdp.hmlfhirconverterapi.service.StatusService;
import org.nmdp.hmlfhirconverterapi.service.SubmissionService;

import org.nmdp.hmlfhirconverterapi.util.FileConverter;
import org.nmdp.hmlfhirmongo.models.FhirSubmission;
import org.nmdp.hmlfhirmongo.models.Status;
import org.nmdp.kafkaproducer.kafka.KafkaProducerService;
import org.nmdp.kafkaproducer.util.ConvertToKafkaMessage;
import org.nmdp.servicekafkaproducermodel.models.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/submission")
@CrossOrigin
public class SubmissionController {

    private static final Logger LOG = Logger.getLogger(SubmissionController.class);
    private final SubmissionService submissionService;
    private final KafkaProducerService kafkaProducerService;
    private final FhirService fhirService;
    private final StatusService statusService;
    private final KafkaConfig kafkaConfig;

    @Autowired
    public SubmissionController(SubmissionService submissionService, KafkaProducerService kafkaProducerService,
        FhirService fhirService, StatusService statusService) {
        this.submissionService = submissionService;
        this.kafkaProducerService = kafkaProducerService;
        this.fhirService = fhirService;
        this.statusService = statusService;
        this.kafkaConfig = KafkaConfig.getConfig();
    }

    @RequestMapping(path = "/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Callable<ResponseEntity<Boolean>> produceKafkaMessage(@PathVariable String statusId) {
        try {
            Document statusRow = statusService.getStatus(statusId);
            String fhirId = statusRow.getString("fhirId");
            String fhirPresubmission = fhirService.getJsonFhir(fhirId);

            statusService.updateStatusStatus(Status.SUBMITTING, statusRow);

            List<KafkaMessage> kafkaMessages = ConvertToKafkaMessage.transform(Arrays.asList(fhirPresubmission), kafkaConfig.getMessageKey());
            kafkaProducerService.produceKafkaMessages(kafkaMessages, kafkaConfig.getFhirSubmissionTopic(), kafkaConfig.getKey());
            return () -> new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error in kafka message production.", ex);
            return () -> new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/{submissionId}/{get}", method = RequestMethod.GET)
    public Callable<ResponseEntity> getFhirSubmission(@PathVariable String submissionId) {
        try {
            Document document = submissionService.getFhirSubmission(submissionId);
            ArrayList submissionResult = document.get("submissionResult", ArrayList.class);
            List<String> parentBundles = new ArrayList<>();

            for (Object obj: submissionResult) {
                parentBundles.add(obj.toString());
            }

            JSONArray jsonArray = new JSONArray(parentBundles.toString());
            List<String> jsonBundles = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray array = jsonArray.getJSONArray(i);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject json = array.getJSONObject(j);
                    jsonBundles.add(json.toString());
                }
            }

            return () -> new ResponseEntity(FileConverter.convertStringToBytes(jsonBundles),
                    getHeadersForDownload(submissionId, "fhir", "application/json"), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error("Error in retriving submission record.", ex);
            byte[] array = new byte[0];
            InputStream isr = new ByteArrayInputStream(array);
            return () -> new ResponseEntity<>(new InputStreamReader(isr), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders getHeadersForDownload(String fileName, String extension, String contentType) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("content-disposition", "attachment; filename=\"" + fileName + "." + extension + "\"");
        headers.add("Content-Type", contentType);

        return headers;
    }
}
