package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Observation;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;

import java.util.*;
import java.util.stream.Collectors;

/**
 * hml-fhir-app
 */

public class SpecimenTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();
    private static final SequenceTransform SEQUENCE_TRANSFORM = new SequenceTransform();
    private static final ProcedureRequestTransform PROCEDURE_REQUEST_TRANSFORM = new ProcedureRequestTransform();
    private static final OrganizationTransform ORGANIZATION_TRANSFORM = new OrganizationTransform();
    private static final CenterCodeOrganizationTransform CENTER_CODE_ORGANIZATION_TRANSFORM = new CenterCodeOrganizationTransform();
    private static final DiagnosticReportOrganizationTransform DIAGNOSTIC_REPORT_ORGANIZATION_TRANSFORM = new DiagnosticReportOrganizationTransform();
    private static final SequenceObservationTransform SEQUENCE_OBSERVATION_TRANSFORM = new SequenceObservationTransform();
    private static final ProcedureRequestObservationTransform PROCEDURE_REQUEST_OBSERVATION_TRANSFORM = new ProcedureRequestObservationTransform();
    private static final DeviceTransform DEVICE_TRANSFORM = new DeviceTransform();
    private static final DiagnosticReportTransform DIAGNOSTIC_REPORT_TRANSFORM = new DiagnosticReportTransform();
    private static final ProvenanceTransform PROVENANCE_TRANSFORM = new ProvenanceTransform();

    public PacBioFhir transform(Specimen spc, String hmlRootId, String supplierOrderId) {
        PacBioFhir fhir = new PacBioFhir();
        String sampleId = spc.getIdentifier().getValue();
        String centerCode = spc.getIdentifier().getSystem();
        Observations sequenceObservations = spc.getObservations();

        fhir.specmin = getSpecimen(centerCode, sampleId);
        fhir.organization = ORGANIZATION_TRANSFORM.getOrganization(hmlRootId);
        fhir.centerOrganization = CENTER_CODE_ORGANIZATION_TRANSFORM.getCenterCodeOrganization(centerCode);
        fhir.diagnosticReportOrganization = DIAGNOSTIC_REPORT_ORGANIZATION_TRANSFORM.getDiagnosticReportOrganization();
        fhir.device = DEVICE_TRANSFORM.getDevice();

        String specimenId = fhir.specmin.get(PROPERTY_NAMES.ID_KEY).getAsString();
        String organizationId = fhir.organization.get(PROPERTY_NAMES.ID_KEY).getAsString();

        fhir.sequences = sequenceObservations.getObservations()
                .stream()
                .filter(Objects::nonNull)
                .map(observation -> getSequencesFromObservation(observation, specimenId, centerCode, sampleId))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        fhir.procedureRequests = sequenceObservations.getObservations()
                .stream()
                .filter(Objects::nonNull)
                .map(observation ->
                        PROCEDURE_REQUEST_TRANSFORM.getProcedureRequest(sampleId, centerCode,
                                getHlaTypeFromObservation(observation), supplierOrderId))
                .collect(Collectors.toList());
        fhir.sequenceObservations = fhir.sequences
                .stream()
                .filter(Objects::nonNull)
                .map(seq -> SEQUENCE_OBSERVATION_TRANSFORM.getSequenceObservation(
                        seq,
                        sampleId,
                        centerCode,
                        specimenId,
                        organizationId,
                        getProcedureRequestIdByHla(getHla(seq), fhir.procedureRequests),
                        getHla(seq))
                )
                .collect(Collectors.toList());
        fhir.procedureRequestObservations = fhir.procedureRequests
                .stream()
                .filter(Objects::nonNull)
                .map(pr -> PROCEDURE_REQUEST_OBSERVATION_TRANSFORM.getObservation(
                    getFullGlString(sequenceObservations, getProcedureHla(pr)),
                        getProcedureRequestIdByHla(getProcedureHla(pr), fhir.procedureRequests),
                        getProcedureHla(pr),
                        centerCode,
                        organizationId,
                        specimenId,
                        getObservationIdsFromHla(fhir.sequenceObservations, getProcedureHla(pr)),
                        sampleId
                ))
                .collect(Collectors.toList());
        fhir.diagnosticReport = DIAGNOSTIC_REPORT_TRANSFORM.getDiagnosticReport(sampleId, centerCode, fhir);
        fhir.provenance = PROVENANCE_TRANSFORM.getProvenance(fhir, hmlRootId, supplierOrderId);

        return fhir;
    }

    public JsonObject getSpecimen(String centerCode, String sampleId) {
        JsonObject specimen = new JsonObject();

        specimen.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Specimen");
//        specimen.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        specimen.add(PROPERTY_NAMES.TEXT_KEY, getText(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifiers(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.SUBJECT_KEY, getSubject(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.COLLECTION_KEY, getCollection());
        specimen.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return specimen;
    }

    private String getObservationIdsFromHla(List<JsonObject> observations, String hla) {
        return observations.stream()
                .filter(Objects::nonNull)
                .filter(obs -> obs.get(PROPERTY_NAMES.COMPONENT_KEY).getAsJsonArray().get(0).getAsJsonObject()
                                    .get(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY).getAsJsonObject()
                                    .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                                    .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString().equals(hla))
                .collect(Collectors.toList())
                    .stream()
                    .map(obs -> obs.get(PROPERTY_NAMES.ID_KEY).getAsString())
                    .collect(Collectors.joining(","));
    }

    private String getFullGlString(Observations observations, String hla) {
        return observations.getObservations()
                .stream()
                .filter(Objects::nonNull)
                .filter(obs -> obs.getGlstrings().getGlstrings().stream()
                    .filter(Objects::nonNull)
                    .map(gl -> gl.getValue())
                    .collect(Collectors.toList()).get(0).startsWith(hla))
                .findFirst().get().getGlstrings().getGlstrings().get(0).getValue();
    }

    private String getProcedureHla(JsonObject procedure) {
        return procedure.get(PROPERTY_NAMES.CODE_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString().substring(0, 5);
    }

    private String getHla(JsonObject seq) {
        return seq.get(PROPERTY_NAMES.REFERENCE_SEQ_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.REFERENCE_SEQ_ID_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.TEXT_KEY).getAsString().split("\\*")[0];
    }

    private String getProcedureRequestIdByHla(String hla, List<JsonObject> procedureRequests) {
        JsonObject procedureRequest = procedureRequests
                .stream()
                .filter(Objects::nonNull)
                .filter(pr -> pr
                        .get(PROPERTY_NAMES.CODE_KEY).getAsJsonObject()
                        .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString().startsWith(hla))
                .findFirst()
                .get();

        return procedureRequest.get(PROPERTY_NAMES.ID_KEY).getAsString();
    }

    private String getHlaTypeFromObservation(Observation observation) {
        String value = getGlStrings(observation).entrySet().stream().findFirst().get().getKey();

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

    private List<JsonObject> getSequencesFromObservation(Observation observation,
        String specimenId, String centerCode, String sampleId) {
        Map<String, Integer> glStrings = getGlStrings(observation);

        return glStrings.entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(entry ->
                        SEQUENCE_TRANSFORM.getSequence(specimenId, centerCode, sampleId,
                                getDna(observation, entry.getValue()), entry.getKey()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getGlStrings(Observation observation) {
        Map<String, Integer> map = new HashMap<>();
        Integer i = 0;

        if (observation.getSequences().getSequences().size() <= 1) {
            map.put(observation.getGlstrings().getGlstrings().get(0).getValue(), i);

            return map;
        }

        for (String glString : observation.getGlstrings().getGlstrings().get(0).getValue().split("\\+")) {
            map.put(glString, i);
            i++;
        }

        return map;
    }

    private String getDna(Observation observation, Integer index) {
        try {
            return observation.getSequences()
                    .getSequences().get(index)
                    .getObservedSeq();
        } catch (Exception ex) {
            return "";
        }
    }

    private JsonArray getIdentifiers(String sampleId, String centerCode) {
        JsonArray identifier = new JsonArray();

        identifier.add(getIdentifier(sampleId, centerCode));

        return identifier;
    }

    private JsonObject getIdentifier(String sampleId, String centerCode) {
        JsonObject obj = new JsonObject();
        JsonArray extension = new JsonArray();
        JsonObject extensionObj = new JsonObject();

        extensionObj.addProperty(PROPERTY_NAMES.URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        extensionObj.addProperty(PROPERTY_NAMES.VALUE_STRING_KEY, String.format("sample id=%s center-code=%s", sampleId, centerCode));
        extension.add(extensionObj);
        obj.addProperty(PROPERTY_NAMES.VALUE_KEY, String.format("%s^%s", centerCode, sampleId));
        obj.add(PROPERTY_NAMES.EXTENSION_KEY, extension);

        return obj;
    }

    private JsonObject getSubject(String sampleId, String centerCode) {
        JsonObject subject = new JsonObject();

        subject.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifier(sampleId, centerCode));
        subject.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("<sample id=%s, center-code=%s>", centerCode, sampleId));

        return subject;
    }

    private JsonObject getCollection() {
        JsonObject collection = new JsonObject();
        JsonObject method = new JsonObject();

        method.addProperty(PROPERTY_NAMES.TEXT_KEY, "Buccal Swab");
        collection.add(PROPERTY_NAMES.METHOD_KEY, method);

        return collection;
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Specimen");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    private JsonObject getText(String sampleId, String centerCode) {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">buccal swab: %s^%s</div>", centerCode, sampleId));

        return text;
    }
}
