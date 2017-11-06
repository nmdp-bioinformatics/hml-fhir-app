package org.nmdp.fhirsubmission.util;

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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.nmdp.fhirsubmission.exceptions.FhirBundleSubmissionFailException;
import org.nmdp.fhirsubmission.http.StatusCode;
import org.nmdp.fhirsubmission.object.FhirSubmissionResponse;

import java.util.Arrays;

public class HttpResponseExtractor {

    private static final String LOCATION_HEADER = "Location";

    public static FhirSubmissionResponse parse(HttpResponse httpResponse) throws FhirBundleSubmissionFailException {
        if (httpResponse.getStatusLine().getStatusCode() != StatusCode.CREATED) {
            throw new FhirBundleSubmissionFailException("Non-201 response");
        }

        Header header = Arrays.asList(httpResponse.getHeaders(LOCATION_HEADER)).get(0);
        String url = header.getValue();

        return new FhirSubmissionResponse(null, url);
    }
}
