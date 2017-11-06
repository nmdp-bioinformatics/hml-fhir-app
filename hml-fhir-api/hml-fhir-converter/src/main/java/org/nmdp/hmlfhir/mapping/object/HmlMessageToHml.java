package org.nmdp.hmlfhir.mapping.object;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/7/17.
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

import org.nmdp.hmlfhirconvertermodels.HmlMessage;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HmlMessageToHml {

    public static Hml toDto(HmlMessage hmlMessage) {
        Hml hml = hmlMessage.getPatientHml();
        List<Sample> reconstructedSamples = new ArrayList<>();
        List<Sample> observationSamples = hmlMessage.getObservationSamples().getSamples();

        for (Sample observationSample : observationSamples) {
            String sampleId = observationSample.getSampleId();
            for (Typing observationTyping : getSampleTypings(observationSamples, sampleId)) {
                observationTyping.setAlleleAssignment(getTypingAlleleAssignments(
                        hmlMessage.getHaploidSamples().getSamples(), hmlMessage.getGlStringSamples().getSamples(),
                        hmlMessage.getAlleleDatabaseSamples().getSamples(), hmlMessage.getAlleleNameSamples().getSamples(),
                        hmlMessage.getGenotypingResultsHaploidSamples().getSamples(), sampleId));
                observationTyping.setTypingMethod(getTypingTypingMethod(
                        hmlMessage.getSsps().getSsps(), hmlMessage.getSsos().getSsos(),
                        hmlMessage.getSbtNgss().getSbtNgss(), hmlMessage.getGenotypingResultsMethodSamples().getSamples(),
                        sampleId));
                observationTyping.setConsensusSequence(getTypingConsensusSequence(
                        hmlMessage.getGeneticsPhaseSetSamples().getSamples(), hmlMessage.getSequenceSamples().getSamples(),
                        sampleId));
            }

            reconstructedSamples.add(observationSample);
        }

        hml.setSamples(reconstructedSamples);
        hml.setReportingCenters(hmlMessage.getOrganizationReportingCenters().getReportingCenters());
        hml.setHmlId(hmlMessage.getHmlId());

        return hml;
    }

    private static List<Typing> getSampleTypings(List<Sample> samples, String sampleId) {
        return samples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList()).stream()
                        .filter(Objects::nonNull)
                        .map(sample -> sample.getTyping())
                        .flatMap(typing -> typing.stream())
                        .collect(Collectors.toList());
    }

    private static List<AlleleAssignment> getTypingAlleleAssignments(List<Sample> haploidSamples, List<Sample> glStringSamples,
        List<Sample> alleleDatabaseSamples, List<Sample> alleleNameSamples, List<Sample> genotypingResultsHaploidSamples,
        String sampleId) {
        AlleleAssignment alleleAssignment = new AlleleAssignment();
        List<Sample> filteredHaploidSamples = haploidSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList());
        List<Sample> filteredGlStringSamples = glStringSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList());
        List<Sample> filteredAlleleDatabaseSamples = alleleDatabaseSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList());
        List<Sample> filteredAlleleNameSamples = alleleNameSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList());
        List<Sample> filteredGenotypingResultsHaploidSamples = genotypingResultsHaploidSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .collect(Collectors.toList());

        alleleAssignment.setHaploid(getAlleleAssignmentHaploids(filteredHaploidSamples, filteredGenotypingResultsHaploidSamples));
        alleleAssignment.setGlString(getAlleleAssignmentGlStrings(filteredGlStringSamples));
        setAlleleAssignmentAlleleDatabase(filteredAlleleDatabaseSamples, alleleAssignment);
        alleleAssignment.setGenotypes(getAlleleAssignmentGenotypes(filteredAlleleNameSamples));

        return Arrays.asList(alleleAssignment);
    }

    private static List<Haploid> getAlleleAssignmentHaploids(List<Sample> samples, List<Sample> xSamples) {
        List<Haploid> haploidList = samples.stream()
                .filter(Objects::nonNull)
                .map(sample -> sample.getTyping())
                .flatMap(typings -> typings.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(typing -> typing.getAlleleAssignment())
                    .flatMap(alleleAssignments -> alleleAssignments.stream())
                    .collect(Collectors.toList()).stream()
                        .filter(Objects::nonNull)
                        .map(alleleAssignment -> alleleAssignment.getHaploid())
                        .flatMap(haploids -> haploids.stream())
                        .collect(Collectors.toList());

        haploidList.addAll(xSamples.stream()
            .filter(Objects::nonNull)
            .map(sample -> sample.getTyping())
            .flatMap(typings -> typings.stream())
            .collect(Collectors.toList()).stream()
                .filter(Objects::nonNull)
                .map(typing -> typing.getAlleleAssignment())
                .flatMap(alleleAssignments -> alleleAssignments.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(alleleAssignment -> alleleAssignment.getHaploid())
                    .flatMap(haploids -> haploids.stream())
                    .collect(Collectors.toList()));

        return haploidList;
    }

    private static GlString getAlleleAssignmentGlStrings(List<Sample> samples) {
        return samples.stream()
                .filter(Objects::nonNull)
                .map(sample -> sample.getTyping())
                .flatMap(typings -> typings.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(typing -> typing.getAlleleAssignment())
                    .flatMap(alleleAssignments -> alleleAssignments.stream())
                    .collect(Collectors.toList()).stream()
                        .filter(Objects::nonNull)
                        .map(alleleAssignment -> alleleAssignment.getGlString())
                        .findFirst()
                        .get();
    }

    private static void setAlleleAssignmentAlleleDatabase(List<Sample> samples, AlleleAssignment alleleAssignment) {
        AlleleAssignment fhirAlleleAssignment = samples.stream()
                .filter(Objects::nonNull)
                .map(sample -> sample.getTyping())
                .flatMap(typings -> typings.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(typing -> typing.getAlleleAssignment())
                    .flatMap(alleleAssignments -> alleleAssignments.stream())
                    .findFirst()
                    .get();

        alleleAssignment.setAlleleDb(fhirAlleleAssignment.getAlleleDb());
        alleleAssignment.setAlleleVersion(fhirAlleleAssignment.getAlleleVersion());
    }

    private static List<Genotype> getAlleleAssignmentGenotypes(List<Sample> samples) {
        return samples.stream()
                .filter(Objects::nonNull)
                .map(sample -> sample.getTyping())
                .flatMap(typings -> typings.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(typing -> typing.getAlleleAssignment())
                    .flatMap(alleleAssignments -> alleleAssignments.stream())
                    .collect(Collectors.toList()).stream()
                        .filter(Objects::nonNull)
                        .map(alleleAssignment -> alleleAssignment.getGenotypes())
                        .flatMap(genotypes -> genotypes.stream())
                        .collect(Collectors.toList());
    }

    private static TypingMethod getTypingTypingMethod(List<Ssp> ssps, List<Sso> ssos, List<SbtNgs> sbtNgss,
        List<Sample> gentoypingResultsMethodSamples, String sampleId) {
        sbtNgss = getTypingTypingMethodSbtNgss(sbtNgss, gentoypingResultsMethodSamples, sampleId);
        TypingMethod typingMethod = new TypingMethod();
        Sso sso = ssos.stream().filter(Objects::nonNull).findFirst().get();
        Ssp ssp = ssps.stream().filter(Objects::nonNull).findFirst().get();

        typingMethod.setSbtNgs(sbtNgss);
        typingMethod.setSso(sso);
        typingMethod.setSsp(ssp);

        return typingMethod;
    }

    private static List<SbtNgs> getTypingTypingMethodSbtNgss(List<SbtNgs> sbtNgss, List<Sample> gentoypingResultsMethodSamples, String sampleId) {
        sbtNgss.addAll(gentoypingResultsMethodSamples.stream()
                .filter(Objects::nonNull)
                .filter(sample -> sample.getSampleId().equals(sampleId))
                .map(sample -> sample.getTyping())
                .flatMap(typings -> typings.stream())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(typing -> typing.getTypingMethod())
                    .collect(Collectors.toList()).stream()
                        .filter(Objects::nonNull)
                        .map(typingMethod -> typingMethod.getSbtNgs())
                        .flatMap(typingMethods -> typingMethods.stream())
                        .collect(Collectors.toList()));

        return sbtNgss;
    }

    private static ConsensusSequence getTypingConsensusSequence(List<Sample> geneticsPhaseSetSamples, List<Sample> sequenceSamples, String sampleId) {
        ConsensusSequence consensusSequence = sequenceSamples.stream()
            .filter(Objects::nonNull)
            .filter(sample -> sample.getSampleId().equals(sampleId))
            .map(sample -> sample.getTyping())
            .flatMap(typings -> typings.stream())
            .collect(Collectors.toList()).stream()
                .filter(Objects::nonNull)
                .map(typing -> typing.getConsensusSequence())
                .findFirst().get();
        List<ConsensusSequenceBlock> consensusSequenceBlockList = geneticsPhaseSetSamples.stream()
            .filter(Objects::nonNull)
            .filter(sample -> sample.getSampleId().equals(sampleId))
            .map(sample -> sample.getTyping())
            .flatMap(typings -> typings.stream())
            .collect(Collectors.toList()).stream()
                .filter(Objects::nonNull)
                .map(typing -> typing.getConsensusSequence())
                .collect(Collectors.toList()).stream()
                    .filter(Objects::nonNull)
                    .map(consensusSeq -> consensusSeq.getConsensusSequenceBlocks())
                    .flatMap(consensusSequenceBlock -> consensusSequenceBlock.stream())
                    .collect(Collectors.toList());

        consensusSequence.setConsensusSequenceBlocks(consensusSequenceBlockList);

        return consensusSequence;
    }
}
