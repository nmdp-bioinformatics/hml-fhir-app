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

import ch.qos.logback.core.encoder.ByteArrayUtil;
import org.apache.commons.lang3.StringUtils;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirPrimaryResource;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Glstrings;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Haploids;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Specimens;

import java.util.Arrays;
import java.util.Date;

import java.io.Serializable;

@FhirPrimaryResource
@FhirResource
public class Patient implements Serializable {
    private Identifier identifier;
    private Boolean active;
    private String name;
    private String telecom;
    private String gender;
    private Date birthDate;
    private Boolean deceased;
    private Address address;
    private Enum<MaritalStatus> maritalStatus;
    private Boolean multipleBirth;
    private Byte[] photo;
    private Practitioner generalPractitioner;
    private Organization managingOrganization;
    private Specimens specimens;
    private DiagnosticReport diagnosticReport;

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getDeceased() {
        return deceased;
    }

    public void setDeceased(Boolean deceased) {
        this.deceased = deceased;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Enum<MaritalStatus> getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Enum<MaritalStatus> maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Boolean getMultipleBirth() {
        return multipleBirth;
    }

    public void setMultipleBirth(Boolean multipleBirth) {
        this.multipleBirth = multipleBirth;
    }

    public Byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(Byte[] photo) {
        this.photo = photo;
    }

    public Practitioner getGeneralPractitioner() {
        return generalPractitioner;
    }

    public void setGeneralPractitioner(Practitioner generalPractitioner) {
        this.generalPractitioner = generalPractitioner;
    }

    public Organization getManagingOrganization() {
        return managingOrganization;
    }

    public void setManagingOrganization(Organization managingOrganization) {
        this.managingOrganization = managingOrganization;
    }

    public Specimens getSpecimens() {
        return specimens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;

        Patient patient = (Patient) o;

        if (getIdentifier() != null ? !getIdentifier().equals(patient.getIdentifier()) : patient.getIdentifier() != null)
            return false;
        if (getActive() != null ? !getActive().equals(patient.getActive()) : patient.getActive() != null) return false;
        if (getName() != null ? !getName().equals(patient.getName()) : patient.getName() != null) return false;
        if (getTelecom() != null ? !getTelecom().equals(patient.getTelecom()) : patient.getTelecom() != null)
            return false;
        if (getGender() != null ? !getGender().equals(patient.getGender()) : patient.getGender() != null) return false;
        if (getBirthDate() != null ? !getBirthDate().equals(patient.getBirthDate()) : patient.getBirthDate() != null)
            return false;
        if (getDeceased() != null ? !getDeceased().equals(patient.getDeceased()) : patient.getDeceased() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(patient.getAddress()) : patient.getAddress() != null)
            return false;
        if (getMaritalStatus() != null ? !getMaritalStatus().equals(patient.getMaritalStatus()) : patient.getMaritalStatus() != null)
            return false;
        if (getMultipleBirth() != null ? !getMultipleBirth().equals(patient.getMultipleBirth()) : patient.getMultipleBirth() != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getPhoto(), patient.getPhoto())) return false;
        if (getGeneralPractitioner() != null ? !getGeneralPractitioner().equals(patient.getGeneralPractitioner()) : patient.getGeneralPractitioner() != null)
            return false;
        if (getManagingOrganization() != null ? !getManagingOrganization().equals(patient.getManagingOrganization()) : patient.getManagingOrganization() != null)
            return false;
        if (getSpecimens() != null ? !getSpecimens().equals(patient.getSpecimens()) : patient.getSpecimens() != null)
            return false;
        return getDiagnosticReport() != null ? getDiagnosticReport().equals(patient.getDiagnosticReport()) : patient.getDiagnosticReport() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdentifier() != null ? getIdentifier().hashCode() : 0;
        result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTelecom() != null ? getTelecom().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getBirthDate() != null ? getBirthDate().hashCode() : 0);
        result = 31 * result + (getDeceased() != null ? getDeceased().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getMaritalStatus() != null ? getMaritalStatus().hashCode() : 0);
        result = 31 * result + (getMultipleBirth() != null ? getMultipleBirth().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getPhoto());
        result = 31 * result + (getGeneralPractitioner() != null ? getGeneralPractitioner().hashCode() : 0);
        result = 31 * result + (getManagingOrganization() != null ? getManagingOrganization().hashCode() : 0);
        result = 31 * result + (getSpecimens() != null ? getSpecimens().hashCode() : 0);
        result = 31 * result + (getDiagnosticReport() != null ? getDiagnosticReport().hashCode() : 0);
        return result;
    }

    public void setSpecimens(Specimens specimens) {
        this.specimens = specimens;
    }

    public DiagnosticReport getDiagnosticReport() {
        return diagnosticReport;
    }

    public void setDiagnosticReport(DiagnosticReport diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    public Boolean hasValue() {
        

        if (getIdentifier() != null && getIdentifier().hasValue()) { return true; }
        if (!StringUtils.isBlank(getName())) { return true; }
        if (!StringUtils.isBlank(getTelecom())) { return true; }
        if (!StringUtils.isBlank(getGender())) { return true; }
        if (getBirthDate() != null) { return true; }
        if (getAddress() != null && getAddress().hasValue()) { return true; }
        if (getMaritalStatus() != null) { return true; }
        if (getPhoto() != null && getPhoto().length > 0) { return true; }
        if (getGeneralPractitioner() != null && getGeneralPractitioner().hasValue()) { return true; }
        if (getManagingOrganization() != null && getManagingOrganization().hasValue()) { return true; }
        if (getSpecimens() != null && getSpecimens().hasValue()) { return true; }
        if (getDiagnosticReport() != null && getDiagnosticReport().hasValue()) { return true; }

        return false;
    }
}
