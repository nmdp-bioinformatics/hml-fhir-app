package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.Instant;

/**
 * hml-fhir-app
 */

public class ProcedureRequestObservationTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getObservation(String fullGlString, String procedureRequestId, String hla, String centerCode,
        String organizationId, String specimenId, String obsIds, String sampleId) {
        JsonObject observation = new JsonObject();
        String[] glStrings = fullGlString.split("\\+");
        String[] observationIds = obsIds.split(",");
        String glString1 = glStrings[0];
        String glString2 = glStrings[1];
        String obs1Id = observationIds[0];
        String obs2Id = observationIds.length >= 2 ? observationIds[1] : null;

        observation.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Observation");
//        observation.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        observation.add(PROPERTY_NAMES.TEXT_KEY, getText(fullGlString));
        observation.add(PROPERTY_NAMES.BASED_ON_KEY, getBasedOn(procedureRequestId, hla, centerCode, sampleId));
        observation.addProperty(PROPERTY_NAMES.STATUS_KEY, "final");
        observation.add(PROPERTY_NAMES.CATEGORY_KEY, getCategory());
        observation.add(PROPERTY_NAMES.CODE_KEY, getCode(hla));
        observation.add(PROPERTY_NAMES.SUBJECT_KEY, getSubject(sampleId, centerCode));
        observation.addProperty(PROPERTY_NAMES.EFFECTIVE_DATE_TIME_KEY, getDateTime());
        observation.add(PROPERTY_NAMES.PERFORMER_KEY, getPerformer(organizationId));
        observation.add(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY, getValueCodeableConcept(fullGlString));
        observation.add(PROPERTY_NAMES.METHOD_KEY, getMethod(hla));
        observation.add(PROPERTY_NAMES.SPECIMEN_KEY, getSpecimen(specimenId, centerCode, sampleId));
        observation.add(PROPERTY_NAMES.RELATED_KEY, getRelated(glString1, obs1Id, glString2, obs2Id));
        observation.add(PROPERTY_NAMES.COMPONENT_KEY, getComponent(hla));
        observation.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return observation;
    }

    private JsonObject getText(String glString) {
        JsonObject text = new JsonObject();
        JsonObject _status = new JsonObject();
        JsonArray _fhirComments = new JsonArray();

        _fhirComments.add("from glstring");
        _status.add(PROPERTY_NAMES._FHIR_COMMENTS_KEY, _fhirComments);
        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.add(PROPERTY_NAMES._STATUS_KEY, _status);
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">%s</div>", glString));

        return text;
    }

    protected JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Observation");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    protected JsonArray getBasedOn(String procedureRequestId, String hla, String centerCode, String sampleId) {
        JsonArray basedOn = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonArray fhirComments = new JsonArray();

        fhirComments.add("R3");
        obj.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, fhirComments);
        obj.addProperty(PROPERTY_NAMES.REFERENCE_KEY, procedureRequestId);
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s typing for sample %s-%s", hla, centerCode, sampleId));
        basedOn.add(obj);

        return basedOn;
    }

    private JsonArray getCategory() {
        JsonArray category = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject codingObj = new JsonObject();

        codingObj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://hl7.org/fhir/observation-category");
        codingObj.addProperty(PROPERTY_NAMES.CODE_KEY, "laboratory");
        coding.add(codingObj);
        obj.add(PROPERTY_NAMES.CODING_KEY, coding);
        category.add(obj);

        return category;
    }

    private JsonObject getCode(String hla) {
        JsonObject code = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonArray fhirComments = new JsonArray();

        fhirComments.add("Loinc code for HLA genotyping");
        obj.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, fhirComments);
        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://loinc.org");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, getLoinc(hla));
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "Genotype display name");
        coding.add(obj);
        code.add(PROPERTY_NAMES.CODING_KEY, coding);

        return code;
    }

    protected JsonObject getSubject(String sampleId, String centerCode) {
        JsonObject subject = new JsonObject();
        JsonObject identifier = new JsonObject();
        JsonArray extension = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        obj.addProperty(PROPERTY_NAMES.VALUE_STRING_KEY, String.format("sample id=%s center-code=%s", sampleId, centerCode));
        extension.add(obj);
        identifier.add(PROPERTY_NAMES.EXTENSION_KEY, extension);
        identifier.addProperty(PROPERTY_NAMES.VALUE_KEY, String.format("%s^%s", centerCode, sampleId));
        subject.add(PROPERTY_NAMES.IDENTIFIER_KEY, identifier);
        subject.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("sample id=%s, center-code=%s", sampleId, centerCode));

        return subject;
    }

    protected JsonArray getPerformer(String organizationId) {
        JsonArray performer = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.REFERENCE_KEY, organizationId);
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "reporting-center ID");
        performer.add(obj);

        return performer;
    }

    private JsonObject getValueCodeableConcept(String glString) {
        JsonObject valueCodeableConcept = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "https://glstring.org");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, String.format("hla#3.31.0#%s", glString));
        coding.add(obj);
        valueCodeableConcept.add(PROPERTY_NAMES.CODING_KEY, coding);

        return valueCodeableConcept;
    }

    protected JsonObject getMethod(String hla) {
        JsonObject method = new JsonObject();

        method.addProperty(PROPERTY_NAMES.TEXT_KEY, String.format("sbt-ngs locus=%s test-id=PACBIOSequel test-id-source=Be The Match", hla));

        return method;
    }

    protected JsonObject getSpecimen(String specimenId, String centerCode, String sampleId) {
        JsonObject specimen = new JsonObject();

        specimen.addProperty(PROPERTY_NAMES.REFERENCE_KEY, specimenId);
        specimen.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("buccal swab: %s-%s", centerCode, sampleId));

        return specimen;
    }

    private JsonArray getRelated(String glString1, String seq1Id, String glString2, String seq2Id) {
        JsonArray related = new JsonArray();
        JsonObject obj1 = new JsonObject();
        JsonObject obj2 = new JsonObject();
        JsonObject target1 = new JsonObject();
        JsonObject target2 = new JsonObject();

        target1.addProperty(PROPERTY_NAMES.REFERENCE_KEY, seq1Id);
        target1.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s allele", glString1));
        obj1.addProperty(PROPERTY_NAMES.TYPE_KEY, "derived-from");
        obj1.add(PROPERTY_NAMES.TARGET_KEY, target1);
        related.add(obj1);

        if (glString2 != null && seq2Id != null) {
            target2.addProperty(PROPERTY_NAMES.REFERENCE_KEY, seq2Id);
            target2.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s allele", glString2));
            obj2.addProperty(PROPERTY_NAMES.TYPE_KEY, "derived-from");
            obj2.add(PROPERTY_NAMES.TARGET_KEY, target2);
            related.add(obj2);
        }

        return  related;
    }

    protected JsonArray getComponent(String hla) {
        JsonArray component = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject code = new JsonObject();
        JsonObject valueCodeableConcept = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject codingObj = new JsonObject();
        JsonArray valueCodeableConceptCoding = new JsonArray();
        JsonObject valueCodeableConceptCodingObj = new JsonObject();

        codingObj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://loinc.org");
        codingObj.addProperty(PROPERTY_NAMES.CODE_KEY, "48018-6");
        codingObj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "Gene Studied [ID]");
        coding.add(codingObj);
        code.add(PROPERTY_NAMES.CODING_KEY, coding);
        valueCodeableConceptCodingObj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "https://www.genenames.org");
        valueCodeableConceptCodingObj.addProperty(PROPERTY_NAMES.CODE_KEY, getHgnc(hla));
        valueCodeableConceptCodingObj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, hla);
        valueCodeableConceptCoding.add(valueCodeableConceptCodingObj);
        valueCodeableConcept.add(PROPERTY_NAMES.CODING_KEY, valueCodeableConceptCoding);
        obj.add(PROPERTY_NAMES.CODE_KEY, code);
        obj.add(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY, valueCodeableConcept);
        component.add(obj);

        return component;
    }

    protected String getDateTime() {
        return Instant.now().toString();
    }

    private String getHgnc(String hla) {
        switch (hla) {
            case "HLA-A":
                return "HGNC:4931";
            case "HLA-B":
                return "HGNC:4932";
            case "HLA-C":
                return "HGNC:4933";
            case "HLA-DR":
                return "HGNC:4934";
            case "HLA-DQ":
                return "HGNC:4935";
        }

        return null;
    }

    private String getLoinc(String hla) {
        switch (hla) {
            case "HLA-A":
                return "84413-4";
            case "HLA-B":
                return "84413-4";
            case "HLA-C":
                return "84413-4";
            case "HLA-DR":
                return "84413-7";
            case "HLA-DQ":
                return "84413-8";

        }

        return null;
    }
}
