package org.nmdp.hmlfhirconvertermodels.domain.fhir;

import org.apache.commons.lang3.StringUtils;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;

import java.util.Date;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/26/17.
 * <p>
 * service-hml-fhir-converter-models
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

import java.io.Serializable;


@FhirResource(parentResourceType = Patient.class)
public class Specimen implements Serializable {
    private Identifier identifier;
    private Collection collection;
    private String accessionIdentifier;
    private Enum<Status> status;
    private Date receivedTime;
    private Specimen parent;
    private Request request;
    private String note;
    private Type type;
    private Observations observations;
    private Object subject;
    private Object reference;

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getAccessionIdentifier() {
        return accessionIdentifier;
    }

    public void setAccessionIdentifier(String accessionIdentifier) {
        this.accessionIdentifier = accessionIdentifier;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(Enum<Status> status) {
        this.status = status;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Specimen getParent() {
        return parent;
    }

    public void setParent(Specimen parent) {
        this.parent = parent;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Observations getObservations() {
        return observations;
    }

    public void setObservations(Observations observations) {
        this.observations = observations;
    }

    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject) {
        this.subject = subject;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specimen)) return false;

        Specimen specimen = (Specimen) o;

        if (getIdentifier() != null ? !getIdentifier().equals(specimen.getIdentifier()) : specimen.getIdentifier() != null)
            return false;
        if (getCollection() != null ? !getCollection().equals(specimen.getCollection()) : specimen.getCollection() != null)
            return false;
        if (getAccessionIdentifier() != null ? !getAccessionIdentifier().equals(specimen.getAccessionIdentifier()) : specimen.getAccessionIdentifier() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(specimen.getStatus()) : specimen.getStatus() != null)
            return false;
        if (getReceivedTime() != null ? !getReceivedTime().equals(specimen.getReceivedTime()) : specimen.getReceivedTime() != null)
            return false;
        if (getParent() != null ? !getParent().equals(specimen.getParent()) : specimen.getParent() != null)
            return false;
        if (getRequest() != null ? !getRequest().equals(specimen.getRequest()) : specimen.getRequest() != null)
            return false;
        if (getNote() != null ? !getNote().equals(specimen.getNote()) : specimen.getNote() != null) return false;
        if (getType() != null ? !getType().equals(specimen.getType()) : specimen.getType() != null) return false;
        if (getObservations() != null ? !getObservations().equals(specimen.getObservations()) : specimen.getObservations() != null)
            return false;
        if (getSubject() != null ? !getSubject().equals(specimen.getSubject()) : specimen.getSubject() != null)
            return false;
        return getReference() != null ? getReference().equals(specimen.getReference()) : specimen.getReference() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdentifier() != null ? getIdentifier().hashCode() : 0;
        result = 31 * result + (getCollection() != null ? getCollection().hashCode() : 0);
        result = 31 * result + (getAccessionIdentifier() != null ? getAccessionIdentifier().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getReceivedTime() != null ? getReceivedTime().hashCode() : 0);
        result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
        result = 31 * result + (getRequest() != null ? getRequest().hashCode() : 0);
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getObservations() != null ? getObservations().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getReference() != null ? getReference().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        if (getIdentifier() != null && getIdentifier().hasValue()) { return true; }
        if (getCollection() != null && getCollection().hasValue()) { return true; }
        if (!StringUtils.isBlank(getAccessionIdentifier())) { return true; }
        if (getStatus() != null) { return true; }
        if (getReceivedTime() != null) { return true; }
        if (getParent() != null && getParent().hasValue()) { return true; }
        if (getReceivedTime() != null) { return true; }
        if (!StringUtils.isBlank(getNote())) { return true; }
        if (getType() != null && getType().hasValue()) { return true; }
        if (getObservations() != null && getObservations().hasValue()) { return true; }
        if (getSubject() != null) { return true; }
        if (getReference() != null) { return true; }

        return false;
    }
}
