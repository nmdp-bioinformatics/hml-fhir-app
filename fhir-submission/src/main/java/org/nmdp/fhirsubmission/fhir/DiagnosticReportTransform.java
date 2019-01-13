package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.nmdp.fhirsubmission.util.StringEncoding;

import java.time.Instant;
import java.util.List;

/**
 * hml-fhir-app
 */

public class DiagnosticReportTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getDiagnosticReport(String sampleId, String centerCode, PacBioFhir fhir) {
        JsonObject diagnosticReport = new JsonObject();

        diagnosticReport.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "DiagnosticReport");
//        diagnosticReport.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        diagnosticReport.add(PROPERTY_NAMES.TEXT_KEY, getText(centerCode, sampleId));
        diagnosticReport.add(PROPERTY_NAMES.BASED_ON_KEY, getBasedOn(fhir.procedureRequests, sampleId, centerCode));
        diagnosticReport.addProperty(PROPERTY_NAMES.STATUS_KEY, "final");
        diagnosticReport.add(PROPERTY_NAMES.CATEGORY_KEY, getCategory());
        diagnosticReport.add(PROPERTY_NAMES.CODE_KEY, getCode());
        diagnosticReport.addProperty(PROPERTY_NAMES.EFFECTIVE_DATE_TIME_KEY, getDateTime());
        diagnosticReport.add(PROPERTY_NAMES.PERFORMER_KEY, getPerformer(fhir.diagnosticReportOrganization));
        diagnosticReport.add(PROPERTY_NAMES.SPECIMEN_KEY, getSpecimen(fhir.specmin, centerCode, sampleId));
        diagnosticReport.add(PROPERTY_NAMES.RESULT_KEY, getResult(fhir));
        diagnosticReport.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return diagnosticReport;
    }

    private JsonArray getResult(PacBioFhir fhir) {
        JsonArray result = new JsonArray();

        fhir.procedureRequestObservations.stream()
                .forEach(ob -> result.add(generateResultObject(ob)));

        return result;
    }

    private JsonObject generateResultObject(JsonObject json) {
        JsonObject result = new JsonObject();

        result.addProperty(PROPERTY_NAMES.REFERENCE_KEY, getResourceId(json));
        result.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s genotype: %s", getHla(json), getGlString(json)));

        return result;
    }

    private String getHla(JsonObject json) {
        return json.get(PROPERTY_NAMES.COMPONENT_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString();
    }

    private String getGlString(JsonObject json) {
        return json.get(PROPERTY_NAMES.TEXT_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.DIV_KEY).getAsString();
    }

    private JsonArray getSpecimen(JsonObject spc, String centerCode, String sampleId) {
        JsonArray specimen = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.REFERENCE_KEY, getResourceId(spc));
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("buccal swab: %s-%s", centerCode, sampleId));
        specimen.add(obj);

        return specimen;
    }

    private JsonArray getPerformer(JsonObject json) {
        JsonArray performer = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject actor = new JsonObject();

        actor.addProperty(PROPERTY_NAMES.REFERENCE_KEY, getResourceId(json));
        actor.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "reporting-center ID");
        obj.add(PROPERTY_NAMES.ACTOR_KEY, actor);
        performer.add(obj);

        return performer;
    }

    private JsonObject getCode() {
        JsonObject code = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://loinc.org");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, "81247-9");
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "Master HL7 genetic variant reporting panel");
        coding.add(obj);
        code.add(PROPERTY_NAMES.CODING_KEY, coding);

        return code;
    }

    private JsonObject getCategory() {
        JsonObject category = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://hl7.org/fhir/v2/0074");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, "GE");
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "Genetics");
        coding.add(obj);
        category.add(PROPERTY_NAMES.CODING_KEY, coding);

        return category;
    }

    private JsonObject getText(String centerCode, String sampleId) {
        JsonObject text = new JsonObject();
        JsonObject _status = new JsonObject();
        JsonArray _fhirComments = new JsonArray();

        _fhirComments.add("from reference-sequence.name");
        _status.add(PROPERTY_NAMES._FHIR_COMMENTS_KEY, _fhirComments);
        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.add(PROPERTY_NAMES._STATUS_KEY, _status);
        text.addProperty(PROPERTY_NAMES.DIV_KEY,
                StringEncoding.encodeHtml(
                        String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">HLA-A,-B,-C genotyping report for sample id=%s center-code=%s</div>",
                sampleId, centerCode)));

        return text;
    }

    private JsonArray getBasedOn(List<JsonObject> procedureReqeusts, String sampleId, String centerId) {
        JsonArray basedOn = new JsonArray();

        procedureReqeusts.stream()
                .forEach(pr -> basedOn.add(getBasedOn(pr, sampleId, centerId)));

        return basedOn;
    }

    private JsonObject getBasedOn(JsonObject json, String sampleId, String centerId) {
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.REFERENCE_KEY, getResourceId(json));
        obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s typing for sample %s-%s", getHlaFromProcedure(json), centerId, sampleId));

        return obj;
    }

    private String getHlaFromProcedure(JsonObject procedure) {
        return procedure.get(PROPERTY_NAMES.CODE_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString().substring(0, 5);
    }

    private String getResourceId(JsonObject resource) {
        return resource.get(PROPERTY_NAMES.ID_KEY).getAsString();
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "DiagnosticReport");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    private String getDateTime() {
        return Instant.now().toString();
    }
}
