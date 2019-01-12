package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class SequenceObservationTransform extends ProcedureRequestObservationTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getSequenceObservation(JsonObject sequence, String sampleId, String centerCode,
        String specimenId, String organizationId, String procedureRequestId, String hla) {
        JsonObject observation = new JsonObject();
        String glString = getGlStringFromSequence(sequence);

        observation.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Observation");
//        observation.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        observation.add(PROPERTY_NAMES.TEXT_KEY, getText(glString));
        observation.add(PROPERTY_NAMES.BASED_ON_KEY, getBasedOn(procedureRequestId, hla, centerCode, sampleId));
        observation.addProperty(PROPERTY_NAMES.STATUS_KEY, "final");
        observation.add(PROPERTY_NAMES.CODE_KEY, getCode());
        observation.add(PROPERTY_NAMES.SUBJECT_KEY, getSubject(sampleId, centerCode));
        observation.addProperty(PROPERTY_NAMES.EFFECTIVE_DATE_TIME_KEY, getDateTime());
        observation.add(PROPERTY_NAMES.PERFORMER_KEY, getPerformer(organizationId));
        observation.add(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY, getValueCodeableConcept(glString));
        observation.add(PROPERTY_NAMES.METHOD_KEY, getMethod(hla));
        observation.add(PROPERTY_NAMES.SPECIMEN_KEY, getSpecimen(specimenId, centerCode, sampleId));
        observation.add(PROPERTY_NAMES.RELATED_KEY, getRelated(glString, getSequenceId(sequence)));
        observation.add(PROPERTY_NAMES.COMPONENT_KEY, getComponent(hla));
        observation.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return observation;
    }

    private String getGlStringFromSequence(JsonObject sequence) {
        return sequence.get(PROPERTY_NAMES.REFERENCE_SEQ_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.REFERENCE_SEQ_ID_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.TEXT_KEY).getAsString();
    }

    private String getSequenceId(JsonObject sequence) {
        return sequence.get(PROPERTY_NAMES.ID_KEY).getAsString();
    }

    private JsonObject getText(String glString) {
        JsonObject text = new JsonObject();
        JsonObject _status = new JsonObject();
        JsonArray _fhirComments = new JsonArray();

        _fhirComments.add("from haplotype");
        _status.add(PROPERTY_NAMES._FHIR_COMMENTS_KEY, _fhirComments);
        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.add(PROPERTY_NAMES._STATUS_KEY, _status);
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">Observation for %s allele</div>", glString));

        return text;
    }

    private JsonObject getCode() {
        JsonObject code = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://loinc.org");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, "84414-2");
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "Haplotype name");
        coding.add(obj);
        code.add(PROPERTY_NAMES.CODING_KEY, coding);

        return code;
    }

    private JsonObject getValueCodeableConcept(String glString) {
        JsonObject valueCodeableConcept = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        /* Todo: IF NOMENCLATURE HAS A GLSTRING DELIMETER, USE GL STRING FOR SYSTEM AND VALUE IN BELOW OBJECT */

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://www.ebi.ac.uk/ipd/imgt/hla");
        obj.addProperty(PROPERTY_NAMES.VERSION_KEY, "3.31.0");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, glString);
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, glString);
        coding.add(obj);
        valueCodeableConcept.add(PROPERTY_NAMES.CODING_KEY, coding);

        return valueCodeableConcept;
    }

    private JsonArray getRelated(String glString, String sequenceId) {
        JsonArray related = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject target = new JsonObject();

        target.addProperty(PROPERTY_NAMES.REFERENCE_KEY, sequenceId);
        target.addProperty(PROPERTY_NAMES.DISPLAY_KEY, glString);
        obj.addProperty(PROPERTY_NAMES.TYPE_KEY, "derived-from");
        obj.add(PROPERTY_NAMES.TARGET_KEY, target);
        related.add(obj);

        return related;
    }
}
