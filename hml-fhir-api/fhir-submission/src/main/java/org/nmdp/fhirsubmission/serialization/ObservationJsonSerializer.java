package org.nmdp.fhirsubmission.serialization;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 9/12/17.
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

public class ObservationJsonSerializer implements JsonSerializer<Observation> {

    private static final String STATUS_KEY = "status";
    private static final String RESOURCE_KEY = "resourceType";
    private static final String SYSTEM_KEY = "system";
    private static final String CODE_KEY = "code";
    private static final String CODING_KEY = "coding";
    private static final String DISPLAY_KEY = "display";
    private static final String REFERENCE_KEY = "reference";
    private static final String VALUE_KEY = "value";
    private static final String ISSUED_KEY = "issued";
    private static final String SUBJECT_KEY = "subject";

    private static final String RESOURCE_VALUE = "Observation";
    private static final String CODE_CODING_SYSTEM_VALUE = "http://loinc.org";
    private static final String CODE_CODING_CODE_VALUE = "13303-3";
    private static final String STATUS_VALUE = "final";

    private static final String BLANK = "";
    private static final String GL_STRING_JOINING_CHARACTER = "+";

    @Override
    public JsonElement serialize(Observation src, Type typeOfSource, JsonSerializationContext context) {
        JsonObject obs = new JsonObject();
        JsonObject code = new JsonObject();
        JsonObject codeCoding = new JsonObject();
        JsonObject subject = new JsonObject();
        Glstrings glstrings = src.getGlstrings();
        List<String> glstringValues = new ArrayList<>();
        FhirSubmissionResponse response = (FhirSubmissionResponse) src.getReference();

        for (Glstring glstring : glstrings.getGlstrings()) {
            glstringValues.add(glstring.getValue());
        }

        String glsv = glstringValues.stream().collect(Collectors.joining(GL_STRING_JOINING_CHARACTER));

        obs.addProperty(RESOURCE_KEY, RESOURCE_VALUE);
        obs.addProperty(STATUS_KEY, STATUS_VALUE);
        obs.addProperty(ISSUED_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        obs.addProperty(VALUE_KEY, glsv);

        codeCoding.addProperty(SYSTEM_KEY, CODE_CODING_SYSTEM_VALUE);
        codeCoding.addProperty(CODE_KEY, CODE_CODING_CODE_VALUE);
        codeCoding.addProperty(DISPLAY_KEY, glsv);
        code.add(CODING_KEY, codeCoding);

        if (response != null) {
            subject.addProperty(REFERENCE_KEY, response.getUrl());
        }

        subject.addProperty(DISPLAY_KEY, BLANK);

        obs.add(SUBJECT_KEY, subject);
        obs.add(CODE_KEY, code);

        return obs;
    }
}
