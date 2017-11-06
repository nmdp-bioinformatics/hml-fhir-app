package org.nmdp.hmlfhir.mapping.fhir;

/**
 * Created by Andrew S. Brown, Ph.D., <abrown3@nmdp.org>, on 4/25/17.
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
import org.nmdp.hmlfhirconvertermodels.domain.fhir.AlleleName;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Identifier;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.AlleleNames;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlleleNameMap implements Converter<Hml, AlleleNames> {

    @Override
    public AlleleNames convert(MappingContext<Hml, AlleleNames> context) {
        if (context.getSource() == null) {
            return null;
        }

        AlleleNames alleleNames = new AlleleNames();
        List<AlleleName> alleleNameList = new ArrayList<>();
        Hml hml = context.getSource();

        for (Sample sample : hml.getSamples()) {
            List<Typing> typings = sample.getTyping();

            for (Typing typing : typings) {
                List<AlleleAssignment> alleleAssignments = typing.getAlleleAssignment();
                for (AlleleAssignment alleleAssignment : alleleAssignments) {
                    List<Genotype> genotypeList = alleleAssignment.getGenotypes();

                    for (Genotype genotype : genotypeList) {
                        for (DiploidCombination diploidCombination : genotype.getDiploidCombinations()) {
                            LocusBlock locusBlock = diploidCombination.getLocusBlock();
                            for (Allele allele : locusBlock.getAlleles()) {
                                AlleleName alleleName = new AlleleName();

                                alleleName.setName(allele.getName());
                                alleleName.setIdentifier(sample.getSampleId());
                                alleleNameList.add(alleleName);
                            }
                        }
                    }
                }
            }
        }

        alleleNames.setAlleleNames(alleleNameList.stream()
            .filter(Objects::nonNull)
            .filter(alleleName -> alleleName.hasValue())
            .collect(Collectors.toList()));

        return alleleNames;
    }
}
