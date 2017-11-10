package org.nmdp.hmlfhirconvertermodels.domain.fhir;

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

import org.apache.commons.lang3.StringUtils;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;

import java.io.Serializable;


@FhirResource
public class Sequence implements Serializable {
    private Identifier identifier;
    private Code type;
    private Integer coordinateSystem;
    private Patient patient;
    private Specimen specimen;
    private Device device;
    private Organization performer;
    private Integer quantity;
    private BackboneElement referenceSeq;
    private Variant variant;
    private String observedSeq;
    private Quality quality;
    private Integer readCoverage;
    private Repository repository;
    private Sequence pointer;

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Code getType() {
        return type;
    }

    public void setType(Code type) {
        this.type = type;
    }

    public Integer getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(Integer coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Organization getPerformer() {
        return performer;
    }

    public void setPerformer(Organization performer) {
        this.performer = performer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BackboneElement getReferenceSeq() {
        return referenceSeq;
    }

    public void setReferenceSeq(BackboneElement referenceSeq) {
        this.referenceSeq = referenceSeq;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public String getObservedSeq() {
        return observedSeq;
    }

    public void setObservedSeq(String observedSeq) {
        this.observedSeq = observedSeq;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Integer getReadCoverage() {
        return readCoverage;
    }

    public void setReadCoverage(Integer readCoverage) {
        this.readCoverage = readCoverage;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Sequence getPointer() {
        return pointer;
    }

    public void setPointer(Sequence pointer) {
        this.pointer = pointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sequence)) return false;

        Sequence sequence = (Sequence) o;

        if (getIdentifier() != null ? !getIdentifier().equals(sequence.getIdentifier()) : sequence.getIdentifier() != null)
            return false;
        if (getType() != null ? !getType().equals(sequence.getType()) : sequence.getType() != null) return false;
        if (getCoordinateSystem() != null ? !getCoordinateSystem().equals(sequence.getCoordinateSystem()) : sequence.getCoordinateSystem() != null)
            return false;
        if (getPatient() != null ? !getPatient().equals(sequence.getPatient()) : sequence.getPatient() != null)
            return false;
        if (getSpecimen() != null ? !getSpecimen().equals(sequence.getSpecimen()) : sequence.getSpecimen() != null)
            return false;
        if (getDevice() != null ? !getDevice().equals(sequence.getDevice()) : sequence.getDevice() != null)
            return false;
        if (getPerformer() != null ? !getPerformer().equals(sequence.getPerformer()) : sequence.getPerformer() != null)
            return false;
        if (getQuantity() != null ? !getQuantity().equals(sequence.getQuantity()) : sequence.getQuantity() != null)
            return false;
        if (getReferenceSeq() != null ? !getReferenceSeq().equals(sequence.getReferenceSeq()) : sequence.getReferenceSeq() != null)
            return false;
        if (getVariant() != null ? !getVariant().equals(sequence.getVariant()) : sequence.getVariant() != null)
            return false;
        if (getObservedSeq() != null ? !getObservedSeq().equals(sequence.getObservedSeq()) : sequence.getObservedSeq() != null)
            return false;
        if (getQuality() != null ? !getQuality().equals(sequence.getQuality()) : sequence.getQuality() != null)
            return false;
        if (getReadCoverage() != null ? !getReadCoverage().equals(sequence.getReadCoverage()) : sequence.getReadCoverage() != null)
            return false;
        if (getRepository() != null ? !getRepository().equals(sequence.getRepository()) : sequence.getRepository() != null)
            return false;
        return getPointer() != null ? getPointer().equals(sequence.getPointer()) : sequence.getPointer() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdentifier() != null ? getIdentifier().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getCoordinateSystem() != null ? getCoordinateSystem().hashCode() : 0);
        result = 31 * result + (getPatient() != null ? getPatient().hashCode() : 0);
        result = 31 * result + (getSpecimen() != null ? getSpecimen().hashCode() : 0);
        result = 31 * result + (getDevice() != null ? getDevice().hashCode() : 0);
        result = 31 * result + (getPerformer() != null ? getPerformer().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        result = 31 * result + (getReferenceSeq() != null ? getReferenceSeq().hashCode() : 0);
        result = 31 * result + (getVariant() != null ? getVariant().hashCode() : 0);
        result = 31 * result + (getObservedSeq() != null ? getObservedSeq().hashCode() : 0);
        result = 31 * result + (getQuality() != null ? getQuality().hashCode() : 0);
        result = 31 * result + (getReadCoverage() != null ? getReadCoverage().hashCode() : 0);
        result = 31 * result + (getRepository() != null ? getRepository().hashCode() : 0);
        result = 31 * result + (getPointer() != null ? getPointer().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getIdentifier() != null && getIdentifier().hasValue()) { return true; }
        if (getType() != null && getType().hasValue()) { return true; }
        if (getCoordinateSystem() != null) { return true; }
        if (getPatient() != null && getPatient().hasValue()) { return true; }
        if (getSpecimen() != null && getSpecimen().hasValue()) { return true; }
        if (getDevice() != null && getDevice().hasValue()) { return true; }
        if (getPerformer() != null && getPerformer().hasValue()) { return true; }
        if (getQuantity() != null) { return true; }
        if (getReferenceSeq() != null && getReferenceSeq().hasValue()) { return true; }
        if (getVariant() != null && getVariant().hasValue()) { return true; }
        if (getQuality() != null && getQuality().hasValue()) { return true; }
        if (!StringUtils.isBlank(getObservedSeq())) { return true; }
        if (getReadCoverage() != null) { return true; }
        if (getRepository() != null && getRepository().hasValue()) { return true; }
        if (getPointer() != null && getPointer().hasValue()) { return true; }

        return false;
    }
}
