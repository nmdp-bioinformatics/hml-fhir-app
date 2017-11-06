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
public class Repository implements Serializable {
    private Type type;
    private String uri;
    private String name;
    private String datasetId;
    private String variantsetId;
    private String readsetId;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getVariantsetId() {
        return variantsetId;
    }

    public void setVariantsetId(String variantsetId) {
        this.variantsetId = variantsetId;
    }

    public String getReadsetId() {
        return readsetId;
    }

    public void setReadsetId(String readsetId) {
        this.readsetId = readsetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repository)) return false;

        Repository that = (Repository) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getUri() != null ? !getUri().equals(that.getUri()) : that.getUri() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getDatasetId() != null ? !getDatasetId().equals(that.getDatasetId()) : that.getDatasetId() != null)
            return false;
        if (getVariantsetId() != null ? !getVariantsetId().equals(that.getVariantsetId()) : that.getVariantsetId() != null)
            return false;
        return getReadsetId() != null ? getReadsetId().equals(that.getReadsetId()) : that.getReadsetId() == null;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getUri() != null ? getUri().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDatasetId() != null ? getDatasetId().hashCode() : 0);
        result = 31 * result + (getVariantsetId() != null ? getVariantsetId().hashCode() : 0);
        result = 31 * result + (getReadsetId() != null ? getReadsetId().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getType() != null && getType().hasValue()) { return true; }
        if (!StringUtils.isBlank(getUri())) { return true; }
        if (!StringUtils.isBlank(getName())) { return true; }
        if (!StringUtils.isBlank(getDatasetId())) { return true; }
        if (!StringUtils.isBlank(getVariantsetId())) { return true; }
        if (!StringUtils.isBlank(getReadsetId())) { return true; }

        return false;
    }
}
