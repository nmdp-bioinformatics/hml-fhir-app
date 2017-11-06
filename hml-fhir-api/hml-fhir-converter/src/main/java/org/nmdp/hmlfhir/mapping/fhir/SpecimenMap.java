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
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Collection;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Identifier;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Specimen;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Observations;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Specimens;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Sample;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpecimenMap implements Converter<Sample, Specimens> {

    @Override
    public Specimens convert(MappingContext<Sample, Specimens> context) {
        if (context.getSource() == null) {
            return null;
        }

        Sample sample = context.getSource();
        Specimen specimen = new Specimen();
        Specimens specimens = new Specimens();
        Identifier identifier = new Identifier();
        Collection collection = new Collection();
        ModelMapper mapper = createMapper();

        if (sample.getCollectionMethods().size() > 0) {
            collection.setMethod(sample.getCollectionMethods().get(0).getName());
        }

        identifier.setValue(sample.getSampleId());
        identifier.setSystem(sample.getCenterCode());
        specimen.setIdentifier(identifier);
        specimen.setObservations(mapper.map(sample, Observations.class));
        specimens.setSpecimens(Arrays.asList(specimen));

        return specimens;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(new ObservationMap());

        return mapper;
    }
}
