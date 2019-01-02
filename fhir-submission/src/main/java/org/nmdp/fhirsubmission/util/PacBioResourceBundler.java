package org.nmdp.fhirsubmission.util;

import com.google.gson.JsonArray;
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
        List<JsonArray> fhirPatients = patients.getPatients()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> map(patient))
                .collect(Collectors.toList());

        return fhirPatients.get(0);
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

    private PacBioFhir map(Specimen specimen, String rootId, String supplierId) {
        return SPECIMEN_TRANSFORM.transform(specimen, rootId, supplierId);
    }
}
