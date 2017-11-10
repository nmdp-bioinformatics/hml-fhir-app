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
import org.nmdp.hmlfhir.mapping.Distinct;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.Haploid;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.Haploids;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;
import org.nmdp.hmlfhirconvertermodels.lists.Samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HaploidMap implements Converter<FhirMessage, Samples> {

    @Override
    public Samples convert(MappingContext<FhirMessage, Samples> context) {
        if (context.getSource() == null) {
            return null;
        }

        Samples samples = new Samples();
        List<Sample> sampleList = new ArrayList<>();
        FhirMessage fhir = context.getSource();
//        Haploids haploids = fhir.getHaploids();
//
//        for (Haploid haploid : haploids.getHaploids()) {
//            org.nmdp.hmlfhirconvertermodels.dto.Haploid nmdpHaploid = new org.nmdp.hmlfhirconvertermodels.dto.Haploid();
//            AlleleAssignment alleleAssignment = new AlleleAssignment();
//            Typing typing = new Typing();
//            Sample sample = new Sample();
//
//            nmdpHaploid.setType(haploid.getHaploidType());
//            nmdpHaploid.setMethod(haploid.getMethod());
//            nmdpHaploid.setLocus(haploid.getLocus());
//            alleleAssignment.setHaploid(Arrays.asList(nmdpHaploid));
//            typing.setAlleleAssignment(Arrays.asList(alleleAssignment));
//            sample.setTyping(Arrays.asList(typing));
//            sampleList.add(sample);
//        }

        samples.setSamples(sampleList);

        return samples;
    }
}
