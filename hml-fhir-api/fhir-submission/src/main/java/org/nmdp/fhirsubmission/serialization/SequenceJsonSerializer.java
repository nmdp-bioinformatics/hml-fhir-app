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


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.nmdp.hmlfhirconvertermodels.domain.fhir.Sequence;

import java.lang.reflect.Type;

public class SequenceJsonSerializer implements JsonSerializer<Sequence> {

    private static final String RESOURCE_KEY = "resourceType";
    private static final String SYSTEM_KEY = "system";
    private static final String CODE_KEY = "code";
    private static final String CODING_KEY = "coding";
    private static final String DISPLAY_KEY = "display";
    private static final String OBSERVED_SEQ_KEY = "observedSeq";
    private static final String COORDINATE_SYSTEM_KEY = "coordinateSystem";
    private static final String RESOURCE_VALUE = "Sequence";
    private static final String CODE_CODING_SYSTEM_VALUE = "http://hl7.org/fhir/sequence-type";
    private static final String CODE_CODING_CODE_VALUE = "dna";
    private static final String DISPLAY_VALUE = "DNA Sequence";
    private static final String COORDINATE_SYSTEM_VALUE = "0";

    @Override
    public JsonElement serialize(Sequence src, Type typeOfSource, JsonSerializationContext context) {
        JsonObject sequence = new JsonObject();
        JsonObject code = new JsonObject();
        JsonObject coding = new JsonObject();

        sequence.addProperty(RESOURCE_KEY, RESOURCE_VALUE);
        sequence.addProperty(OBSERVED_SEQ_KEY, src.getObservedSeq());
        sequence.addProperty(COORDINATE_SYSTEM_KEY, COORDINATE_SYSTEM_VALUE);

        coding.addProperty(SYSTEM_KEY, CODE_CODING_SYSTEM_VALUE);
        coding.addProperty(CODE_KEY, CODE_CODING_CODE_VALUE);
        coding.addProperty(DISPLAY_KEY, DISPLAY_VALUE);
        code.add(CODING_KEY, coding);
        sequence.add(CODE_KEY, code);

        return sequence;
    }
}
