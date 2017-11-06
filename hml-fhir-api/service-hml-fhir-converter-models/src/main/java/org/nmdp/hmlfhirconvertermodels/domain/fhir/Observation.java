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
import org.joda.time.DateTime;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.*;

import java.io.Serializable;


@FhirResource
public class Observation implements Serializable {

    private Identifier identifier;
    private Reference basedOn;
    private Status status;
    private Category category;
    private Code code;
    private String context;
    private DateTime effective;
    private DateTime issued;
    private Practitioner performer;
    private Object value;
    private Object dataAbsentReason;
    private Object interpretation;
    private String comment;
    private Object bodySite;
    private Object method;
    private Specimen specimen;
    private Device device;
    private DiagnosticReport diagnosticReport;
    private Glstrings glstrings;
    private Haploids haploids;
    private String geneticsAlleleName;
    private String geneticsGene;
    private SbtNgss sbtNgss;
    private Sequences sequences;
    private GeneticsPhaseSet geneticsPhaseSet;
    private GenotypingResultsMethod genotypingResultsMethod;
    private GenotypingResultsHaploids genotypingResultsHaploids;
    private String subject;
    private Object reference;


    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Reference getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(Reference basedOn) {
        this.basedOn = basedOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public DateTime getEffective() {
        return effective;
    }

    public void setEffective(DateTime effective) {
        this.effective = effective;
    }

    public DateTime getIssued() {
        return issued;
    }

    public void setIssued(DateTime issued) {
        this.issued = issued;
    }

    public Practitioner getPerformer() {
        return performer;
    }

    public void setPerformer(Practitioner performer) {
        this.performer = performer;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getDataAbsentReason() {
        return dataAbsentReason;
    }

    public void setDataAbsentReason(Object dataAbsentReason) {
        this.dataAbsentReason = dataAbsentReason;
    }

    public Object getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(Object interpretation) {
        this.interpretation = interpretation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getBodySite() {
        return bodySite;
    }

    public void setBodySite(Object bodySite) {
        this.bodySite = bodySite;
    }

    public Object getMethod() {
        return method;
    }

    public void setMethod(Object method) {
        this.method = method;
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

    public Glstrings getGlstrings() {
        return glstrings;
    }

    public void setGlstrings(Glstrings glstrings) {
        this.glstrings = glstrings;
    }

    public Haploids getHaploids() {
        return haploids;
    }

    public void setHaploids(Haploids haploids) {
        this.haploids = haploids;
    }

    public String getGeneticsAlleleName() {
        return geneticsAlleleName;
    }

    public void setGeneticsAlleleName(String geneticsAlleleName) {
        this.geneticsAlleleName = geneticsAlleleName;
    }

    public String getGeneticsGene() {
        return geneticsGene;
    }

    public void setGeneticsGene(String geneticsGene) {
        this.geneticsGene = geneticsGene;
    }

    public SbtNgss getSbtNgss() {
        return sbtNgss;
    }

    public void setSbtNgss(SbtNgss sbtNgss) {
        this.sbtNgss = sbtNgss;
    }

    public Sequences getSequences() {
        return sequences;
    }

    public void setSequences(Sequences sequences) {
        this.sequences = sequences;
    }

    public GeneticsPhaseSet getGeneticsPhaseSet() {
        return geneticsPhaseSet;
    }

    public void setGeneticsPhaseSet(GeneticsPhaseSet geneticsPhaseSet) {
        this.geneticsPhaseSet = geneticsPhaseSet;
    }

    public GenotypingResultsMethod getGenotypingResultsMethod() {
        return genotypingResultsMethod;
    }

    public void setGenotypingResultsMethod(GenotypingResultsMethod genotypingResultsMethod) {
        this.genotypingResultsMethod = genotypingResultsMethod;
    }

    public GenotypingResultsHaploids getGenotypingResultsHaploids() {
        return genotypingResultsHaploids;
    }

    public void setGenotypingResultsHaploids(GenotypingResultsHaploids genotypingResultsHaploids) {
        this.genotypingResultsHaploids = genotypingResultsHaploids;
    }


    public DiagnosticReport getDiagnosticReport() {
        return diagnosticReport;

    }

    public void setDiagnosticReport(DiagnosticReport diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Observation)) return false;

        Observation that = (Observation) o;

        if (getIdentifier() != null ? !getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() != null)
            return false;
        if (getBasedOn() != null ? !getBasedOn().equals(that.getBasedOn()) : that.getBasedOn() != null) return false;
        if (getStatus() != that.getStatus()) return false;
        if (getCategory() != null ? !getCategory().equals(that.getCategory()) : that.getCategory() != null)
            return false;
        if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) return false;
        if (getSubject() != null ? !getSubject().equals(that.getSubject()) : that.getSubject() != null) return false;
        if (getContext() != null ? !getContext().equals(that.getContext()) : that.getContext() != null) return false;
        if (getEffective() != null ? !getEffective().equals(that.getEffective()) : that.getEffective() != null)
            return false;
        if (getIssued() != null ? !getIssued().equals(that.getIssued()) : that.getIssued() != null) return false;
        if (getPerformer() != null ? !getPerformer().equals(that.getPerformer()) : that.getPerformer() != null)
            return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        if (getDataAbsentReason() != null ? !getDataAbsentReason().equals(that.getDataAbsentReason()) : that.getDataAbsentReason() != null)
            return false;
        if (getInterpretation() != null ? !getInterpretation().equals(that.getInterpretation()) : that.getInterpretation() != null)
            return false;
        if (getComment() != null ? !getComment().equals(that.getComment()) : that.getComment() != null) return false;
        if (getBodySite() != null ? !getBodySite().equals(that.getBodySite()) : that.getBodySite() != null)
            return false;
        if (getMethod() != null ? !getMethod().equals(that.getMethod()) : that.getMethod() != null) return false;
        if (getSpecimen() != null ? !getSpecimen().equals(that.getSpecimen()) : that.getSpecimen() != null)
            return false;
        if (getDevice() != null ? !getDevice().equals(that.getDevice()) : that.getDevice() != null) return false;
        if (getDiagnosticReport() != null ? !getDiagnosticReport().equals(that.getDiagnosticReport()) : that.getDiagnosticReport() != null)
            return false;
        if (getGlstrings() != null ? !getGlstrings().equals(that.getGlstrings()) : that.getGlstrings() != null)
            return false;
        if (getHaploids() != null ? !getHaploids().equals(that.getHaploids()) : that.getHaploids() != null)
            return false;
        if (getGeneticsAlleleName() != null ? !getGeneticsAlleleName().equals(that.getGeneticsAlleleName()) : that.getGeneticsAlleleName() != null)
            return false;
        if (getGeneticsGene() != null ? !getGeneticsGene().equals(that.getGeneticsGene()) : that.getGeneticsGene() != null)
            return false;
        if (getSbtNgss() != null ? !getSbtNgss().equals(that.getSbtNgss()) : that.getSbtNgss() != null) return false;
        if (getSequences() != null ? !getSequences().equals(that.getSequences()) : that.getSequences() != null)
            return false;
        if (getGeneticsPhaseSet() != null ? !getGeneticsPhaseSet().equals(that.getGeneticsPhaseSet()) : that.getGeneticsPhaseSet() != null)
            return false;
        if (getGenotypingResultsMethod() != null ? !getGenotypingResultsMethod().equals(that.getGenotypingResultsMethod()) : that.getGenotypingResultsMethod() != null)
            return false;
        return getGenotypingResultsHaploids() != null ? getGenotypingResultsHaploids().equals(that.getGenotypingResultsHaploids()) : that.getGenotypingResultsHaploids() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdentifier() != null ? getIdentifier().hashCode() : 0;
        result = 31 * result + (getBasedOn() != null ? getBasedOn().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
        result = 31 * result + (getEffective() != null ? getEffective().hashCode() : 0);
        result = 31 * result + (getIssued() != null ? getIssued().hashCode() : 0);
        result = 31 * result + (getPerformer() != null ? getPerformer().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getDataAbsentReason() != null ? getDataAbsentReason().hashCode() : 0);
        result = 31 * result + (getInterpretation() != null ? getInterpretation().hashCode() : 0);
        result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
        result = 31 * result + (getBodySite() != null ? getBodySite().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getSpecimen() != null ? getSpecimen().hashCode() : 0);
        result = 31 * result + (getDevice() != null ? getDevice().hashCode() : 0);
        result = 31 * result + (getDiagnosticReport() != null ? getDiagnosticReport().hashCode() : 0);
        result = 31 * result + (getGlstrings() != null ? getGlstrings().hashCode() : 0);
        result = 31 * result + (getHaploids() != null ? getHaploids().hashCode() : 0);
        result = 31 * result + (getGeneticsAlleleName() != null ? getGeneticsAlleleName().hashCode() : 0);
        result = 31 * result + (getGeneticsGene() != null ? getGeneticsGene().hashCode() : 0);
        result = 31 * result + (getSbtNgss() != null ? getSbtNgss().hashCode() : 0);
        result = 31 * result + (getSequences() != null ? getSequences().hashCode() : 0);
        result = 31 * result + (getGeneticsPhaseSet() != null ? getGeneticsPhaseSet().hashCode() : 0);
        result = 31 * result + (getGenotypingResultsMethod() != null ? getGenotypingResultsMethod().hashCode() : 0);
        result = 31 * result + (getGenotypingResultsHaploids() != null ? getGenotypingResultsHaploids().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getIdentifier() != null && getIdentifier().hasValue()) { return true; }
        if (getBasedOn() != null && getBasedOn().hasValue()) { return true; }
        if (getStatus() != null) { return true; }
        if (getCategory() != null && getCategory().hasValue()) { return true; }
        if (getCode() != null && getCode().hasValue()) { return true; }
        if (!StringUtils.isBlank(getSubject())) { return true; }
        if (!StringUtils.isBlank(getContext())) { return true; }
        if (getEffective() != null) { return true; }
        if (getIssued() != null) { return true; }
        if (getPerformer() != null && getPerformer().hasValue()) { return true; }
        if (getValue() != null) { return true; }
        if (getDataAbsentReason() != null) { return true; }
        if (getInterpretation() != null) { return true; }
        if (!StringUtils.isBlank(getComment())) { return true; }
        if (getBodySite() != null) { return true; }
        if (getMethod() != null) { return true; }
        if (getDevice() != null && getDevice().hasValue()) { return true; }
        if (getSpecimen() != null && getSpecimen().hasValue()) { return true; }
        if (getDiagnosticReport() != null && getDiagnosticReport().hasValue()) { return true; }
        if (getGlstrings() != null && getGlstrings().hasValue()) { return true; }
        if (getHaploids() != null && getHaploids().hasValue()) { return true; }
        if (!StringUtils.isBlank(getGeneticsAlleleName())) { return true; }
        if (!StringUtils.isBlank(getGeneticsGene())) { return true; }
        if (getSbtNgss() != null && getSbtNgss().hasValue()) { return true; }
        if (getSequences() != null && getSequences().hasValue()) { return true; }
        if (getGeneticsPhaseSet() != null && getGeneticsPhaseSet().hasValue()) { return true; }
        if (getGenotypingResultsMethod() != null && getGenotypingResultsMethod().hasValue()) { return true; }
        if (getGenotypingResultsHaploids() != null && getGenotypingResultsHaploids().hasValue()) { return true; }

        return false;
    }
}
