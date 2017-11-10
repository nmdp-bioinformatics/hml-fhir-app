package org.nmdp.hmlfhir.mapping.fhir;

/**
 * Created by Andrew S. Brown, Ph.D., <abrown3@nmdp.org>, on 5/3/17.
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
import org.nmdp.hmlfhirconvertermodels.domain.fhir.GeneticsPhaseSet;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.GeneticsPhaseSets;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeneticsPhaseSetMap implements Converter<Typing, GeneticsPhaseSets> {

    @Override
    public GeneticsPhaseSets convert(MappingContext<Typing, GeneticsPhaseSets> context) {
        if (context.getSource() == null) {
            return null;
        }

        GeneticsPhaseSets geneticsPhaseSets = new GeneticsPhaseSets();
        Typing typing = context.getSource();
        List<GeneticsPhaseSet> geneticsPhaseSetList = new ArrayList<>();
        ConsensusSequence consensusSequence = typing.getConsensusSequence();
        List<ConsensusSequenceBlock> consensusSequenceBlocks = consensusSequence.getConsensusSequenceBlocks();

        consensusSequenceBlocks.stream()
                .forEach(c -> geneticsPhaseSetList.add(convertConsensusSequenceBlockToGeneticsPhaseSet(c)));

        geneticsPhaseSets.setGeneticsPhaseSets(geneticsPhaseSetList.stream()
            .filter(Objects::nonNull)
            .filter(geneticsPhaseSet -> geneticsPhaseSet.hasValue())
            .collect(Collectors.toList()));

        return geneticsPhaseSets;
    }

    private GeneticsPhaseSet convertConsensusSequenceBlockToGeneticsPhaseSet(ConsensusSequenceBlock consensusSequenceBlock) {
        GeneticsPhaseSet geneticsPhaseSet = new GeneticsPhaseSet();

        geneticsPhaseSet.setPhasingGroup(consensusSequenceBlock.getPhasingGroup());
        geneticsPhaseSet.setPhaseSet(consensusSequenceBlock.getPhaseSet());

        return geneticsPhaseSet;
    }
}
