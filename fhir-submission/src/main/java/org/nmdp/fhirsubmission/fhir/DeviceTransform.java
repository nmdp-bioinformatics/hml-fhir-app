package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class DeviceTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getDevice() {
        JsonObject device = new JsonObject();

        device.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Device");
        device.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        device.add(PROPERTY_NAMES.TEXT_KEY, getText());
        device.addProperty(PROPERTY_NAMES.MODEL_KEY, "HML2FHIR");
        device.addProperty(PROPERTY_NAMES.VERSION_KEY, "1.0");
        device.add(PROPERTY_NAMES.NOTE_KEY, getNote());
        device.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return device;
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Device");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    private JsonObject getText() {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, "<div xmlns=\"http://www.w3.org/1999/xhtml\">software device: HML2FHIR</div>");

        return text;
    }

    private JsonArray getNote() {
        JsonArray note = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.TEXT_KEY, "converts HLA and KIR genotype report from Histoimmunogenetics Markup Language (HML) to FHIR bundle");
        note.add(obj);

        return note;
    }
}
