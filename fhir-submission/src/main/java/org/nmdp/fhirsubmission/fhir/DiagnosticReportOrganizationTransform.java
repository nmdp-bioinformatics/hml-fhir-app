package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonObject;

/**
 * hml-fhir-app
 */

public class DiagnosticReportOrganizationTransform extends OrganizationTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getDiagnosticReportOrganization() {
        JsonObject organiztion = new JsonObject();

        organiztion.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Organization");
//        organiztion.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        organiztion.add(PROPERTY_NAMES.TEXT_KEY, getDiagnosticReportText());
        organiztion.add(PROPERTY_NAMES.IDENTIFIER_KEY, getIdentifier("501"));
        organiztion.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return organiztion;
    }

    private JsonObject getDiagnosticReportText() {
        JsonObject text = new JsonObject();

        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
        text.addProperty(PROPERTY_NAMES.DIV_KEY, "<div xmlns=\"http://www.w3.org/1999/xhtml\">reporting-center-id 501</div>");

        return text;
    }
}
