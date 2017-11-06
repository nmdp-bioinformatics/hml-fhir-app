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
public class Haploid extends StructureDefinition implements Serializable {
    private String locus;
    private String method;
    private String haploidType;

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHaploidType() {
        return haploidType;
    }

    public void setHaploidType(String haploidType) {
        this.haploidType = haploidType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Haploid)) return false;
        if (!super.equals(o)) return false;

        Haploid haploid = (Haploid) o;

        if (getLocus() != null ? !getLocus().equals(haploid.getLocus()) : haploid.getLocus() != null) return false;
        if (getMethod() != null ? !getMethod().equals(haploid.getMethod()) : haploid.getMethod() != null) return false;
        return getHaploidType() != null ? getHaploidType().equals(haploid.getHaploidType()) : haploid.getHaploidType() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getLocus() != null ? getLocus().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getHaploidType() != null ? getHaploidType().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (!StringUtils.isBlank(getLocus())) { return true; }
        if (!StringUtils.isBlank(getMethod())) { return true; }
        if (!StringUtils.isBlank(getHaploidType())) { return true; }
        if (super.hasValue()) { return true; }

        return false;
    }
}
