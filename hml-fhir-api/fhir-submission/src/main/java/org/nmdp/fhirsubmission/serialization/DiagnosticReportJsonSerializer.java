package org.nmdp.fhirsubmission.serialization;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 9/11/17.
 * <p>
 * fhir-submission
 * Copyright (c) 2012-2017 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import com.google.gson.*;

import org.nmdp.fhirsubmission.object.FhirSubmissionResponse;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.*;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Glstrings;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosticReportJsonSerializer implements JsonSerializer<Specimen> {

    private static final String STATUS_KEY = "status";
    private static final String EFFECTIVE_DATE_TIME_KEY = "effectiveDateTime";
    private static final String ISSUED_KEY = "issued";
    private static final String SYSTEM_KEY = "system";
    private static final String CODE_KEY = "code";
    private static final String DISPLAY_KEY = "display";
    private static final String CODING_KEY = "coding";
    private static final String BASED_ON_KEY = "basedOn";
    private static final String CATEGORY_KEY = "category";
    private static final String RESOURCE_KEY = "resourceType";
    private static final String RESULT_KEY = "result";
    private static final String SUBJECT_KEY = "subject";
    private static final String SPECIMEN_KEY = "specimen";
    private static final String PERFORMER_KEY = "performer";
    private static final String EXTENSION_KEY = "extension";
    private static final String XMLNS_KEY = "xmlns";
    private static final String URL_KEY = "url";
    private static final String VALUE_STRING_KEY = "valueString";
    private static final String VALUE_URI_KEY = "valueUri";
    private static final String REFERENCE_KEY = "reference";

    private static final String RESOURCE_VALUE = "DiagnosticReport";
    private static final String STATUS_VALUE = "final";
    private static final String DISPLAY_VALUE = "HLA-A+B+C (class I) [Type]";
    private static final String CODE_CODING_SYSTEM_VALUE = "http://loinc.org";
    private static final String CODE_CODING_CODE_VALUE = "13303-3";
    private static final String CATEGORY_CODE_SYSTEM = "http://hl7.org/fhir/ValueSet/diagnostic-service-sections";
    private static final String CATEGORY_CODE_VALUE = "GE";
    private static final String CATEGORY_CODE_DISPLAY = "Genetics";
    private static final String REFERENCE_VALUE = "urn:uuid:9243cc20-27bd-4f87-ba90-0328ed474950";
    private static final String REFERENCE_DISPLAY_VALUE = "Typing Laboratory";
    private static final String EXTENSION_VALUE_VALUE = "text";
    private static final String VALUE_URI_VALUE = "https://gl.nmdp.org/imgt-hla/3.23.0/multilocus-unphased-genotype/ez";
    private static final String EXTENSION_URI_VALUE = "url";
    private static final String XMLNS_VALUE = "http://hl7.org/fhir";
    private static final String EXTENSION_URL_VALUE = "http://hl7.org/fhir/StructureDefinition/hla-genotyping-resultsGlstring";

    private static final String BLANK = "";
    private static final String GL_STRING_JOINING_CHARACTER = "+";
    private static final String GL_STRING_ALLELE_JOINING_CHARACTER = "^";

    @Override
    public JsonElement serialize(Specimen src, Type typeOfSource, JsonSerializationContext context) {
        JsonObject code = new JsonObject();
        JsonObject codeCoding = new JsonObject();
        JsonObject basedOn = new JsonObject();
        JsonObject category = new JsonObject();
        JsonObject categoryCoding = new JsonObject();
        JsonObject diagnosticReport = new JsonObject();
        JsonObject subject = new JsonObject();
        JsonObject specimen = new JsonObject();
        JsonObject performer = new JsonObject();
        JsonObject glStringExtension = new JsonObject();
        JsonArray glStringExtensions = new JsonArray();
        JsonObject glStringExtensionUri = new JsonObject();
        JsonArray glStrings = new JsonArray();
        JsonObject glStringExtensionValue = new JsonObject();
        JsonArray results = new JsonArray();
        Observations observations = src.getObservations();
        FhirSubmissionResponse response = (FhirSubmissionResponse) src.getSubject();
        FhirSubmissionResponse reference = (FhirSubmissionResponse) src.getReference();
        List<List<String>> glSegments = new ArrayList<>();

        diagnosticReport.addProperty(RESOURCE_KEY, RESOURCE_VALUE);
        diagnosticReport.addProperty(STATUS_KEY, STATUS_VALUE);
        diagnosticReport.addProperty(EFFECTIVE_DATE_TIME_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        diagnosticReport.addProperty(ISSUED_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        codeCoding.addProperty(SYSTEM_KEY, CODE_CODING_SYSTEM_VALUE);
        codeCoding.addProperty(CODE_KEY, CODE_CODING_CODE_VALUE);
        codeCoding.addProperty(DISPLAY_KEY, DISPLAY_VALUE);
        code.add(CODING_KEY, codeCoding);

        basedOn.addProperty(REFERENCE_KEY, BLANK);
        basedOn.addProperty(DISPLAY_KEY, BLANK);

        categoryCoding.addProperty(SYSTEM_KEY, CATEGORY_CODE_SYSTEM);
        categoryCoding.addProperty(CODE_KEY, CATEGORY_CODE_VALUE);
        categoryCoding.addProperty(DISPLAY_KEY, CATEGORY_CODE_DISPLAY);
        category.add(CODING_KEY, categoryCoding);

        if (response != null) {
            subject.addProperty(REFERENCE_KEY, response.getUrl());
            subject.addProperty(DISPLAY_KEY, BLANK);
        }

        if (reference != null) {
            specimen.addProperty(REFERENCE_KEY, reference.getUrl());
            specimen.addProperty(DISPLAY_KEY, BLANK);
        }

        performer.addProperty(REFERENCE_KEY, REFERENCE_VALUE);
        performer.addProperty(DISPLAY_KEY, REFERENCE_DISPLAY_VALUE);

        for (Observation observation : observations.getObservations()) {
            Glstrings glstrings = observation.getGlstrings();
            List<String> gls = new ArrayList<>();

            for (Glstring glstring : glstrings.getGlstrings()) {
                JsonObject result = new JsonObject();
                String glStringValue = glstring.getValue();
                FhirSubmissionResponse observationResponse = (FhirSubmissionResponse) observation.getValue();
                gls.add(glStringValue);

                if (observationResponse != null) {
                    result.addProperty(REFERENCE_KEY, observationResponse.getUrl());
                }

                result.addProperty(DISPLAY_KEY, glStringValue);
                results.add(result);
            }

            glSegments.add(gls);
        }

        String alleleGlstring = glSegments.stream().map(allele -> allele
            .stream().collect(Collectors.joining(GL_STRING_JOINING_CHARACTER)))
            .collect(Collectors.toList())
                .stream().collect(Collectors.joining(GL_STRING_ALLELE_JOINING_CHARACTER));

        glStringExtensionValue.addProperty(VALUE_STRING_KEY, alleleGlstring);
        glStringExtensionValue.addProperty(URL_KEY, EXTENSION_VALUE_VALUE);
        glStringExtensionUri.addProperty(VALUE_URI_KEY, VALUE_URI_VALUE);
        glStringExtensionUri.addProperty(URL_KEY, EXTENSION_URI_VALUE);
        glStrings.add(glStringExtensionUri);
        glStrings.add(glStringExtensionValue);
        glStringExtension.addProperty(XMLNS_KEY, XMLNS_VALUE);
        glStringExtension.addProperty(URL_KEY, EXTENSION_URL_VALUE);
        glStringExtension.add(EXTENSION_KEY, glStrings);
        glStringExtensions.add(glStringExtension);

        diagnosticReport.add(RESULT_KEY, results);
        diagnosticReport.add(CODE_KEY, code);
        diagnosticReport.add(CATEGORY_KEY, category);
        diagnosticReport.add(BASED_ON_KEY, basedOn);
        diagnosticReport.add(SUBJECT_KEY, subject);
        diagnosticReport.add(SPECIMEN_KEY, specimen);
        diagnosticReport.add(PERFORMER_KEY, performer);
        diagnosticReport.add(EXTENSION_KEY, glStringExtensions);

        return diagnosticReport;
    }
}
