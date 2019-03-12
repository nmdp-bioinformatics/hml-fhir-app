package org.nmdp.fhirsubmission.fhir;

import java.util.UUID;

/**
 * hml-fhir-app
 */

public class FhirGuid {

    public static String genereateUrn() {
        return String.format("urn:uuid:%s", UUID.randomUUID().toString());
    }
}
