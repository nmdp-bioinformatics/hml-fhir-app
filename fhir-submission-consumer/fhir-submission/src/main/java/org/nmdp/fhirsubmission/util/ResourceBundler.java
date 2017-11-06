package org.nmdp.fhirsubmission.util;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 10/11/17.
 * <p>
 * fhir-submission
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

import org.apache.log4j.Logger;
import org.nmdp.fhirsubmission.object.BundleReference;
import org.nmdp.fhirsubmission.object.BundleSubmission;
import org.nmdp.fhirsubmission.serialization.DiagnosticReportJsonSerializer;
import org.nmdp.fhirsubmission.serialization.ObservationJsonSerializer;
import org.nmdp.fhirsubmission.serialization.PatientJsonSerializer;
import org.nmdp.fhirsubmission.serialization.SpecimenJsonSerializer;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Observation;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Patient;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Patients;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Specimens;

import java.util.*;
import java.util.concurrent.*;

public class ResourceBundler {

    private static final String RESOURCE_TYPE_KEY = "resourceType";
    private static final String RESOURCE_TYPE_VALUE = "Bundle";
    private static final String BUNDLE_TYPE_KEY = "type";
    //private static final String BUNDLE_TYPE_VALUE = "collection";
    private static final String BUNDLE_TYPE_VALUE = "transaction";
    private static final String REQUEST_METHOD_KEY = "method";
    private static final String REQUEST_METOHD_VALUE = "POST";
    private static final String REQUEST_URL_KEY = "url";
    private static final String REQUEST_KEY = "request";
    private static final String VALUE_KEY = "value";
    private static final String SUBJECT_KEY = "subject";
    private static final String SPECIMEN_KEY = "specimen";
    private static final String REFERENCE_KEY = "reference";
    private static final String ENTRY = "entry";
    private static final String RESOURCE = "resource";
    private static final String FULL_URL = "fullUrl";
    private static final String GUID_PREFIX = "urn:uuid:";
    private static final String PATIENT_RESOURCE = "Patient";
    private static final String SPECIMEN_RESOURCE = "Specimen";
    private static final String DISPLAY_KEY = "display";
    private static final String OBSERVATION_RESOURCE = "Observation";
    private static final String DIAGNOSTIC_REPORT_RESOURCE = "DiagnosticReport";
    private static final String RESULT_KEY = "result";

    private static final Logger LOG = Logger.getLogger(ResourceBundler.class);

    public JsonArray serialize(FhirMessage fhir) {
        JsonArray patientBundle = new JsonArray();
        Patients patients = fhir.getPatients();

        for (Patient patient : patients.getPatients()) {
            BundleSubmission bundle = new BundleSubmission();
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            bundle.setPatient(serializeToJsonSingleton(
                    getConverter(Patient.class, new PatientJsonSerializer()), patient, executorService));
            Specimens specimens = patient.getSpecimens();

            for (Specimen specimen : specimens.getSpecimens()) {
                String specimenId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
                bundle.addSpecimen(specimenId, serializeToJsonSingleton(getConverter(Specimen.class, new SpecimenJsonSerializer()),
                        specimen, executorService));
                bundle.addDiagnosticReport(specimenId, serializeToJsonSingleton(getConverter(Specimen.class, new DiagnosticReportJsonSerializer()),
                        specimen, executorService));
                Observations observations = specimen.getObservations();
                for (Observation observation : observations.getObservations()) {
                    bundle.addObservation(specimenId, serializeToJsonSingleton(getConverter(Observation.class, new ObservationJsonSerializer()),
                            observation, executorService));
                }
            }

            patientBundle.add(combine(bundle));
        }

        return patientBundle;
    }

    private String serializeToJsonSingleton(Gson gson, Object obj, ExecutorService executor) {
        try {
            Callable<String> callable = deserialize(obj, gson);
            Future<String> json = executor.submit(callable);
            return json.get();
        } catch (InterruptedException ex) {
            LOG.error(ex);
        } catch (ExecutionException ex) {
            LOG.error(ex);
        }

        return null;
    }

    private JsonObject combine(BundleSubmission bundleSubmission) {
        JsonObject bundle = new JsonObject();
        JsonArray entry = new JsonArray();
        Gson gson = new GsonBuilder().create();
        String patientId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());

