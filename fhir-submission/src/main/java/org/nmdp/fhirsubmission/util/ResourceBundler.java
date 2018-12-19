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

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
    private static final String DEVICE_RESOURCE = "Device";
    private static final String PROVENANCE_RESOURCE = "Provenance";
    private static final String PROCEDURE_REQUEST_RESOURCE = "ProcedureRequest";
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
    private static final String COMPONENT_KEY = "component";
    private static final String VALUE_CODEABLE_CONCEPT_KEY = "valueCodeableConcept";
    private static final String VALUE_KEY = "value";
    private static final String TEXT_KEY = "text";
    private static final String WHO_REFERENCE_KEY = "whoReference";
    private static final String RECORDED_KEY = "recorded";
    private static final String AGENT_KEY = "agent";
    private static final String WHAT_IDENTIFIER_KEY = "whatIdentifier";
    private static final String EXTENSION_KEY = "extension";
    private static final String URL_KEY = "url";
    private static final String ROLE_KEY = "role";
    private static final String MODEL_KEY = "model";
    private static final String VERSION_KEY = "version";
    private static final String NOTE_KEY = "note";
    private static final String INTENT_KEY = "intent";
    private static final String IDENTIFIER_KEY = "identifier";

    private static final Logger LOG = Logger.getLogger(ResourceBundler.class);

    public JsonArray serialize(FhirMessage fhir) {
        JsonArray patientBundle = new JsonArray();
        Patients patients = fhir.getPatients();

        for (Patient patient : patients.getPatients()) {
            BundleSubmission bundle = new BundleSubmission();
            String patientId = generateId();
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            bundle.setPatient(serializeToJsonSingleton(
                    getConverter(Patient.class, new PatientJsonSerializer()), patient, executorService));
            Specimens specimens = patient.getSpecimens();
            String hmlIdValue = patient.getIdentifier().getValue();
            String hmlRootId = hmlIdValue.split("_")[0];
            String hmlExtensionId = hmlIdValue.split("_")[1];

            for (Specimen specimen : specimens.getSpecimens()) {
                String centerCode = specimen.getIdentifier().getSystem();
                String sampleId = specimen.getIdentifier().getValue();
                String specimenId = generateId();
                bundle.addSpecimen(specimenId, serializeToJsonSingleton(getConverter(Specimen.class, new SpecimenJsonSerializer()),
                        specimen, executorService));
                bundle.addDiagnosticReport(specimenId, serializeToJsonSingleton(getConverter(Specimen.class, new DiagnosticReportJsonSerializer()),
                        specimen, executorService));
                Observations observations = specimen.getObservations();
                Sequences sequences = new Sequences();
                sequences.setSequences(new ArrayList<>());
                Gson gson = new GsonBuilder().create();

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

                for (String hla : getAlleleTypings(observations.getObservations())) {
                    String procedureRequest = getProcedureRequestResource(hla, "", centerCode, sampleId, gson);
                    bundle.addProcedureRequest(specimenId, procedureRequest);
                }

                bundle.getDevices().put(specimenId, handleDevice(gson));
                bundle.getProvenances().put(specimenId, handleProvenance(new JsonArray(), gson, hmlRootId, hmlExtensionId));
            }

            patientBundle.add(combine(bundle, hmlRootId, hmlExtensionId));
        }

        return patientBundle;
    }

    private List<String> getAlleleTypings(List<Observation> observations) {
        List<String> hlaTypes = new ArrayList<>();

        for (Observation observation : observations) {
            List<String> glStrings = observation.getGlstrings().getGlstrings()
                    .stream()
                    .map(gls -> gls.getValue().substring(0, 5))
                    .collect(Collectors.toList());
            hlaTypes.addAll(glStrings);
        }

        return hlaTypes.stream().distinct().collect(Collectors.toList());
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

    private JsonObject combine(BundleSubmission bundleSubmission, String hmlRootId, String hmlExtensionId) {
        JsonObject bundle = new JsonObject();
        JsonArray entry = new JsonArray();
        Gson gson = new GsonBuilder().create();
        String patientId = generateId();

        handleBundle(bundleSubmission, gson, patientId, entry, hmlRootId, hmlExtensionId);
        bundle.addProperty(RESOURCE_TYPE_KEY, RESOURCE_TYPE_VALUE);
        bundle.addProperty(BUNDLE_TYPE_KEY, BUNDLE_TYPE_VALUE);
        bundle.add(ENTRY, entry);

        return bundle;
    }

    private void handleBundle(BundleSubmission bundle, Gson gson, String patientId, JsonArray entry, String hmlRootId, String hmlExtensionId) {
        for (Map.Entry<String, String> specimen : bundle.getSpecimens().entrySet()) {
            String specimenId = specimen.getKey();
            Map<String, BundleReference> specimenReferences = new HashMap<>();
            Map<String, BundleReference> diagnosticReportReferences = new HashMap<>();
            Map<String, BundleReference> observationReferences = new HashMap<>();
            Map<String, BundleReference> sequenceReferences = new HashMap<>();
            Map<String, BundleReference> procedureRequestReferences = new HashMap<>();
            Map<String, BundleReference> deviceReferences = new HashMap<>();
            Map<String, BundleReference> provenanceReferences = new HashMap<>();
            String deviceId = generateId();
            String provenanceId = generateId();

            specimenReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            diagnosticReportReferences.put(SPECIMEN_KEY, new BundleReference(specimenId));
            sequenceReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            sequenceReferences.put(SPECIMEN_KEY, new BundleReference(specimenId));
            observationReferences.put(SUBJECT_KEY, new BundleReference(patientId));
            procedureRequestReferences.put(SUBJECT_KEY, new BundleReference(specimenId));
            deviceReferences.put(SUBJECT_KEY, new BundleReference(specimenId));
            provenanceReferences.put(SUBJECT_KEY, new BundleReference(specimenId));

            entry.add(createJsonObject(bundle.getPatient(), gson, PATIENT_RESOURCE, patientId, new HashMap<>()));
            entry.add(createJsonObject(specimen.getValue(), gson, SPECIMEN_RESOURCE, specimenId, specimenReferences));
            String diagnosticReportId = generateId();
            String diagnosticReport = bundle.getDiangosticReports().getOrDefault(specimenId, null);
            List<String> observations = bundle.getObservations().getOrDefault(specimenId, new ArrayList<>());
            List<String> procedureRequests = bundle.getProcedureRequests().getOrDefault(specimenId, new ArrayList<>());
            String device = bundle.getDevices().getOrDefault(specimenId, "");
            String provenance = bundle.getProvenances().getOrDefault(specimenId, "");
            List<String> sequences = bundle.getSequences().getOrDefault(specimenId, new ArrayList<>());
            Map<String, JsonObject> observationResults = new HashMap<>();
            Map<String, JsonObject> sequenceResults = new HashMap<>();
            Map<String, JsonObject> procedureRequestResults = new HashMap<>();

            for (String sequence : sequences) {
                loopRelatedResources(entry, SEQUENCE_RESOURCE, sequenceReferences, sequence, gson, sequenceResults);
            }

            String sequenceObservation = handleSequenceObservation(sequenceResults, patientId, gson);
            String sequenceObservationId = loopRelatedResources(entry, OBSERVATION_RESOURCE, observationReferences, sequenceObservation, gson, null);

            for (String observation : observations) {
                String hla = getHla(observation, gson);
                addResource(entry, OBSERVATION_RESOURCE, observationReferences, observation, gson, observationResults, sequenceObservationId);
                loopGlStringObservations(entry, OBSERVATION_RESOURCE, sequenceReferences, specimenId, hla, gson, observationResults, sequenceObservationId);
            }

            for (String procedureRequest : procedureRequests) {
                addResource(entry, PROCEDURE_REQUEST_RESOURCE, procedureRequestReferences, procedureRequest, gson, procedureRequestResults, specimenId);;
            }

            diagnosticReport = handleDiagnosticReport(observationResults, diagnosticReport, gson);
            entry.add(createJsonObject(diagnosticReport, gson, DIAGNOSTIC_REPORT_RESOURCE, diagnosticReportId, diagnosticReportReferences));
            entry.add(createJsonObject(device, gson, DEVICE_RESOURCE, deviceId, deviceReferences));
            entry.add(createJsonObject(provenance, gson, PROVENANCE_RESOURCE, provenanceId, provenanceReferences));
        }
    }

    private void addResource(JsonArray entry, String resourceType, Map<String, BundleReference> references, String data, Gson gson, Map<String, JsonObject> idMap, String refId) {
        String id = generateId();
        JsonObject json = createJsonObject(data, gson, resourceType, id, references);
        JsonObject related = new JsonObject();
        JsonObject target = new JsonObject();

        related.addProperty(TYPE_KEY, DERIVED_FROM_VALUE);
        target.addProperty(REFERENCE_KEY, refId);

        related.add(TARGET_KEY, target);
        json.add(RELATED_KEY, related);
        entry.add(json);
        idMap.put(id, json);
    }

    private void loopGlStringObservations(JsonArray entry, String resourceType, Map<String, BundleReference> references, String specimenId, String hlaType,
                                          Gson gson, Map<String, JsonObject> idMap, String glStringObservationId) {
        String id = generateId();
        String data = handleGlStringObservations(hlaType, specimenId, gson);
        JsonObject json = createJsonObject(data, gson, resourceType, id, references);
        JsonObject related = new JsonObject();
        JsonObject target = new JsonObject();

        related.addProperty(TYPE_KEY, DERIVED_FROM_VALUE);
        target.addProperty(REFERENCE_KEY, glStringObservationId);

        related.add(TARGET_KEY, target);
        json.add(RELATED_KEY, related);
        entry.add(json);
        idMap.put(id, json);
    }

    private String loopRelatedResources(JsonArray entry, String resourceType, Map<String, BundleReference> references, String data, Gson gson, Map<String, JsonObject> idMap) {
        String id = generateId();
        JsonObject json = createJsonObject(data, gson, resourceType, id, references);
        entry.add(json);

        if (idMap != null) {
            idMap.put(id, json);
        }

        return id;
    }

    private String generateId() {
        return String.format("%s%s", GUID_PREFIX, UUID.randomUUID().toString());
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
        coding.addProperty(TYPE_KEY, CODE_VALUE);
        coding.addProperty(SYSTEM_KEY, SYSTEM_VALUE);
        code.add(CODING_KEY, coding);
        observation.addProperty(RESOURCE_TYPE_KEY, OBSERVATION_RESOURCE);
        observation.addProperty(STATUS_KEY, STATUS_VALUE);
        observation.add(RELATED_KEY, sequenceRefs);
        observation.add(CODE_KEY, code);

        return gson.toJson(observation);
    }

    private String getInstant() {
        return Instant.now().toString();
    }

    private String handleDevice(Gson gson) {
        JsonObject device = new JsonObject();
        JsonObject deviceText = new JsonObject();
        JsonObject deviceStatus = new JsonObject();
        JsonObject model = new JsonObject();
        JsonObject version = new JsonObject();
        JsonObject note = new JsonObject();
        JsonObject noteText = new JsonObject();

        deviceStatus.addProperty(VALUE_KEY, "generated");
        noteText.addProperty(VALUE_KEY, "converts HLA and KIR genotype report from Histoimmunogenetics Markup Language (HML) to FHIR bundle");
        note.add(TEXT_KEY, noteText);
        version.addProperty(VALUE_KEY, "1.0");
        model.addProperty(VALUE_KEY, "HML2FHIR");
        deviceText.add(STATUS_KEY, deviceStatus);

        device.addProperty(RESOURCE_TYPE_KEY, DEVICE_RESOURCE);
        device.add(TEXT_KEY, deviceText);
        device.add(MODEL_KEY, model);
        device.add(VERSION_KEY, version);
        device.add(NOTE_KEY, note);
        device.addProperty(RESOURCE_TYPE_KEY, DEVICE_RESOURCE);

        return gson.toJson(device);
    }

    private String handleProvenance(JsonArray resources, Gson gson, String hmlRootId, String hmlExtensionId) {
        JsonObject provenance = new JsonObject();
        String referenceId = generateId();
        JsonObject text = new JsonObject();
        JsonObject status = new JsonObject();
        JsonArray target = new JsonArray();
        JsonObject recorded = new JsonObject();
        JsonObject agent = new JsonObject();
        JsonObject whoReference = new JsonObject();
        JsonObject whoReferenceReference = new JsonObject();
        JsonObject whoReferenceDisplay = new JsonObject();
        JsonObject entity = new JsonObject();
        JsonObject entityRole = new JsonObject();
        JsonObject whatIdentifier = new JsonObject();
        JsonObject whatIdentifierExtension = new JsonObject();
        JsonObject extensionValueString = new JsonObject();
        JsonObject whatIdentifierValue = new JsonObject();
        Iterator iterator = resources.iterator();

        entityRole.addProperty(VALUE_KEY, "derivation");
        entity.add(ROLE_KEY, entityRole);
        extensionValueString.addProperty(URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        extensionValueString.addProperty(VALUE_KEY, String.format("hmlid root=%s, extension=%s", hmlRootId, hmlExtensionId));
        whatIdentifierExtension.add(VALUE_STRING_KEY, extensionValueString);
        whatIdentifier.add(EXTENSION_KEY, whatIdentifierExtension);
        whatIdentifierValue.addProperty(VALUE_KEY, String.format("%s^%s", hmlRootId, hmlExtensionId));
        whatIdentifier.add(VALUE_KEY, whatIdentifierValue);
        whoReferenceDisplay.addProperty(VALUE_KEY, "HML2FHIR");
        whoReferenceReference.addProperty(VALUE_KEY, referenceId);
        whoReference.add(REFERENCE_KEY, whoReferenceReference);
        whoReference.add(DISPLAY_KEY, whoReferenceDisplay);
        entity.add(WHAT_IDENTIFIER_KEY, whatIdentifier);
        agent.add(WHO_REFERENCE_KEY, whoReference);
        status.addProperty(VALUE_KEY, "generated");
        text.add(STATUS_KEY, status);
        recorded.addProperty(VALUE_KEY, getInstant());
        provenance.add(TEXT_KEY, text);
        provenance.add(RECORDED_KEY, recorded);
        provenance.add(AGENT_KEY, agent);
        provenance.addProperty(RESOURCE_TYPE_KEY, PROVENANCE_RESOURCE);

        while (iterator.hasNext()) {
            JsonObject resource = (JsonObject) iterator.next();
            JsonObject targetObj = new JsonObject();
            JsonObject reference = new JsonObject();
            JsonObject display = new JsonObject();

            reference.addProperty(VALUE_KEY, getResourceId(resource));
            display.addProperty(VALUE_KEY, getResourceType(resource));
            targetObj.add(REFERENCE_KEY, reference);
            targetObj.add(DISPLAY_KEY, display);
            target.add(targetObj);
        }

        provenance.add(TARGET_KEY, target);
        provenance.addProperty(RESOURCE_TYPE_KEY, PROVENANCE_RESOURCE);

        return gson.toJson(provenance);
    }

    private String getResourceId(JsonObject resource) {
        return resource.get(FULL_URL).getAsString();
    }

    private String getResourceType(JsonObject resource) {
        String resourceType = "";

        try {
            JsonObject resourceObj = resource.get(RESOURCE).getAsJsonObject();
            resourceType = resourceObj.get(RESOURCE_TYPE_KEY).getAsString();
        } catch (Exception ex) {
            LOG.error(String.format("Not a resource %s", resource.toString()), ex);
        }

        return resourceType;
    }

    private String handleGlStringObservations(String hlaType, String id, Gson gson) {
        JsonObject observation = new JsonObject();
        JsonArray sequenceRefs = new JsonArray();
        JsonObject code = new JsonObject();
        JsonObject coding = new JsonObject();
        JsonObject subject = new JsonObject();
        JsonArray component = new JsonArray();
        JsonObject componentObj = new JsonObject();
        JsonObject componentCode = new JsonObject();
        JsonArray componentCoding = new JsonArray();
        JsonObject componentCodingObj = new JsonObject();
        JsonObject valueCodeableConcept = new JsonObject();
        JsonArray valueCodeableConceptCoding = new JsonArray();
        JsonObject valueCodeableConceptCodingObj = getValueableCodeableConceptCodingObj(hlaType);
        JsonObject text = new JsonObject();
        JsonObject textStatus = new JsonObject();

        textStatus.addProperty(VALUE_KEY, "generated");
        text.add(STATUS_KEY, textStatus);

        valueCodeableConceptCoding.add(valueCodeableConceptCodingObj);
        valueCodeableConcept.add(CODING_KEY, valueCodeableConceptCoding);

        componentCodingObj.addProperty(SYSTEM_KEY, "http://loinc.org");
        componentCodingObj.addProperty(CODE_KEY, "48018-6");
        componentCodingObj.addProperty(DISPLAY_KEY, "Gene Studied");
        componentCoding.add(componentCodingObj);
        componentCode.add(CODING_KEY, componentCoding);
        componentObj.add(CODE_KEY, componentCode);
        componentObj.add(VALUE_CODEABLE_CONCEPT_KEY, valueCodeableConcept);
        component.add(componentObj);

        subject.addProperty(REFERENCE_KEY, id);
        coding.addProperty(TYPE_KEY, CODE_VALUE);
        coding.addProperty(SYSTEM_KEY, SYSTEM_VALUE);
        code.add(CODING_KEY, coding);
        observation.addProperty(RESOURCE_TYPE_KEY, OBSERVATION_RESOURCE);
        observation.addProperty(STATUS_KEY, STATUS_VALUE);
        observation.add(COMPONENT_KEY, component);
        observation.add(TEXT_KEY, text);
        observation.add(RELATED_KEY, sequenceRefs);
        observation.add(CODE_KEY, code);

        return gson.toJson(observation);
    }

    private String getHla(String input, Gson gson) {
        JsonObject json = gson.fromJson(input, JsonObject.class);
        String value = json.get(VALUE_STRING_KEY).getAsString();

        if (value.startsWith("HLA-A")) {
            return "HLA-A";
        } else if (value.startsWith("HLA-B")) {
            return "HLA-B";
        } else if (value.startsWith("HLA-C")) {
            return "HLA-C";
        } else if (value.startsWith("HLA-DR")) {
            return "HLA-DR";
        } else if (value.startsWith("HLA-DQ")) {
            return "HLA-DQ";
        } else {
            return "";
        }
    }

    private String getProcedureRequestResource(String hlaType, String supplierId, String centerCode, String sampleId, Gson gson) {
        JsonObject procedureRequest = new JsonObject();
        JsonObject text = new JsonObject();
        JsonObject identifier = new JsonObject();
        JsonObject identifierExtension = new JsonObject();
        JsonObject procedureRequestStatus = new JsonObject();
        JsonObject procedureRequestIntent = new JsonObject();
        JsonObject procedureReqeustCode = new JsonObject();
        JsonArray procedureRequestCoding = new JsonArray();
        JsonObject procedureRequestCodingObj = new JsonObject();
        JsonObject system = new JsonObject();
        JsonObject code = new JsonObject();
        JsonObject display = new JsonObject();
        JsonObject subject = new JsonObject();
        JsonObject subjectIdentifier = new JsonObject();
        JsonObject subjectIdentifierExtension = new JsonObject();
        JsonObject subjectIdentifierExtensionValue = new JsonObject();
        JsonObject subjectIdentifierExtensionValueString = new JsonObject();

        text.addProperty(STATUS_KEY, "generated");

        identifierExtension.addProperty(URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        identifierExtension.addProperty(VALUE_STRING_KEY, String.format("property name=SupplierOrderLineID value=%s", supplierId));
        identifier.add(EXTENSION_KEY, identifierExtension);
        identifier.addProperty(VALUE_KEY, supplierId);
        procedureRequestStatus.addProperty(VALUE_KEY, "status");
        procedureRequestIntent.addProperty(VALUE_KEY, "order");

        subjectIdentifierExtensionValue.addProperty(VALUE_KEY, String.format("sample id=%s center-code=%s", sampleId, centerCode));
        subjectIdentifierExtensionValueString.add(VALUE_STRING_KEY, subjectIdentifierExtensionValue);
        subjectIdentifierExtension.addProperty(VALUE_KEY, String.format("%s^%s", centerCode, sampleId));
        subjectIdentifier.add(EXTENSION_KEY, subjectIdentifierExtension);
        subject.add(IDENTIFIER_KEY, subjectIdentifier);
        subject.addProperty(DISPLAY_KEY,String.format("sample id=%s center-code=%s", sampleId, centerCode));

        system.addProperty(VALUE_KEY, "http://loinc.org");
        code.addProperty(VALUE_KEY, getDisplayValue(hlaType));
        display.addProperty(VALUE_KEY, getDisplayValue(hlaType));
        procedureRequestCodingObj.add(CODE_KEY, code);
        procedureRequestCodingObj.add(DISPLAY_KEY, display);
        procedureRequestCodingObj.add(SYSTEM_KEY, system);
        procedureRequestCoding.add(procedureRequestCodingObj);
        procedureReqeustCode.add(CODE_KEY, procedureRequestCoding);

        procedureRequest.add(SUBJECT_KEY, subject);
        procedureRequest.add(IDENTIFIER_KEY, identifier);
        procedureRequest.add(STATUS_KEY, procedureRequestStatus);
        procedureRequest.add(INTENT_KEY, procedureRequestIntent);
        procedureRequest.add(CODE_KEY, procedureReqeustCode);
        procedureRequest.add(TEXT_KEY, text);
        procedureRequest.addProperty(RESOURCE_TYPE_KEY, PROCEDURE_REQUEST_RESOURCE);

        return gson.toJson(procedureRequest);
    }

    private String getDisplayValue(String hla) {
        switch (hla) {
            case "HLA-A":
                return "HLA-A [Type] by High resolution";
            case "HLA-B":
                return "HLA-B [Type] by High resolution";
            case "HLA-C":
                return "HLA-C [Type] by High resolution";
        }

        return "";
    }

    private String getDisplayCode(String hla) {
        switch (hla) {
            case "HLA-A":
                return "57290-9";
            case "HLA-B":
                return "57290-8";
            case "HLA-C":
                return "57290-7";
        }

        return "";
    }

    private JsonObject getValueableCodeableConceptCodingObj(String value) {
        JsonObject obj = new JsonObject();
        String display = "";
        String displayCode = "";

        switch (value) {
            case "HLA-A":
                display = "HLA-A";
                displayCode = "HGNC:4931";
                break;
            case "HLA-B":
                display = "HLA-B";
                displayCode = "HGNC:4932";
                break;
            case "HLA-C":
                display = "HLA-C";
                displayCode = "HGNC:4933";
                break;
            case "HLA-DR":
                display = "HLA-DR";
                displayCode = "HGNC:4934";
                break;
            case "HLA-DQ":
                display = "HLA-DQ";
                displayCode = "HGNC:4935";
                break;
        }

        obj.addProperty(SYSTEM_KEY, "http://loinc.com");
        obj.addProperty(CODE_KEY, displayCode);
        obj.addProperty(DISPLAY_KEY, display);

        return obj;
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

        if (resource.has(VALUE_STRING_KEY)) {
            obs.addProperty(DISPLAY_KEY, resource.get(VALUE_STRING_KEY).getAsString());
        } else {
            JsonObject text = resource.get(TEXT_KEY).getAsJsonObject();
            JsonObject status = text.get(STATUS_KEY).getAsJsonObject();
            String value = status.get(VALUE_KEY).getAsString();
            obs.addProperty(DISPLAY_KEY, value);
        }

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

    private JsonObject createJsonObject(String str, Gson gson, String id) {
        JsonObject json = new JsonObject();
        JsonObject request = new JsonObject();

        if (str == null) {
            return json;
        }

        JsonObject incoming = gson.fromJson(str, JsonObject.class);
        JsonObject resourceObj = new JsonObject();
        json.add(RESOURCE, incoming);
        json.addProperty(FULL_URL, id);
        json.add(RESOURCE, resourceObj);
        request.addProperty(REQUEST_METHOD_KEY, REQUEST_METOHD_VALUE);
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
