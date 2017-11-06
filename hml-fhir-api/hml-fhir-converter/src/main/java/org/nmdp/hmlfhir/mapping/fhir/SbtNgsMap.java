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
import org.nmdp.hmlfhirconvertermodels.domain.fhir.SbtNgs;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.SbtNgss;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Typing;
import org.nmdp.hmlfhirconvertermodels.dto.hml.TypingMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SbtNgsMap implements Converter<Typing, SbtNgss> {

    @Override
    public SbtNgss convert(MappingContext<Typing, SbtNgss> context) {
        if (context.getSource() == null) {
            return null;
        }

        SbtNgss sbtNgss = new SbtNgss();
        List<SbtNgs> sbtNgsList = new ArrayList<>();
        Typing typing = context.getSource();
        SbtNgs sbtNgs = new SbtNgs();
        TypingMethod typingMethod = typing.getTypingMethod();

        for (org.nmdp.hmlfhirconvertermodels.dto.hml.SbtNgs nmdpSbtNgs : typingMethod.getSbtNgs()) {
            sbtNgs.setLocus(nmdpSbtNgs.getLocus());
            sbtNgsList.add(sbtNgs);
        }

        sbtNgss.setSbtNgss(sbtNgsList.stream()
                .filter(Objects::nonNull)
                .filter(Distinct.distinctByKey(s -> s.getLocus()))
                .filter(s -> s.hasValue())
                .collect(Collectors.toList()));

        return sbtNgss;
    }
}
