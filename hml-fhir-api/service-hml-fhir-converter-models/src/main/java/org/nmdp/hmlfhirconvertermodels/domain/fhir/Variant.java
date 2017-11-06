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
public class Variant implements Serializable {
    private Integer start;
    private Integer end;
    private String observedAllele;
    private String referenceAllele;
    private String cigar;
    private Observation variantPointer;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getObservedAllele() {
        return observedAllele;
    }

    public void setObservedAllele(String observedAllele) {
        this.observedAllele = observedAllele;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public String getCigar() {
        return cigar;
    }

    public void setCigar(String cigar) {
        this.cigar = cigar;
    }

    public Observation getVariantPointer() {
        return variantPointer;
    }

    public void setVariantPointer(Observation variantPointer) {
        this.variantPointer = variantPointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variant)) return false;

        Variant variant = (Variant) o;

        if (getStart() != null ? !getStart().equals(variant.getStart()) : variant.getStart() != null) return false;
        if (getEnd() != null ? !getEnd().equals(variant.getEnd()) : variant.getEnd() != null) return false;
        if (getObservedAllele() != null ? !getObservedAllele().equals(variant.getObservedAllele()) : variant.getObservedAllele() != null)
            return false;
        if (getReferenceAllele() != null ? !getReferenceAllele().equals(variant.getReferenceAllele()) : variant.getReferenceAllele() != null)
            return false;
        if (getCigar() != null ? !getCigar().equals(variant.getCigar()) : variant.getCigar() != null) return false;
        return getVariantPointer() != null ? getVariantPointer().equals(variant.getVariantPointer()) : variant.getVariantPointer() == null;
    }

    @Override
    public int hashCode() {
        int result = getStart() != null ? getStart().hashCode() : 0;
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        result = 31 * result + (getObservedAllele() != null ? getObservedAllele().hashCode() : 0);
        result = 31 * result + (getReferenceAllele() != null ? getReferenceAllele().hashCode() : 0);
        result = 31 * result + (getCigar() != null ? getCigar().hashCode() : 0);
        result = 31 * result + (getVariantPointer() != null ? getVariantPointer().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getStart() != null && getStart() > 0) { return true; }
        if (getEnd() != null && getEnd() > 0) { return true; }
        if (!StringUtils.isBlank(getObservedAllele())) { return true; }
        if (!StringUtils.isBlank(getReferenceAllele())) { return true; }
        if (!StringUtils.isBlank(getCigar())) { return true; }
        if (getVariantPointer() != null && getVariantPointer().hasValue()) { return true; }

        return false;
    }
}
