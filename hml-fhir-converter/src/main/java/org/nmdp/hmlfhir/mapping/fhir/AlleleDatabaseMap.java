package org.nmdp.hmlfhir.mapping.fhir;

/**
 * Created by Andrew S. Brown, Ph.D., <abrown3@nmdp.org>, on 4/18/17.
 * <p>
 * service-hmlFhirConverter
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
import org.nmdp.hmlfhir.mapping.Distinct;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.AlleleDatabase;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.AlleleDatabases;
import org.nmdp.hmlfhirconvertermodels.dto.hml.AlleleAssignment;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Sample;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Typing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlleleDatabaseMap implements Converter<Hml, AlleleDatabases> {

    @Override
    public AlleleDatabases convert(MappingContext<Hml, AlleleDatabases> context) {
        if (context.getSource() == null) {
            return null;
        }

        AlleleDatabases alleleDatabases = new AlleleDatabases();
        List<AlleleDatabase> alleleDatabaseList = new ArrayList<>();
        Hml hml = context.getSource();

        for (Sample sample : hml.getSamples()) {
            List<Typing> typings = sample.getTyping();

            for (Typing typing : typings) {
                List<AlleleAssignment> alleleAssignments = typing.getAlleleAssignment();
                for (AlleleAssignment alleleAssignment : alleleAssignments) {
                    AlleleDatabase alleleDatabase = new AlleleDatabase();

                    alleleDatabase.setName(alleleAssignment.getAlleleDb());
                    alleleDatabase.setVersion(alleleAssignment.getAlleleVersion());
                    alleleDatabase.setIdentifier(sample.getSampleId());
                    alleleDatabaseList.add(alleleDatabase);
                }
            }
        }


        alleleDatabases.setAlleleDatabases(alleleDatabaseList.stream()
                .filter(Objects::nonNull)
                .filter(Distinct.distinctByKey(alleleDatabase -> alleleDatabase.getName()))
                .filter(alleleDatabase -> alleleDatabase.hasValue())
                .collect(Collectors.toList()));

        return alleleDatabases;
    }
}
