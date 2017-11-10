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
import org.nmdp.fhirsubmission.serialization.*;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.*;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Patients;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Sequences;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Specimens;

import java.util.*;
import java.util.concurrent.*;

public class ResourceBundler {

    private static final String RESOURCE_TYPE_KEY = "resourceType";
    private static final String RESOURCE_TYPE_VALUE = "Bundle";
    private static final String BUNDLE_TYPE_KEY = "type";
    private static final String BUNDLE_TYPE_VALUE = "transaction";
    private static final String REQUEST_METHOD_KEY = "method";
    private static final String REQUEST_METOHD_VALUE = "POST";
    private static final String REQUEST_URL_KEY = "url";
    private static final String REQUEST_KEY = "request";
    private static final String STATUS_KEY = "status";
    private static final String STATUS_VALUE = "final";
    private static final String SUBJECT_KEY = "subject";
    private static final String SPECIMEN_KEY = "specimen";
    private static final String REFERENCE_KEY = "reference";
    private static final String RELATED_KEY = "related";
    private static final String ENTRY = "entry";
    private static final String RESOURCE = "resource";
    private static final String FULL_URL = "fullUrl";
    private static final String GUID_PREFIX = "urn:uuid:";
    private static final String PATIENT_RESOURCE = "Patient";
    private static final String SPECIMEN_RESOURCE = "Specimen";
    private static final String DISPLAY_KEY = "display";
    private static final String OBSERVATION_RESOURCE = "Observation";
    private static final String DIAGNOSTIC_REPORT_RESOURCE = "DiagnosticReport";
    private static final String SEQUENCE_RESOURCE = "Sequence";
    private static final String RESULT_KEY = "result";
    private static final String CODE_KEY = "code";
    private static final String SYSTEM_KEY = "system";
    private static final String SYSTEM_VALUE = "http://loinc.org";
    private static final String CODE_VALUE = "51959-5";
    private static final String CODING_KEY = "coding";
    private static final String TYPE_KEY = "type";
    private static final String DERIVED_FROM_VALUE = "derived-from";
    private static final String HAS_MEMBER_VALUE = "has-member";
    private static final String TARGET_KEY = "target";
    private static final String VALUE_STRING_KEY = "valueString";

    private static final Logger LOG = Logger.getLogger(ResourceBundler.class);

    public JsonArray serialize(FhirMessage fhir) {
        JsonArray patientBundle = new JsonArray();
        Patients patients = fhir.getPatients();

        for (Patient patient : patients.getPatients()) {
            BundleSubmission bundle = new BundleSubmission();
            String patientId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
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
                Sequences sequences = new Sequences();
                sequences.setSequences(new ArrayList<>());

                for (Observation observation : observations.getObservations()) {
                    Sequences seqs = observation.getSequences();
                    sequences.getSequences().addAll(seqs.getSequences());
                    bundle.addObservation(specimenId, serializeToJsonSingleton(getConverter(Observation.class, new ObservationJsonSerializer()),
                            observation, executorService));
                }

                for (Sequence sequence : sequences.getSequences()) {
                    JsonObject seq = serializeToJsonObject(getConverter(Sequence.class, new SequenceJsonSerializer()), sequence, executorService);
                    JsonObject patientRef = new JsonObject();
                    JsonObject specimenRef = new JsonObject();

                    patientRef.addProperty(REFERENCE_KEY, patientId);
                    specimenRef.addProperty(REFERENCE_KEY, specimenId);
                    seq.add(SPECIMEN_KEY, specimenRef);

                    String sequenceJson = serializeToJsonSingleton(new GsonBuilder().create(), seq, executorService);
                    bundle.addSequence(specimenId, sequenceJson);
                }
            }

            patientBundle.add(combine(bundle));
        }

