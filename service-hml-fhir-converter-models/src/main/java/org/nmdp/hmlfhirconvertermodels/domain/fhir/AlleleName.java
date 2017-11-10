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
public class AlleleName extends StructureDefinition implements Serializable {
    private String allele;

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlleleName)) return false;
        if (!super.equals(o)) return false;

        AlleleName that = (AlleleName) o;

        return getAllele() != null ? getAllele().equals(that.getAllele()) : that.getAllele() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getAllele() != null ? getAllele().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (!StringUtils.isBlank(getAllele())) { return true; }
        if (super.hasValue()) { return true; }

        return false;
    }
}
