package org.nmdp.hmlfhirconvertermodels.domain.fhir.lists;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/31/17.
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

import org.nmdp.hmlfhirconvertermodels.domain.fhir.SbtNgs;

import java.util.List;
import java.io.Serializable;

public class SbtNgss implements Serializable {
    private List<SbtNgs> sbtNgss;

    public List<SbtNgs> getSbtNgss() {
        return sbtNgss;
    }

    public void setSbtNgss(List<SbtNgs> sbtNgss) {
        this.sbtNgss = sbtNgss;
    }

    public Boolean hasValue() {
        

        if (getSbtNgss() != null && getSbtNgss().size() > 0) { return true; }

        return false;
    }
}