        return patientBundle;
    }

    private JsonObject serializeToJsonObject(Gson gson, Object obj, ExecutorService executor) {
        try {
            Callable<JsonObject> callable = deserializeJson(obj, gson);
            Future<JsonObject> json = executor.submit(callable);
            return json.get();
        } catch (InterruptedException ex) {
            LOG.error(ex);
        } catch (ExecutionException ex) {
            LOG.error(ex);
        }

        return null;
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
            Map<String, BundleReference> sequenceReferences = new HashMap<>();

            specimenReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SPECIMEN_KEY, new BundleReference(specimenId));
            sequenceReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            sequenceReferences.put(SPECIMEN_KEY, new BundleReference(specimenId));
            observationReferences.put(SUBJECT_KEY, new BundleReference(patientId));

            entry.add(createJsonObject(bundle.getPatient(), gson, PATIENT_RESOURCE, patientId, new HashMap<>()));
            entry.add(createJsonObject(specimen.getValue(), gson, SPECIMEN_RESOURCE, specimenId, specimenReferences));
            String diagnosticReportId = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
            String diagnosticReport = bundle.getDiangosticReports().getOrDefault(specimenId, null);
            List<String> observations = bundle.getObservations().getOrDefault(specimenId, new ArrayList<>());
            List<String> sequences = bundle.getSequences().getOrDefault(specimenId, new ArrayList<>());
            Map<String, JsonObject> observationResults = new HashMap<>();
            Map<String, JsonObject> sequenceResults = new HashMap<>();

            for (String sequence : sequences) {
                loopRelatedResources(entry, SEQUENCE_RESOURCE, sequenceReferences, sequence, gson, sequenceResults);
            }

            String sequenceObservation = handleSequenceObservation(sequenceResults, patientId, gson);
            String sequenceObservationId = loopRelatedResources(entry, OBSERVATION_RESOURCE, observationReferences, sequenceObservation, gson, null);

            for (String observation : observations) {
                loopObservations(entry, OBSERVATION_RESOURCE, observationReferences, observation, gson, observationResults, sequenceObservationId);
            }

            diagnosticReport = handleDiagnosticReport(observationResults, diagnosticReport, gson);
            entry.add(createJsonObject(diagnosticReport, gson, DIAGNOSTIC_REPORT_RESOURCE, diagnosticReportId, diagnosticReportReferences));
        }
    }

    private void loopObservations(JsonArray entry, String resourceType, Map<String, BundleReference> references, String data,
                                  Gson gson, Map<String, JsonObject> idMap, String sequenceObservationId) {
        String id = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
        JsonObject json = createJsonObject(data, gson, resourceType, id, references);
        JsonObject related = new JsonObject();
        JsonObject target = new JsonObject();

        related.addProperty(TYPE_KEY, DERIVED_FROM_VALUE);
        target.addProperty(REFERENCE_KEY, sequenceObservationId);

        related.add(TARGET_KEY, target);
        json.add(RELATED_KEY, related);
        entry.add(json);
        idMap.put(id, json);
    }

    private String loopRelatedResources(JsonArray entry, String resourceType, Map<String, BundleReference> references, String data, Gson gson, Map<String, JsonObject> idMap) {
        String id = String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
        JsonObject json = createJsonObject(data, gson, resourceType, id, references);
        entry.add(json);

        if (idMap != null) {
            idMap.put(id, json);
        }

        return id;
    }

    private String handleSequenceObservation(Map<String, JsonObject> sequences, String patientId, Gson gson) {
        JsonObject observation = new JsonObject();
        JsonArray sequenceRefs = new JsonArray();
        JsonObject code = new JsonObject();
        JsonObject coding = new JsonObject();
        JsonObject subject = new JsonObject();

        for (Map.Entry<String, JsonObject> entry : sequences.entrySet()) {
            JsonObject sequence = new JsonObject();
            JsonObject target = new JsonObject();

            sequence.addProperty(TYPE_KEY, HAS_MEMBER_VALUE);
            target.addProperty(REFERENCE_KEY, entry.getKey());
            target.addProperty(DISPLAY_KEY, SEQUENCE_RESOURCE);
            sequence.add(TARGET_KEY, target);
            sequenceRefs.add(sequence);
        }

        subject.addProperty(REFERENCE_KEY, patientId);
        coding.addProperty(CODE_KEY, CODE_VALUE);
        coding.addProperty(SYSTEM_KEY, SYSTEM_VALUE);
        code.add(CODING_KEY, coding);
        observation.addProperty(RESOURCE_TYPE_KEY, OBSERVATION_RESOURCE);
        observation.addProperty(STATUS_KEY, STATUS_VALUE);
        observation.add(SUBJECT_KEY, subject);
        observation.add(RELATED_KEY, sequenceRefs);
        observation.add(CODE_KEY, code);

        return gson.toJson(observation);
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

        obs.addProperty(DISPLAY_KEY, resource.get(VALUE_STRING_KEY).getAsString());
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

    private Callable<JsonObject> deserializeJson(Object obj, Gson gson) {
        Callable<JsonObject> task = () -> {
            return (JsonObject) gson.toJsonTree(obj);
        };

        return task;
    }

    private <T> Gson getConverter(Class<T> clazz, JsonSerializer serializer) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(clazz, serializer);
        return builder.create();
    }
}
