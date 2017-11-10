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
import org.nmdp.hmlfhirconvertermodels.domain.fhir.*;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.*;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Sample;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Typing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObservationMap implements Converter<Sample, Observations> {

    @Override
    public Observations convert(MappingContext<Sample, Observations> context) {
        if (context.getSource() == null) {
            return null;
        }

        ModelMapper mapper = createMapper();
        Observations observations = new Observations();
        List<Observation> observationList = new ArrayList<>();
        Sample sample = context.getSource();

        for (Typing typing : sample.getTyping()) {

            Observation observation = new Observation();
            Identifier identifier = new Identifier();
            Code code = new Code();

            code.setName(typing.getGeneFamily());
            identifier.setValue(sample.getSampleId());
            identifier.setSystem(sample.getCenterCode());
            observation.setCode(code);
            observation.setIdentifier(identifier);
            observation.setGlstrings(mapper.map(typing, Glstrings.class));
            observation.setHaploids(mapper.map(typing, Haploids.class));
            observation.setSbtNgss(mapper.map(typing, SbtNgss.class));
            observation.setSequences(mapper.map(typing, Sequences.class));
            observation.setGeneticsPhaseSet(mapper.map(typing, GeneticsPhaseSet.class));
            observation.setGenotypingResultsMethod(mapper.map(typing, GenotypingResultsMethod.class));
            observation.setGenotypingResultsHaploids(mapper.map(typing, GenotypingResultsHaploids.class));

            observationList.add(observation);
        }

        observations.setObservations(observationList.stream()
            .filter(Objects::nonNull)
            .filter(observation -> observation.hasValue())
            .collect(Collectors.toList()));

        return observations;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(new GlStringMap());
        mapper.addConverter(new HaploidMap());
        mapper.addConverter(new SbtNgsMap());
        mapper.addConverter(new SequenceMap());
        mapper.addConverter(new GeneticsPhaseSetMap());
        mapper.addConverter(new GenotypingResultsMethodMap());
        mapper.addConverter(new GenotypingResultsHaploidMap());

        return mapper;
    }
}