        handleBundle(bundleSubmission, gson, patientId, entry);
        bundle.addProperty(RESOURCE_TYPE_KEY, RESOURCE_TYPE_VALUE);
        bundle.addProperty(BUNDLE_TYPE_KEY, BUNDLE_TYPE_VALUE);
        bundle.add(ENTRY, entry);

        return bundle;
    }

    private void handleBundle(BundleSubmission bundle, Gson gson, String patientId, JsonArray entry) {
        for (Map.Entry<String, String> specimen : bundle.getSpecimens().entrySet()) {
            String specimenId = specimen.getKey();
            Map<String, BundleReference> specimenReferences = new HashMap<>();
            Map<String, BundleReference> diagnosticReportReferences = new HashMap<>();
            Map<String, BundleReference> observationReferences = new HashMap<>();

            specimenReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SPECIMEN_KEY, new BundleReference(specimenId));
            observationReferences.put(SUBJECT_KEY, new BundleReference(patientId));

            entry.add(createJsonObject(bundle.getPatient(), gson, PATIENT_RESOURCE, patientId, new HashMap<>()));
            entry.add(createJsonObject(specimen.getValue(), gson, SPECIMEN_RESOURCE, specimenId, specimenReferences));
            String diagnosticReportId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
            String diagnosticReport = bundle.getDiangosticReports().getOrDefault(specimenId, null);
            List<String> observations = bundle.getObservations().getOrDefault(specimenId, new ArrayList<>());
            Map<String, JsonObject> observationResults = new HashMap<>();

            for (String observation : observations) {
                String observationId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
                JsonObject obs = createJsonObject(observation, gson, OBSERVATION_RESOURCE, observationId, observationReferences);
                entry.add(obs);
                observationResults.put(observationId, obs);
            }

            diagnosticReport = handleDiagnosticReport(observationResults, diagnosticReport, gson);
            entry.add(createJsonObject(diagnosticReport, gson, DIAGNOSTIC_REPORT_RESOURCE, diagnosticReportId, diagnosticReportReferences));
        }
    }

    private String handleDiagnosticReport(Map<String, JsonObject> observations, String diagnosticReport, Gson gson) {
        JsonObject request = gson.fromJson(diagnosticReport, JsonObject.class);
        JsonArray result = new JsonArray();

        request.remove(RESULT_KEY);
        observations.entrySet().forEach(observation -> result.add(createResultObject(observation.getValue())));
        request.add(RESULT_KEY, result);

        return gson.toJson(request);
    }

    private JsonObject createResultObject(JsonObject observation) {
        JsonObject obs = new JsonObject();
        JsonObject resource = observation.get(RESOURCE).getAsJsonObject();

        obs.addProperty(DISPLAY_KEY, resource.get(VALUE_KEY).getAsString());
        obs.addProperty(REFERENCE_KEY, observation.get(FULL_URL).getAsString());

        return obs;
    }

    private JsonObject createJsonObject(String str, Gson gson, String resource, String id, Map<String, BundleReference> refs) {
        JsonObject json = new JsonObject();
        JsonObject request = new JsonObject();

        if (str == null) {
            return json;
        }

        JsonObject incoming = gson.fromJson(str, JsonObject.class);
        json.add(RESOURCE, incoming);
        json.addProperty(FULL_URL, id);
        request.addProperty(REQUEST_METHOD_KEY, REQUEST_METOHD_VALUE);
        request.addProperty(REQUEST_URL_KEY, resource);

        for (Map.Entry<String, BundleReference> ref : refs.entrySet()) {
            BundleReference bundleReference = ref.getValue();
            addReferenceToObject(bundleReference.getRefId(), ref.getKey(), json, bundleReference.getPropertyMap());
        }

        json.add(REQUEST_KEY, request);

        return json;
    }

    private void addReferenceToObject(String refId, String propertyName, JsonObject json, List<String> propMap) {
        JsonObject referenceJson = new JsonObject();
        JsonObject mutableJson = new JsonObject();

        for (String pn : propMap) {
            mutableJson = (JsonObject) json.get(pn);
        }

        referenceJson.addProperty(REFERENCE_KEY, refId);
        mutableJson.remove(propertyName);
        mutableJson.add(propertyName, referenceJson);
    }

    private Callable<String> deserialize(Object obj, Gson gson) {
        Callable<String> task = () -> {
            return gson.toJson(obj);
        };

        return task;
    }

    private <T> Gson getConverter(Class<T> clazz, JsonSerializer serializer) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(clazz, serializer);
        return builder.create();
    }
}
