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
import org.modelmapper.spi.MappingContext;
import org.nmdp.hmlfhir.mapping.Distinct;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.*;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Sequence;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Sequences;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SequenceMap implements Converter<Typing, Sequences> {

    @Override
    public Sequences convert(MappingContext<Typing, Sequences> context) {
        if (context.getSource() == null) {
            return null;
        }

        Sequences sequences = new Sequences();
        Typing typing = context.getSource();
        List<Sequence> sequenceList = createConsensusSequences(typing);

        sequences.setSequences(sequenceList.stream()
            .filter(Objects::nonNull)
            .filter(sequence -> sequence.hasValue())
            .collect(Collectors.toList()));

        return sequences;
    }

    private BackboneElement createReferenceSequence(List<ReferenceDatabase> referenceDatabases) {
        if (referenceDatabases == null || referenceDatabases.size() == 0) {
            return null;
        }

        ReferenceDatabase referenceDatabase = referenceDatabases.get(0);

        if (referenceDatabase == null) {
            return null;
        }

        BackboneElement backboneElement = new BackboneElement();
        ReferenceSequenceId referenceSequenceId = new ReferenceSequenceId();
        Identifier referenceSequenceIdentifer = new Identifier();
        Identifier observedIdentifier = new Identifier();
        Sequence pointer = new Sequence();
        Sequence observed = new Sequence();

        referenceSequenceId.setIdentifier(referenceSequenceIdentifer);
        backboneElement.setReferenceSequenceId(referenceSequenceId);
        referenceSequenceIdentifer.setValue(referenceDatabase.getVersion());
        referenceSequenceIdentifer.setSystem(referenceDatabase.getDescription());

        ReferenceSequence referenceSequence = referenceDatabase.getReferenceSequence();

        if (referenceSequence == null) {
            return backboneElement;
        }

        backboneElement.setWindowEnd(referenceSequence.getEnd());
        backboneElement.setWindowStart(referenceSequence.getStart());
        observedIdentifier.setValue(referenceSequence.getAccession());
        observedIdentifier.setSystem(referenceSequence.getReferenceSequenceId());
        observed.setIdentifier(observedIdentifier);
        pointer.setPointer(observed);
        backboneElement.setReferenceSeqPointer(pointer);
        backboneElement.setStrand(Integer.parseInt(referenceSequence.getStrand()));
        backboneElement.setReferenceSeqString(referenceSequence.getUri());

        return backboneElement;
    }

    private List<Sequence> createConsensusSequences(Typing typing) {
        List<Sequence> sequences = new ArrayList<>();
        ConsensusSequence consensusSequence = typing.getConsensusSequence();

        if (typing == null) {
            return null;
        }

        for (ConsensusSequenceBlock consensusSequenceBlock : consensusSequence.getConsensusSequenceBlocks()) {
            org.nmdp.hmlfhirconvertermodels.dto.hml.Sequence seq = consensusSequenceBlock.getSequence();
            org.nmdp.hmlfhirconvertermodels.dto.hml.Variant var = consensusSequenceBlock.getVariant();
            Sequence sequence = new Sequence();
            SequenceQuality sequenceQuality = consensusSequenceBlock.getSequenceQuality();
            org.nmdp.hmlfhirconvertermodels.domain.fhir.Variant variant = new org.nmdp.hmlfhirconvertermodels.domain.fhir.Variant();
            Quality quality = new Quality();
            Score score = new Score();
            Identifier identifier = new Identifier();

            variant.setStart(var.getStart());
            variant.setEnd(var.getEnd());
            variant.setReferenceAllele(var.getReferenceBases());
            variant.setObservedAllele(var.getAlternateBases());
            score.setValue(var.getQualityScore());
            quality.setScore(score);
            quality.setStart(sequenceQuality.getSequenceStart());
            quality.setEnd(sequenceQuality.getSequenceEnd());
            sequence.setIdentifier(identifier);
            sequence.setQuality(quality);
            sequence.setObservedSeq(seq.getSequence());
            sequence.setReferenceSeq(createReferenceSequence(consensusSequence.getReferenceDatabase()));

            sequences.add(sequence);
        }

        return sequences;
    }
}
