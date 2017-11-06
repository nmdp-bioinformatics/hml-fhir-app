package org.nmdp.hmlfhir.mapping.hml;

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
import org.modelmapper.spi.MappingContext;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Identifier;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Organization;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.ReportingCenter;
import org.nmdp.hmlfhirconvertermodels.lists.ReportingCenters;

import java.util.ArrayList;
import java.util.List;

public class OrganizationMap implements Converter<FhirMessage, ReportingCenters> {

    @Override
    public ReportingCenters convert(MappingContext<FhirMessage, ReportingCenters> context) {
        if (context.getSource() == null) {
            return null;
        }

        ReportingCenters reportingCenters = new ReportingCenters();
        List<ReportingCenter> reportingCenterList = new ArrayList<>();
        FhirMessage fhir = context.getSource();
        Organization organization = fhir.getOrganization();
        ReportingCenter reportingCenter = new ReportingCenter();
        Identifier identifier = organization.getIdentifier();

        reportingCenter.setCenterId(identifier.getValue());
        reportingCenter.setContext(identifier.getSystem());
        reportingCenterList.add(reportingCenter);
        reportingCenters.setReportingCenters(reportingCenterList);

        return reportingCenters;
    }
}
