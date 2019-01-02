package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class CenterCodeOrganizationTransform extends OrganizationTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getCenterCodeOrganization(String centerCode) {
        JsonObject organization = new JsonObject();

        organization.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Organization");
        organization.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        organization.add(PROPERTY_NAMES.TEXT_KEY, getDrText(centerCode));
        organization.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifier(centerCode));
        organization.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return organization;
    }

    private JsonObject getDrText(String centerCode) {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, String.format("sample center-code %s", centerCode));

        return text;
    }
}