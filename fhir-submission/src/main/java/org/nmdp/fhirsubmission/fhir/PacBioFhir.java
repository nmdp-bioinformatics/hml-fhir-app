package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * hml-fhir-app
 */

public class PacBioFhir {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject specmin;
    public JsonObject organization;
    public JsonObject centerOrganization;
    public JsonObject diagnosticReportOrganization;
    public JsonObject device;
    public JsonObject provenance;
    public JsonObject diagnosticReport;
    public List<JsonObject> sequences;
    public List<JsonObject> procedureRequests;
    public List<JsonObject> sequenceObservations;
    public List<JsonObject> procedureRequestObservations;

    public JsonObject bundle() {
        JsonObject bundle = new JsonObject();
        JsonArray entry = new JsonArray();


        entry.add(createResource(specmin));
        entry.add(createResource(organization));
        entry.add(createResource(centerOrganization));
        entry.add(createResource(diagnosticReportOrganization));
        entry.add(createResource(device));
        entry.add(createResource(provenance));
        entry.add(createResource(diagnosticReport));

        sequences.stream()
                .forEach(seq -> entry.add(createResource(seq)));
        procedureRequests.stream()
                .forEach(seq -> entry.add(createResource(seq)));
        procedureRequestObservations.stream()
                .forEach(seq -> entry.add(createResource(seq)));
        sequenceObservations.stream()
                .forEach(seq -> entry.add(createResource(seq)));

        bundle.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Bundle");
        bundle.addProperty(PROPERTY_NAMES.TYPE_KEY, "transaction");
        bundle.add(PROPERTY_NAMES.ENTRY, entry);

        return bundle;
    }

    private JsonObject createResource(JsonObject json) {
        JsonObject resource = new JsonObject();

        resource.add(PROPERTY_NAMES.RESOURCE_KEY, json);

        return resource;
    }
}
