package org.nmdp.fhirsubmission.serialization;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/29/17.
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.nmdp.fhirsubmission.object.FhirSubmissionResponse;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Identifier;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;

import java.lang.reflect.Type;

public class SpecimenJsonSerializer implements JsonSerializer<Specimen> {

    private static final String RESOURCE_TYPE_KEY = "resourceType";
    private static final String RESOURCE_TYPE = "Specimen";
    private static final String VALUE_KEY = "value";
    private static final String IDENTIFIER_KEY = "identifier";
    private static final String SUBJECT_KEY = "subject";
    private static final String REFERENCE_KEY = "reference";
    private static final String SYSTEM_KEY = "system";
    private static final String CODE_KEY = "code";
    private static final String DISPLAY_KEY = "display";
    private static final String CODING_KEY = "coding";
    private static final String TYPE_KEY = "type";

    private static final String SYSTEM_VALUE = "http://snowmed.info/sct";
    private static final String CODE_VALUE = "258564008";
    private static final String DISPLAY_VALUE = "Buccal smear sample";

    @Override
    public JsonElement serialize(Specimen src, Type typeOfSource, JsonSerializationContext context) {
        JsonObject specimen = new JsonObject();
        JsonObject identifier = new JsonObject();
        JsonObject subject = new JsonObject();
        JsonObject type = new JsonObject();
        JsonObject coding = new JsonObject();

        Identifier id = src.getIdentifier();
        FhirSubmissionResponse response = (FhirSubmissionResponse) src.getSubject();

        identifier.addProperty(VALUE_KEY, id.getValue());
//        identifier.addProperty(SYSTEM_KEY, id.getSystem());
        identifier.addProperty(SYSTEM_KEY, "http://bethematch.org/center-code/001");

        coding.addProperty(SYSTEM_KEY, SYSTEM_VALUE);
        coding.addProperty(CODE_KEY, CODE_VALUE);;
        coding.addProperty(DISPLAY_KEY, DISPLAY_VALUE);
        type.add(CODING_KEY, coding);

        if (response != null) {
            subject.addProperty(REFERENCE_KEY, response.getUrl());
        }

        specimen.addProperty(RESOURCE_TYPE_KEY, RESOURCE_TYPE);
        specimen.add(IDENTIFIER_KEY, identifier);
        specimen.add(SUBJECT_KEY, subject);
        specimen.add(TYPE_KEY, type);

        return specimen;
    }
}
