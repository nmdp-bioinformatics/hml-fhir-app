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

    public PacBioFhir transform(Specimen spc) {
        PacBioFhir fhir = new PacBioFhir();
        String sampleId = spc.getIdentifier().getValue();
        String centerCode = spc.getIdentifier().getSystem();
        Observations sequenceObservations = spc.getObservations();

        fhir.specmin = getSpecimen(centerCode, sampleId);

        String specimenId = fhir.specmin.get(PROPERTY_NAMES.ID_KEY).getAsString();

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
                                getHlaTypeFromObservation(observation), "Supplier OrderLine ID"))
                .collect(Collectors.toList());

        return fhir;
    }

    public JsonObject getSpecimen(String centerCode, String sampleId) {
        JsonObject specimen = new JsonObject();

        specimen.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Specimen");
        specimen.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        specimen.add(PROPERTY_NAMES.TEXT_KEY, getText(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifiers(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.SUBJECT_KEY, getSubject(sampleId, centerCode));
        specimen.add(PROPERTY_NAMES.COLLECTION_KEY, getCollection());
        specimen.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return specimen;
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

        for (String glString : observation.getGlstrings().getGlstrings().get(0).getValue().split("\\+")) {
            map.put(glString, i);
            i++;
        }

        return map;
    }

    private String getDna(Observation observation, Integer index) {
        return observation.getSequences()
                .getSequences().get(index)
                .getObservedSeq();
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
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("buccal swab: %s-%s", centerCode, sampleId));

        return text;
    }
}
