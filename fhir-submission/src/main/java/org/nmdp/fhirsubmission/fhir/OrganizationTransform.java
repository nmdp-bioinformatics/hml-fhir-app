package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class OrganizationTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getOrganization(String rootId) {
        JsonObject organization = new JsonObject();

        organization.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Organization");
        organization.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        organization.add(PROPERTY_NAMES.TEXT_KEY, getText(rootId));
        organization.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifier(rootId));
        organization.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return  organization;
    }

    protected JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Organization");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }

    private JsonObject getText(String rootId) {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">hmlid root %s</div>", rootId));

        return text;
    }

    protected JsonArray getIdentifier(String id) {
        JsonArray identifier = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.VALUE_KEY, id);
        identifier.add(obj);

        return identifier;
    }
}
