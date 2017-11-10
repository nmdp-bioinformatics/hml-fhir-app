package org.nmdp.hmlfhirconvertermodels;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/6/17.
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


import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.HmlId;
import org.nmdp.hmlfhirconvertermodels.lists.*;

import java.io.Serializable;

public class HmlMessage implements Serializable {
    private Ssps ssps;
    private Ssos ssos;
    private Samples observationSamples;
    private Samples sequenceSamples;
    private SbtNgss sbtNgss;
    private Hml patientHml;
    private ReportingCenters organizationReportingCenters;
    private Samples haploidSamples;
    private Samples glStringSamples;
    private Samples genotypingResultsMethodSamples;
    private Samples genotypingResultsHaploidSamples;
    private Samples geneticsPhaseSetSamples;
    private HmlId hmlId;
    private Samples alleleNameSamples;
    private Samples alleleDatabaseSamples;

    public Ssps getSsps() {
        return ssps;
    }

    public void setSsps(Ssps ssps) {
        this.ssps = ssps;
    }

    public Ssos getSsos() {
        return ssos;
    }

    public void setSsos(Ssos ssos) {
        this.ssos = ssos;
    }

    public Samples getObservationSamples() {
        return observationSamples;
    }

    public void setObservationSamples(Samples observationSamples) {
        this.observationSamples = observationSamples;
    }

    public Samples getSequenceSamples() {
        return sequenceSamples;
    }

    public void setSequenceSamples(Samples sequenceSamples) {
        this.sequenceSamples = sequenceSamples;
    }

    public SbtNgss getSbtNgss() {
        return sbtNgss;
    }

    public void setSbtNgss(SbtNgss sbtNgss) {
        this.sbtNgss = sbtNgss;
    }

    public Hml getPatientHml() {
        return patientHml;
    }

    public void setPatientHml(Hml patientHml) {
        this.patientHml = patientHml;
    }

    public ReportingCenters getOrganizationReportingCenters() {
        return organizationReportingCenters;
    }

    public void setOrganizationReportingCenters(ReportingCenters organizationReportingCenters) {
        this.organizationReportingCenters = organizationReportingCenters;
    }

    public Samples getHaploidSamples() {
        return haploidSamples;
    }

    public void setHaploidSamples(Samples haploidSamples) {
        this.haploidSamples = haploidSamples;
    }

    public Samples getGlStringSamples() {
        return glStringSamples;
    }

    public void setGlStringSamples(Samples glStringSamples) {
        this.glStringSamples = glStringSamples;
    }

    public Samples getGenotypingResultsMethodSamples() {
        return genotypingResultsMethodSamples;
    }

    public void setGenotypingResultsMethodSamples(Samples genotypingResultsMethodSamples) {
        this.genotypingResultsMethodSamples = genotypingResultsMethodSamples;
    }

    public Samples getGenotypingResultsHaploidSamples() {
        return genotypingResultsHaploidSamples;
    }

    public void setGenotypingResultsHaploidSamples(Samples genotypingResultsHaploidSamples) {
        this.genotypingResultsHaploidSamples = genotypingResultsHaploidSamples;
    }

    public Samples getGeneticsPhaseSetSamples() {
        return geneticsPhaseSetSamples;
    }

    public void setGeneticsPhaseSetSamples(Samples geneticsPhaseSetSamples) {
        this.geneticsPhaseSetSamples = geneticsPhaseSetSamples;
    }

    public HmlId getHmlId() {
        return hmlId;
    }

    public void setHmlId(HmlId hmlId) {
        this.hmlId = hmlId;
    }

    public Samples getAlleleNameSamples() {
        return alleleNameSamples;
    }

    public void setAlleleNameSamples(Samples alleleNameSamples) {
        this.alleleNameSamples = alleleNameSamples;
    }

    public Samples getAlleleDatabaseSamples() {
        return alleleDatabaseSamples;
    }

