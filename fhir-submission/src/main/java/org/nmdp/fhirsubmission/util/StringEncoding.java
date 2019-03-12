package org.nmdp.fhirsubmission.util;

/**
 * hml-fhir-app
 */

public class StringEncoding {

    public static String encodeHtml(String str) {
        return str.replace("\"", "\\\"");
    }
}
