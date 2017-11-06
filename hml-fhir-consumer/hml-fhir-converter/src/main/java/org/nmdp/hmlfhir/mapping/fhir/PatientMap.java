package org.nmdp.hmlfhir.mapping.fhir;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/31/17.
 * <p>
 * hml-fhir
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

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.DiagnosticReport;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Identifier;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Patient;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Patients;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Specimens;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Sample;

import java.util.ArrayList;
import java.util.List;

public class PatientMap implements Converter<Hml, Patients> {

    @Override
    public Patients convert(MappingContext<Hml, Patients> context) {
        if (context.getSource() == null) {
            return null;
        }

        Patients patients = new Patients();
        List<Patient> patientList = new ArrayList<>();
        Hml hml = context.getSource();
        ModelMapper mapper = createMapper();

        for (Sample sample : hml.getSamples()) {
            Patient patient = new Patient();
            Identifier identifier = new Identifier();

            identifier.setValue(hml.getHmlId().getRootName() + "_" + hml.getHmlId().getExtension());
            identifier.setSystem(sample.getSampleId());
            patient.setIdentifier(identifier);
            patient.setSpecimens(mapper.map(sample, Specimens.class));
            patient.setDiagnosticReport(mapper.map(hml, DiagnosticReport.class));
            patientList.add(patient);
        }

        patients.setPatients(patientList);

        return patients;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(new SpecimenMap());
        mapper.addConverter(new DiagnosticReportMap());

        return mapper;
    }
}
