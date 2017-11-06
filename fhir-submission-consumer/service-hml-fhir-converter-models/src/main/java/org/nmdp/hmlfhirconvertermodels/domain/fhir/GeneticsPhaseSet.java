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
public class GeneticsPhaseSet extends Observation implements Serializable {
    private String phasingGroup;
    private String phaseSet;

    public String getPhasingGroup() {
        return phasingGroup;
    }

    public void setPhasingGroup(String phasingGroup) {
        this.phasingGroup = phasingGroup;
    }

    public String getPhaseSet() {
        return phaseSet;
    }

    public void setPhaseSet(String phaseSet) {
        this.phaseSet = phaseSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneticsPhaseSet)) return false;
        if (!super.equals(o)) return false;

        GeneticsPhaseSet that = (GeneticsPhaseSet) o;

        if (getPhasingGroup() != null ? !getPhasingGroup().equals(that.getPhasingGroup()) : that.getPhasingGroup() != null)
            return false;
        return getPhaseSet() != null ? getPhaseSet().equals(that.getPhaseSet()) : that.getPhaseSet() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPhasingGroup() != null ? getPhasingGroup().hashCode() : 0);
        result = 31 * result + (getPhaseSet() != null ? getPhaseSet().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (!StringUtils.isBlank(getPhasingGroup())) { return true; }
        if (!StringUtils.isBlank(getPhaseSet())) { return true; }
        if (super.hasValue()) { return true; }

        return false;
    }
}
