package org.nmdp.hmlfhirmongo.models;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/1/17.
 * <p>
 * hml-fhir-mongo
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

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

import org.nmdp.fhirsubmission.object.HmlSubmission;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;

public class FhirSubmission implements Serializable {
    private String id;
    private Boolean complete;
    private Boolean error;
    private String exception;
    private Date startTime;
    private Date endTime;
    private FhirMessage fhirMessage;
    List<HmlSubmission> submissionResult;

    public FhirSubmission() {
        submissionResult = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<HmlSubmission> getSubmissionResult() {
        return submissionResult;
    }

    public void setSubmissionResult(List<HmlSubmission> submissionResult) {
        this.submissionResult = submissionResult;
    }

    public void addSubmissionResult(List<HmlSubmission> submissionResults) {
        this.submissionResult.addAll(submissionResults);
    }

    public void addSubmissionResult(HmlSubmission submissionResult) {
        this.submissionResult.add(submissionResult);
    }

    public FhirMessage getFhirMessage() {
        return fhirMessage;
    }

    public void setFhirMessage(FhirMessage fhirMessage) {
        this.fhirMessage = fhirMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FhirSubmission)) return false;

        FhirSubmission that = (FhirSubmission) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getComplete() != null ? !getComplete().equals(that.getComplete()) : that.getComplete() != null)
            return false;
        if (getError() != null ? !getError().equals(that.getError()) : that.getError() != null) return false;
        if (getException() != null ? !getException().equals(that.getException()) : that.getException() != null)
            return false;
        if (getStartTime() != null ? !getStartTime().equals(that.getStartTime()) : that.getStartTime() != null)
            return false;
        if (getEndTime() != null ? !getEndTime().equals(that.getEndTime()) : that.getEndTime() != null) return false;
        return getSubmissionResult() != null ? getSubmissionResult().equals(that.getSubmissionResult()) : that.getSubmissionResult() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getComplete() != null ? getComplete().hashCode() : 0);
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        result = 31 * result + (getException() != null ? getException().hashCode() : 0);
        result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
        result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
        result = 31 * result + (getSubmissionResult() != null ? getSubmissionResult().hashCode() : 0);
        return result;
    }
}
