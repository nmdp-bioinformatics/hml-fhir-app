package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class ProcedureRequestTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getProcedureRequest(String sampleId, String centerCode, String hla, String supplierOrderLineId) {
        JsonObject procedureRequest = new JsonObject();

        procedureRequest.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "ProcedureRequest");
//        procedureRequest.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        procedureRequest.add(PROPERTY_NAMES.TEXT_KEY, getText(hla, centerCode, sampleId));
        procedureRequest.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifiers(supplierOrderLineId));
        procedureRequest.addProperty(PROPERTY_NAMES.STATUS_KEY, "completed");
        procedureRequest.addProperty(PROPERTY_NAMES.INTENT_KEY, "order");
        procedureRequest.add(PROPERTY_NAMES.CODE_KEY, getCode(hla));
        procedureRequest.add(PROPERTY_NAMES.SUBJECT_KEY, getSubject(centerCode, sampleId));
        procedureRequest.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return procedureRequest;
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "ServiceRequest");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    private JsonObject getText(String hla, String centerCode, String sampleId) {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">%s typing for sample: %s-%s</div>", hla, centerCode, sampleId));

        return text;
    }

    private JsonArray getIdentifiers(String supplierOrderLineId) {
        JsonArray identifier = new JsonArray();

        identifier.add(getIdentifier(supplierOrderLineId));

        return identifier;
    }

    private JsonObject getIdentifier(String supplierOrderLineId) {
        JsonObject obj = new JsonObject();
        JsonArray extension = new JsonArray();
        JsonObject extensionObj = new JsonObject();

        extensionObj.addProperty(PROPERTY_NAMES.URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        extensionObj.addProperty(PROPERTY_NAMES.VALUE_STRING_KEY, String.format("property name=SupplierOrderLineID value=%s", supplierOrderLineId));
        extension.add(extensionObj);
        obj.addProperty(PROPERTY_NAMES.VALUE_KEY, supplierOrderLineId);
        obj.add(PROPERTY_NAMES.EXTENSION_KEY, extension);

        return obj;
    }

    private JsonObject getCode(String hla) {
        JsonObject code = new JsonObject();
        JsonArray coding = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://loinc.org");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, getHlaCode(hla));

        if (!hla.equals("HLA-C")) {
            obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s [Type] by High Resolution", hla));
        } else {
            obj.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("%s [Type] by High resolution typing", hla));
        }

        coding.add(obj);
        code.add(PROPERTY_NAMES.CODING_KEY, coding);

        return code;
    }

    private String getHlaCode(String hla) {
        switch (hla) {
            case "HLA-A":
                return "57290-9";
            case "HLA-B":
                return "57291-7";
            case "HLA-C":
                return "77636-9";
        }

        return "";
    }

    private JsonObject getSubject(String centerCode, String sampleId) {
        JsonObject subject = new JsonObject();

        subject.add(PROPERTY_NAMES.IDENTIFIER_KEY, getSubjectIdentifier(centerCode, sampleId));
        subject.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("sample id=%s center-code=%s", sampleId, centerCode));

        return subject;
    }

    private JsonObject getSubjectIdentifier(String centerCode, String sampleId) {
        JsonObject identifier = new JsonObject();
        JsonArray extension = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        obj.addProperty(PROPERTY_NAMES.VALUE_STRING_KEY, String.format("sample id=%s center-code=%s", sampleId, centerCode));
        extension.add(obj);
        identifier.add(PROPERTY_NAMES.EXTENSION_KEY, extension);
        identifier.addProperty(PROPERTY_NAMES.VALUE_KEY, String.format("%s^%s", centerCode, sampleId));

        return identifier;
    }
}
