package org.nmdp.hmlfhirconvertermodels.domain.hml;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/26/17.
 * <p>
 * service-hml-fhir-converter-models
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

import org.nmdp.hmlfhirconvertermodels.domain.base.SwaggerConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Document(collection = "Hml.Samples.Typings")
public class Typing extends SwaggerConverter<Typing, org.nmdp.hmlfhirconvertermodels.dto.hml.Typing> implements Serializable, ICascadable {

    private static Class<Typing> tClass = Typing.class;
    private static Class<org.nmdp.hmlfhirconvertermodels.dto.hml.Typing> uClass = org.nmdp.hmlfhirconvertermodels.dto.hml.Typing.class;

    public Typing() {
        super(tClass, uClass);
    }

    public Typing(Boolean active) {
        super (tClass, uClass);
        this.active = active;
        this.dateUpdated = null;
        this.properties = new ArrayList<>();
        this.alleleAssignment = new ArrayList<>();
        this.typingMethod = new TypingMethod(true);
        this.consensusSequence = new ConsensusSequence(true);
    }

    @XmlAttribute
    @Id
    private String id;

    @XmlAttribute
    private String geneFamily;

    @XmlAttribute
    private Date date;

    @XmlAttribute
    private List<Property> properties;

    @XmlAttribute
    private List<AlleleAssignment> alleleAssignment;

    @XmlAttribute
    private TypingMethod typingMethod;

    @XmlAttribute
    private ConsensusSequence consensusSequence;

    @XmlAttribute
    private Boolean active;

    @XmlAttribute
    private Date dateCreated;

    @XmlAttribute
    private Date dateUpdated;
}
