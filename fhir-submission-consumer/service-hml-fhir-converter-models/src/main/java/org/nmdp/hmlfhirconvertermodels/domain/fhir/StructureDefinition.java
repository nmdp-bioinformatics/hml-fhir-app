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
import org.joda.time.DateTime;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;

import java.net.URL;
import java.io.Serializable;


@FhirResource
public class StructureDefinition implements Serializable {

    private URL url;
    private String identifier;
    private String version;
    private String name;
    private String title;
    private Status status;
    private Boolean experimental;
    private DateTime date;
    private String publisher;
    private Contact contact;
    private String description;
    private UsageContext useContext;
    private Jurisdiction jurisdiction;
    private String purpose;
    private String copyright;
    private String keyword;
    private String fhirVersion;
    private StructureDefinitionKind kind;
    private Boolean abstracted;
    private ExtensionContext contextType;
    private String context;
    private String contextVariant;
    private FhirDefinedType type;
    private URL baseDefinition;
    private TypeDerivationRule derivation;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getExperimental() {
        return experimental;
    }

    public void setExperimental(Boolean experimental) {
        this.experimental = experimental;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UsageContext getUseContext() {
        return useContext;
    }

    public void setUseContext(UsageContext useContext) {
        this.useContext = useContext;
    }

    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(Jurisdiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }

    public StructureDefinitionKind getKind() {
        return kind;
    }

    public void setKind(StructureDefinitionKind kind) {
        this.kind = kind;
    }

    public Boolean getAbstracted() {
        return abstracted;
    }

    public void setAbstracted(Boolean abstracted) {
        this.abstracted = abstracted;
    }

    public ExtensionContext getContextType() {
        return contextType;
    }

    public void setContextType(ExtensionContext contextType) {
        this.contextType = contextType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextVariant() {
        return contextVariant;
    }

    public void setContextVariant(String contextVariant) {
        this.contextVariant = contextVariant;
    }

    public FhirDefinedType getType() {
        return type;
    }

    public void setType(FhirDefinedType type) {
        this.type = type;
    }

    public URL getBaseDefinition() {
        return baseDefinition;
    }

    public void setBaseDefinition(URL baseDefinition) {
        this.baseDefinition = baseDefinition;
    }

    public TypeDerivationRule getDerivation() {
        return derivation;
    }

    public void setDerivation(TypeDerivationRule derivation) {
        this.derivation = derivation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructureDefinition)) return false;

        StructureDefinition that = (StructureDefinition) o;

        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) return false;
        if (getIdentifier() != null ? !getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() != null)
            return false;
        if (getVersion() != null ? !getVersion().equals(that.getVersion()) : that.getVersion() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
        if (getStatus() != that.getStatus()) return false;
        if (getExperimental() != null ? !getExperimental().equals(that.getExperimental()) : that.getExperimental() != null)
            return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        if (getPublisher() != null ? !getPublisher().equals(that.getPublisher()) : that.getPublisher() != null)
            return false;
        if (getContact() != null ? !getContact().equals(that.getContact()) : that.getContact() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getUseContext() != null ? !getUseContext().equals(that.getUseContext()) : that.getUseContext() != null)
            return false;
        if (getJurisdiction() != null ? !getJurisdiction().equals(that.getJurisdiction()) : that.getJurisdiction() != null)
            return false;
        if (getPurpose() != null ? !getPurpose().equals(that.getPurpose()) : that.getPurpose() != null) return false;
        if (getCopyright() != null ? !getCopyright().equals(that.getCopyright()) : that.getCopyright() != null)
            return false;
        if (getKeyword() != null ? !getKeyword().equals(that.getKeyword()) : that.getKeyword() != null) return false;
        if (getFhirVersion() != null ? !getFhirVersion().equals(that.getFhirVersion()) : that.getFhirVersion() != null)
            return false;
        if (getKind() != null ? !getKind().equals(that.getKind()) : that.getKind() != null) return false;
        if (getAbstracted() != null ? !getAbstracted().equals(that.getAbstracted()) : that.getAbstracted() != null)
            return false;
        if (getContextType() != null ? !getContextType().equals(that.getContextType()) : that.getContextType() != null)
            return false;
        if (getContext() != null ? !getContext().equals(that.getContext()) : that.getContext() != null) return false;
        if (getContextVariant() != null ? !getContextVariant().equals(that.getContextVariant()) : that.getContextVariant() != null)
            return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getBaseDefinition() != null ? !getBaseDefinition().equals(that.getBaseDefinition()) : that.getBaseDefinition() != null)
            return false;
        return getDerivation() != null ? getDerivation().equals(that.getDerivation()) : that.getDerivation() == null;
    }

    @Override
    public int hashCode() {
        int result = getUrl() != null ? getUrl().hashCode() : 0;
        result = 31 * result + (getIdentifier() != null ? getIdentifier().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getExperimental() != null ? getExperimental().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getPublisher() != null ? getPublisher().hashCode() : 0);
        result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getUseContext() != null ? getUseContext().hashCode() : 0);
        result = 31 * result + (getJurisdiction() != null ? getJurisdiction().hashCode() : 0);
        result = 31 * result + (getPurpose() != null ? getPurpose().hashCode() : 0);
        result = 31 * result + (getCopyright() != null ? getCopyright().hashCode() : 0);
        result = 31 * result + (getKeyword() != null ? getKeyword().hashCode() : 0);
        result = 31 * result + (getFhirVersion() != null ? getFhirVersion().hashCode() : 0);
        result = 31 * result + (getKind() != null ? getKind().hashCode() : 0);
        result = 31 * result + (getAbstracted() != null ? getAbstracted().hashCode() : 0);
        result = 31 * result + (getContextType() != null ? getContextType().hashCode() : 0);
        result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
        result = 31 * result + (getContextVariant() != null ? getContextVariant().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getBaseDefinition() != null ? getBaseDefinition().hashCode() : 0);
        result = 31 * result + (getDerivation() != null ? getDerivation().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getUrl() != null) { return true; }
        if (getIdentifier() != null) { return true; }
        if (!StringUtils.isBlank(getVersion())) { return true; }
        if (!StringUtils.isBlank(getName())) { return true; }
        if (!StringUtils.isBlank(getTitle())) { return true; }
        if (getStatus() != null) { return true; }
        if (getExperimental() != null) { return true; }
        if (getDate() != null) { return true; }
        if (!StringUtils.isBlank(getPublisher())) { return true; }
        if (getContact() != null && getContact().hasValue()) { return true; }
        if (!StringUtils.isBlank(getDescription())) { return true; }
        if (getUseContext() != null && getUseContext().hasValue()) { return true; }
        if (getJurisdiction() != null && getJurisdiction().hasValue()) { return true; }
        if (!StringUtils.isBlank(getPurpose())) { return true; }
        if (!StringUtils.isBlank(getCopyright())) { return true; }
        if (!StringUtils.isBlank(getKeyword())) { return true; }
        if (!StringUtils.isBlank(getFhirVersion())) { return true; }
        if (getKind() != null && getKind().hasValue()) { return true; }
        if (getAbstracted() != null) { return true; }
        if (getContextType() != null && getContextType().hasValue()) { return true; }
        if (!StringUtils.isBlank(getContext())) { return true; }
        if (!StringUtils.isBlank(getContextVariant())) { return true; }
        if (!StringUtils.isBlank(getContextVariant())) { return true; }
        if (getType() != null && getType().hasValue()) { return true; }
        if (getBaseDefinition() != null) { return true; }
        if (getDerivation() != null && getDerivation().hasValue()) { return true; }

        return false;
    }
}
