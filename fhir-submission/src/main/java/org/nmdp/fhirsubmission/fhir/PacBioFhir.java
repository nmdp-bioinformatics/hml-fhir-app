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

    private String getId(JsonObject resource) {
        String id = resource.get(PROPERTY_NAMES.ID_KEY).getAsString();

        return id;
    }

    private JsonObject createRequest(String resource) {
        JsonObject request = new JsonObject();

        request.addProperty(PROPERTY_NAMES.METHOD_KEY, "POST");
        request.addProperty(PROPERTY_NAMES.URL_KEY, resource);

        return request;
    }

    private JsonObject createResource(JsonObject json) {
        JsonObject resource = new JsonObject();
        String id = getId(json);
        String resourceType = json.get(PROPERTY_NAMES.RESOURCE_TYPE_KEY).getAsString();

        resource.add(PROPERTY_NAMES.RESOURCE_KEY, json);
        resource.add(PROPERTY_NAMES.REQUEST_KEY, createRequest(resourceType));
        resource.addProperty(PROPERTY_NAMES.FULL_URL, id);
        resource.remove(PROPERTY_NAMES.ID_KEY);

        return resource;
    }
}
