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
public class BackboneElement implements Serializable {
    private Chromosome chromosome;
    private String genomeBuild;
    private ReferenceSequenceId referenceSequenceId;
    private Sequence referenceSeqPointer;
    private String referenceSeqString;
    private Integer strand;
    private Integer windowStart;
    private Integer windowEnd;

    public Chromosome getChromosome() {
        return chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public String getGenomeBuild() {
        return genomeBuild;
    }

    public void setGenomeBuild(String genomeBuild) {
        this.genomeBuild = genomeBuild;
    }

    public ReferenceSequenceId getReferenceSequenceId() {
        return referenceSequenceId;
    }

    public void setReferenceSequenceId(ReferenceSequenceId referenceSequenceId) {
        this.referenceSequenceId = referenceSequenceId;
    }

    public Sequence getReferenceSeqPointer() {
        return referenceSeqPointer;
    }

    public void setReferenceSeqPointer(Sequence referenceSeqPointer) {
        this.referenceSeqPointer = referenceSeqPointer;
    }

    public String getReferenceSeqString() {
        return referenceSeqString;
    }

    public void setReferenceSeqString(String referenceSeqString) {
        this.referenceSeqString = referenceSeqString;
    }

    public Integer getStrand() {
        return strand;
    }

    public void setStrand(Integer strand) {
        this.strand = strand;
    }

    public Integer getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Integer windowStart) {
        this.windowStart = windowStart;
    }

    public Integer getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Integer windowEnd) {
        this.windowEnd = windowEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackboneElement)) return false;

        BackboneElement that = (BackboneElement) o;

        if (getChromosome() != null ? !getChromosome().equals(that.getChromosome()) : that.getChromosome() != null)
            return false;
        if (getGenomeBuild() != null ? !getGenomeBuild().equals(that.getGenomeBuild()) : that.getGenomeBuild() != null)
            return false;
        if (getReferenceSequenceId() != null ? !getReferenceSequenceId().equals(that.getReferenceSequenceId()) : that.getReferenceSequenceId() != null)
            return false;
        if (getReferenceSeqPointer() != null ? !getReferenceSeqPointer().equals(that.getReferenceSeqPointer()) : that.getReferenceSeqPointer() != null)
            return false;
        if (getReferenceSeqString() != null ? !getReferenceSeqString().equals(that.getReferenceSeqString()) : that.getReferenceSeqString() != null)
            return false;
        if (getStrand() != null ? !getStrand().equals(that.getStrand()) : that.getStrand() != null) return false;
        if (getWindowStart() != null ? !getWindowStart().equals(that.getWindowStart()) : that.getWindowStart() != null)
            return false;
        return getWindowEnd() != null ? getWindowEnd().equals(that.getWindowEnd()) : that.getWindowEnd() == null;
    }

    @Override
    public int hashCode() {
        int result = getChromosome() != null ? getChromosome().hashCode() : 0;
        result = 31 * result + (getGenomeBuild() != null ? getGenomeBuild().hashCode() : 0);
        result = 31 * result + (getReferenceSequenceId() != null ? getReferenceSequenceId().hashCode() : 0);
        result = 31 * result + (getReferenceSeqPointer() != null ? getReferenceSeqPointer().hashCode() : 0);
        result = 31 * result + (getReferenceSeqString() != null ? getReferenceSeqString().hashCode() : 0);
        result = 31 * result + (getStrand() != null ? getStrand().hashCode() : 0);
        result = 31 * result + (getWindowStart() != null ? getWindowStart().hashCode() : 0);
        result = 31 * result + (getWindowEnd() != null ? getWindowEnd().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getChromosome() != null && getChromosome().hasValue()) { return true; }
        if (!StringUtils.isBlank(getGenomeBuild())) { return true; }
        if (getReferenceSequenceId() != null && getReferenceSequenceId().hasValue()) { return true; }
        if (getReferenceSeqPointer() != null && getReferenceSeqPointer().hasValue()) { return true; }
        if (!StringUtils.isBlank(getReferenceSeqString())) { return true; }
        if (getStrand() != null && getStrand() > -10) { return true; }
        if (getWindowStart() != null && getWindowStart() > 0) { return true; }
        if (getWindowEnd() != null && getWindowEnd() > 0) { return true; }

        return false;
    }
}
