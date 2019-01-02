package org.nmdp.fhirsubmission.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.nmdp.fhirsubmission.fhir.PacBioFhir;
import org.nmdp.fhirsubmission.fhir.SpecimenTransform;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Patient;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Patients;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * hml-fhir-app
 */

public class PacBioResourceBundler extends ResourceBundler {

    private static final SpecimenTransform SPECIMEN_TRANSFORM = new SpecimenTransform();

    @Override
    public JsonArray serialize(FhirMessage fhir) {
        Patients patients = fhir.getPatients();
        List<JsonObject> fhirPatients = patients.getPatients()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> map(patient))
                .collect(Collectors.toList());
        JsonArray fhirBundle = new JsonArray();

        fhirPatients.stream().forEach(patient -> fhirBundle.add(patient));

        return fhirBundle;
    }

    private JsonObject map(Patient patient) {
        List<PacBioFhir> specimens = patient.getSpecimens()
                .getSpecimens()
                .stream()
                .filter(Objects::nonNull)
                .map(specimen -> map(specimen))
                .collect(Collectors.toList());

        return new JsonObject();
    }

    private PacBioFhir map(Specimen specimen) {
        return SPECIMEN_TRANSFORM.transform(specimen);
    }
}