    public void setAlleleDatabaseSamples(Samples alleleDatabaseSamples) {
        this.alleleDatabaseSamples = alleleDatabaseSamples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HmlMessage)) return false;

        HmlMessage that = (HmlMessage) o;

        if (getSsps() != null ? !getSsps().equals(that.getSsps()) : that.getSsps() != null) return false;
        if (getSsos() != null ? !getSsos().equals(that.getSsos()) : that.getSsos() != null) return false;
        if (getObservationSamples() != null ? !getObservationSamples().equals(that.getObservationSamples()) : that.getObservationSamples() != null)
            return false;
        if (getSequenceSamples() != null ? !getSequenceSamples().equals(that.getSequenceSamples()) : that.getSequenceSamples() != null)
            return false;
        if (getSbtNgss() != null ? !getSbtNgss().equals(that.getSbtNgss()) : that.getSbtNgss() != null) return false;
        if (getPatientHml() != null ? !getPatientHml().equals(that.getPatientHml()) : that.getPatientHml() != null)
            return false;
        if (getOrganizationReportingCenters() != null ? !getOrganizationReportingCenters().equals(that.getOrganizationReportingCenters()) : that.getOrganizationReportingCenters() != null)
            return false;
        if (getHaploidSamples() != null ? !getHaploidSamples().equals(that.getHaploidSamples()) : that.getHaploidSamples() != null)
            return false;
        if (getGlStringSamples() != null ? !getGlStringSamples().equals(that.getGlStringSamples()) : that.getGlStringSamples() != null)
            return false;
        if (getGenotypingResultsMethodSamples() != null ? !getGenotypingResultsMethodSamples().equals(that.getGenotypingResultsMethodSamples()) : that.getGenotypingResultsMethodSamples() != null)
            return false;
        if (getGenotypingResultsHaploidSamples() != null ? !getGenotypingResultsHaploidSamples().equals(that.getGenotypingResultsHaploidSamples()) : that.getGenotypingResultsHaploidSamples() != null)
            return false;
        if (getGeneticsPhaseSetSamples() != null ? !getGeneticsPhaseSetSamples().equals(that.getGeneticsPhaseSetSamples()) : that.getGeneticsPhaseSetSamples() != null)
            return false;
        if (getHmlId() != null ? !getHmlId().equals(that.getHmlId()) : that.getHmlId() != null) return false;
        if (getAlleleNameSamples() != null ? !getAlleleNameSamples().equals(that.getAlleleNameSamples()) : that.getAlleleNameSamples() != null)
            return false;
        return getAlleleDatabaseSamples() != null ? getAlleleDatabaseSamples().equals(that.getAlleleDatabaseSamples()) : that.getAlleleDatabaseSamples() == null;
    }

    @Override
    public int hashCode() {
        int result = getSsps() != null ? getSsps().hashCode() : 0;
        result = 31 * result + (getSsos() != null ? getSsos().hashCode() : 0);
        result = 31 * result + (getObservationSamples() != null ? getObservationSamples().hashCode() : 0);
        result = 31 * result + (getSequenceSamples() != null ? getSequenceSamples().hashCode() : 0);
        result = 31 * result + (getSbtNgss() != null ? getSbtNgss().hashCode() : 0);
        result = 31 * result + (getPatientHml() != null ? getPatientHml().hashCode() : 0);
        result = 31 * result + (getOrganizationReportingCenters() != null ? getOrganizationReportingCenters().hashCode() : 0);
        result = 31 * result + (getHaploidSamples() != null ? getHaploidSamples().hashCode() : 0);
        result = 31 * result + (getGlStringSamples() != null ? getGlStringSamples().hashCode() : 0);
        result = 31 * result + (getGenotypingResultsMethodSamples() != null ? getGenotypingResultsMethodSamples().hashCode() : 0);
        result = 31 * result + (getGenotypingResultsHaploidSamples() != null ? getGenotypingResultsHaploidSamples().hashCode() : 0);
        result = 31 * result + (getGeneticsPhaseSetSamples() != null ? getGeneticsPhaseSetSamples().hashCode() : 0);
        result = 31 * result + (getHmlId() != null ? getHmlId().hashCode() : 0);
        result = 31 * result + (getAlleleNameSamples() != null ? getAlleleNameSamples().hashCode() : 0);
        result = 31 * result + (getAlleleDatabaseSamples() != null ? getAlleleDatabaseSamples().hashCode() : 0);
        return result;
    }
}
