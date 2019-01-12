package org.nmdp.fhirsubmission.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.nmdp.fhirsubmission.fhir.PacBioFhir;
import org.nmdp.fhirsubmission.fhir.SpecimenTransform;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Patient;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Patients;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * hml-fhir-app
 */

public class PacBioResourceBundler {

    private static final SpecimenTransform SPECIMEN_TRANSFORM = new SpecimenTransform();

    public JsonArray serialize(FhirMessage fhir) {
        Patients patients = fhir.getPatients();
        List<JsonArray> fhirPatients = patients.getPatients()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> map(patient))
                .collect(Collectors.toList());
        JsonArray flatPatients = new JsonArray();

        for (JsonArray array : fhirPatients) {
            for (JsonObject json : fromArray(array)) {
                flatPatients.add(json);
            }
        }

        return flatPatients;
    }

    private JsonArray map(Patient patient) {
        String[] hmlRootIds = patient.getIdentifier().getValue().split("_");
        JsonArray bundles = new JsonArray();

        List<PacBioFhir> specimens = patient.getSpecimens()
                .getSpecimens()
                .stream()
                .filter(Objects::nonNull)
                .map(specimen -> map(specimen, hmlRootIds[0], hmlRootIds[1]))
                .collect(Collectors.toList());

        specimens.stream()
                .forEach(spec -> bundles.add(spec.bundle()));

        return bundles;
    }

    private List<JsonObject> fromArray(JsonArray array) {
        Iterator iterator = array.iterator();
        List<JsonObject> json = new ArrayList<>();

        while(iterator.hasNext()) {
            JsonObject jsonObject = (JsonObject) iterator.next();
            json.add(jsonObject);
        }

        return json;
    }

    private PacBioFhir map(Specimen specimen, String rootId, String supplierId) {
        return SPECIMEN_TRANSFORM.transform(specimen, rootId, supplierId);
    }
}
