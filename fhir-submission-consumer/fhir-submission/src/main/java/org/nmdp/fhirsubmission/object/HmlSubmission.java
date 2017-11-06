package org.nmdp.fhirsubmission.object;


/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 10/2/17.
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


import java.util.HashMap;
import java.util.Map;

public class HmlSubmission {

    private String patientId;
    private FhirSubmissionResponse patientResource;
    private Map<String, FhirSubmissionResponse> specimens;
    private Map<String, FhirSubmissionResponse> diagnosticReports;
    private Map<String, FhirSubmissionResponse> observations;

    public HmlSubmission() {
        this.specimens = new HashMap<>();
        this.diagnosticReports = new HashMap<>();
        this.observations = new HashMap<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Map<String, FhirSubmissionResponse> getSpecimens() {
        return specimens;
    }

    public void setSpecimens(Map<String, FhirSubmissionResponse> specimens) {
        this.specimens = specimens;
    }

    public void addSpecimen(String id, FhirSubmissionResponse result) {
        this.specimens.put(id, result);
    }

    public FhirSubmissionResponse getPatientResource() {
        return patientResource;
    }

    public void setPatientResource(FhirSubmissionResponse patientResource) {
        this.patientResource = patientResource;
    }

    public Map<String, FhirSubmissionResponse> getDiagnosticReports() {
        return diagnosticReports;
    }

    public void setDiagnosticReports(Map<String, FhirSubmissionResponse> diagnosticReports) {
        this.diagnosticReports = diagnosticReports;
    }

    public void addDiagnosticReport(String id, FhirSubmissionResponse result) {
        this.diagnosticReports.put(id, result);
    }

    public Map<String, FhirSubmissionResponse> getObservations() {
        return observations;
    }

    public void setObservations(Map<String, FhirSubmissionResponse> observations) {
        this.observations = observations;
    }

    public void addObservation(String id, FhirSubmissionResponse result) {
        this.observations.put(id, result);
    }
}
