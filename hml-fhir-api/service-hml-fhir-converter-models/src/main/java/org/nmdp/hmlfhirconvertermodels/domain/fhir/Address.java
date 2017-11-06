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
public class Address implements Serializable {
    private String street1;
    private String street2;
    private String street3;
    private Integer zip;
    private String state;
    private String city;
    private String country;

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (getStreet1() != null ? !getStreet1().equals(address.getStreet1()) : address.getStreet1() != null)
            return false;
        if (getStreet2() != null ? !getStreet2().equals(address.getStreet2()) : address.getStreet2() != null)
            return false;
        if (getStreet3() != null ? !getStreet3().equals(address.getStreet3()) : address.getStreet3() != null)
            return false;
        if (getZip() != null ? !getZip().equals(address.getZip()) : address.getZip() != null) return false;
        if (getState() != null ? !getState().equals(address.getState()) : address.getState() != null) return false;
        if (getCity() != null ? !getCity().equals(address.getCity()) : address.getCity() != null) return false;
        return getCountry() != null ? getCountry().equals(address.getCountry()) : address.getCountry() == null;
    }

    @Override
    public int hashCode() {
        int result = getStreet1() != null ? getStreet1().hashCode() : 0;
        result = 31 * result + (getStreet2() != null ? getStreet2().hashCode() : 0);
        result = 31 * result + (getStreet3() != null ? getStreet3().hashCode() : 0);
        result = 31 * result + (getZip() != null ? getZip().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (!StringUtils.isBlank(getStreet1())) { return true; }
        if (!StringUtils.isBlank(getStreet2())) { return true; }
        if (!StringUtils.isBlank(getStreet3())) { return true; }
        if (getZip() != null && getZip() > 0) { return true; }
        if (!StringUtils.isBlank(getState())) { return true; }
        if (!StringUtils.isBlank(getCity())) { return true; }
        if (!StringUtils.isBlank(getCountry())) { return true; }

        return false;
    }
}
