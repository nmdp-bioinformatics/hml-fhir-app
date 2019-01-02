package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.Instant;

/**
 * hml-fhir-app
 */

public class ProvenanceTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getProvenance(PacBioFhir pacBioFhir, String rootId, String extensionId) {
        JsonObject provenance = new JsonObject();

        provenance.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Provenance");
        provenance.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        provenance.add(PROPERTY_NAMES.TARGET_KEY, getTargets(pacBioFhir));
        provenance.addProperty(PROPERTY_NAMES.RECORDED_KEY, getDateTime());;
        provenance.add(PROPERTY_NAMES.AGENT_KEY, getAgent(pacBioFhir.device));
        provenance.add(PROPERTY_NAMES.ENTITY_KEY, getEntity(rootId, extensionId));
        provenance.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return  provenance;
    }

    private JsonArray getTargets(PacBioFhir pacBioFhir) {
        JsonArray targets = new JsonArray();

        targets.add(createTarget(getResourceId(pacBioFhir.device), "Device for HML2FHIR"));
        targets.add(createTarget(getResourceId(pacBioFhir.specmin), "Specimen"));
        targets.add(createTarget(getResourceId(pacBioFhir.diagnosticReportOrganization), "Organization for reporting-center-id"));
        targets.add(createTarget(getResourceId(pacBioFhir.centerOrganization), "Organization for sample-center-code"));
        targets.add(createTarget(getResourceId(pacBioFhir.organization), "Organization for hmlid.root"));
        pacBioFhir.procedureRequests.stream()
                .forEach(pr -> targets.add(createTarget(getResourceId(pr), String.format("ProcedureRequest for %s genotyping", getHlaFromProcedureRequest(pr)))));
        targets.add(createTarget(getResourceId(pacBioFhir.diagnosticReport), "Diagnostic Report"));
        pacBioFhir.procedureRequestObservations.stream()
                .forEach(pr -> targets.add(createTarget(getResourceId(pr), String.format("Genotype Observation for %s", getHlaFromObservation(pr)))));
        pacBioFhir.sequenceObservations.stream()
                .forEach(seq -> targets.add(createTarget(getResourceId(seq), String.format("Allele Observation for %s", getHlaFromObservation(seq)))));
        pacBioFhir.sequences.stream()
                .forEach(seq -> targets.add(createTarget(getResourceId(seq), String.format("Sequence for %s", getGlString(seq)))));

        return targets;
    }

    private JsonArray getEntity(String rootId, String extensionId) {
        JsonArray entity = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject whatIdentifier = new JsonObject();
        JsonArray extension = new JsonArray();
        JsonObject extensionObj = new JsonObject();

        extensionObj.addProperty(PROPERTY_NAMES.URL_KEY, "http://hl7.org/fhir/StructureDefinition/rendered-value");
        extensionObj.addProperty(PROPERTY_NAMES.VALUE_STRING_KEY, String.format("hmlid root=%s extension=%s", rootId, extensionId));
        extension.add(extensionObj);
        whatIdentifier.add(PROPERTY_NAMES.EXTENSION_KEY, extension);
        whatIdentifier.addProperty(PROPERTY_NAMES.VALUE_KEY, String.format("%s^%s", rootId, extensionId));
        obj.addProperty(PROPERTY_NAMES.ROLE_KEY, "derivation");
        obj.add(PROPERTY_NAMES.WHAT_IDENTIFIER_KEY, whatIdentifier);
        entity.add(obj);

        return entity;
    }

    private JsonArray getAgent(JsonObject device) {
        JsonArray agent = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject whoReference = new JsonObject();

        whoReference.addProperty(PROPERTY_NAMES.REFERENCE_KEY, getResourceId(device));
        whoReference.addProperty(PROPERTY_NAMES.DISPLAY_KEY, "HML2FHIR");
        obj.add(PROPERTY_NAMES.WHO_REFERENCE_KEY, whoReference);
        agent.add(obj);

        return agent;
    }

    private String getGlString(JsonObject seq) {
        return seq.get(PROPERTY_NAMES.REFERENCE_SEQ_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.REFERENCE_SEQ_ID_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.TEXT_KEY).getAsString();
    }

    private String getHlaFromObservation(JsonObject resource) {
        return resource.get(PROPERTY_NAMES.COMPONENT_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.VALUE_CODEABLE_CONCEPT_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString();
    }

    private String getHlaFromProcedureRequest(JsonObject resource) {
        return resource.get(PROPERTY_NAMES.CODE_KEY).getAsJsonObject()
                .get(PROPERTY_NAMES.CODING_KEY).getAsJsonArray().get(0).getAsJsonObject()
                .get(PROPERTY_NAMES.DISPLAY_KEY).getAsString().substring(0, 5);
    }

    private String getResourceId(JsonObject resource) {
        return resource.get(PROPERTY_NAMES.ID_KEY).getAsString();
    }

    private JsonObject createTarget(String reference, String display) {
        JsonObject json = new JsonObject();

        json.addProperty(PROPERTY_NAMES.REFERENCE_KEY, reference);
        json.addProperty(PROPERTY_NAMES.DISPLAY_KEY, display);

        return json;
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Provenance");
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
