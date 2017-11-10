package org.nmdp.hmlfhir.mapping.object;

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

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.nmdp.hmlfhirconvertermodels.domain.hml.*;

@Mapper
public interface DoDtoMapper {

    DoDtoMapper INSTANCE = Mappers.getMapper(DoDtoMapper.class);

    Allele map(org.nmdp.hmlfhirconvertermodels.dto.hml.Allele value);
    AlleleAssignment map(org.nmdp.hmlfhirconvertermodels.dto.hml.AlleleAssignment value);
    Amplification map(org.nmdp.hmlfhirconvertermodels.dto.hml.Amplification value);
    CollectionMethod map(org.nmdp.hmlfhirconvertermodels.dto.hml.CollectionMethod value);
    ConsensusSequence map(org.nmdp.hmlfhirconvertermodels.dto.hml.ConsensusSequence value);
    ConsensusSequenceBlock map(org.nmdp.hmlfhirconvertermodels.dto.hml.ConsensusSequenceBlock value);
    DiploidCombination map(org.nmdp.hmlfhirconvertermodels.dto.hml.DiploidCombination value);
    ExtendedItem map(org.nmdp.hmlfhirconvertermodels.dto.hml.ExtendedItem value);
    Genotype map(org.nmdp.hmlfhirconvertermodels.dto.hml.Genotype value);
    GlString map(org.nmdp.hmlfhirconvertermodels.dto.hml.GlString value);
    Gssp map(org.nmdp.hmlfhirconvertermodels.dto.hml.Gssp value);
    Haploid map(org.nmdp.hmlfhirconvertermodels.dto.hml.Haploid value);
    Hml map(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml value);
    HmlId map(org.nmdp.hmlfhirconvertermodels.dto.hml.HmlId value);
    IupacBases map(org.nmdp.hmlfhirconvertermodels.dto.hml.IupacBases value);
    LocusBlock map(org.nmdp.hmlfhirconvertermodels.dto.hml.LocusBlock value);
    Project map(org.nmdp.hmlfhirconvertermodels.dto.hml.Project value);
    Property map(org.nmdp.hmlfhirconvertermodels.dto.hml.Property value);
    RawRead map(org.nmdp.hmlfhirconvertermodels.dto.hml.RawRead value);
    ReferenceDatabase map(org.nmdp.hmlfhirconvertermodels.dto.hml.ReferenceDatabase value);
    ReferenceSequence map(org.nmdp.hmlfhirconvertermodels.dto.hml.ReferenceSequence value);
    ReportingCenter map(org.nmdp.hmlfhirconvertermodels.dto.hml.ReportingCenter value);
    Sample map(org.nmdp.hmlfhirconvertermodels.dto.hml.Sample value);
    SbtNgs map(org.nmdp.hmlfhirconvertermodels.dto.hml.SbtNgs value);
    SbtSanger map(org.nmdp.hmlfhirconvertermodels.dto.hml.SbtSanger value);
    Sequence map(org.nmdp.hmlfhirconvertermodels.dto.hml.Sequence value);
    SequenceQuality map(org.nmdp.hmlfhirconvertermodels.dto.hml.SequenceQuality value);
    Sso map(org.nmdp.hmlfhirconvertermodels.dto.hml.Sso value);
    Ssp map(org.nmdp.hmlfhirconvertermodels.dto.hml.Ssp value);
    SubAmplification map(org.nmdp.hmlfhirconvertermodels.dto.hml.SubAmplification value);
    Typing map(org.nmdp.hmlfhirconvertermodels.dto.hml.Typing value);
    TypingMethod map(org.nmdp.hmlfhirconvertermodels.dto.hml.TypingMethod value);
    TypingTestName map(org.nmdp.hmlfhirconvertermodels.dto.hml.TypingTestName value);
    Variant map(org.nmdp.hmlfhirconvertermodels.dto.hml.Variant value);
    VariantEffect map(org.nmdp.hmlfhirconvertermodels.dto.hml.VariantEffect value);
    Version map(org.nmdp.hmlfhirconvertermodels.dto.hml.Version value);
}
