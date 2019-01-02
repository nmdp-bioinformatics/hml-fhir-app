package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * hml-fhir-app
 */

public class PacBioFhir {

    public JsonObject specmin;
    public List<JsonObject> sequences;
    public List<JsonObject> procedureRequests;
    public List<JsonObject> sequenceObservations;
}
