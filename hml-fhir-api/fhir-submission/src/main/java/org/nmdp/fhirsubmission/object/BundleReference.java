package org.nmdp.fhirsubmission.object;

import java.util.ArrayList;
import java.util.List;

/**
 * fhir-submission
 */

public class BundleReference {

    private String refId;
    private List<String> propertyMap;

    private static final String RESOURCE = "resource";

    public BundleReference(String refId) {
        this.propertyMap = new ArrayList<>();
        this.propertyMap.add(RESOURCE);
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public List<String> getPropertyMap() {
        return propertyMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BundleReference)) return false;

        BundleReference that = (BundleReference) o;

        if (!getRefId().equals(that.getRefId())) return false;
        return getPropertyMap().equals(that.getPropertyMap());
    }

    @Override
    public int hashCode() {
        int result = getRefId().hashCode();
        result = 31 * result + getPropertyMap().hashCode();
        return result;
    }
}
