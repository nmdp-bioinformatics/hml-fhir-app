package org.nmdp.hmlfhir;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/6/17.
 * <p>
 * hml-fhir
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

import com.google.gson.JsonObject;

import org.json.JSONObject;

import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;

public interface ConvertFhirToHml {
    Hml convert(FhirMessage fhir) throws Exception;
    Hml convert(JsonObject fhir) throws Exception;
    Hml convert(JSONObject fhir, String prefix) throws Exception;
    Hml convert(String fhir, String prefix) throws Exception;
    FhirMessage toDto(JSONObject fhir, String prefix);
    FhirMessage toDto(JsonObject fhir);
    FhirMessage toDto(String fhir, String prefix) throws Exception;
    org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(JSONObject fhir, String prefix);
    org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(JsonObject fhir);
    org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(String fhir, String prefix) throws Exception;
}
