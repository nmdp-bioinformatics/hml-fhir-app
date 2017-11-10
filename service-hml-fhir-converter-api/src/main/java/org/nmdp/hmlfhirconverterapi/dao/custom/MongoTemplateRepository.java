package org.nmdp.hmlfhirconverterapi.dao.custom;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/26/17.
 * <p>
 * service-hml-fhir-converter-api
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

import org.apache.log4j.Logger;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public abstract class MongoTemplateRepository<T> {

    private final Class<T> tClass;
    private final MongoOperations mongoOperations;

    private static final Logger LOG = Logger.getLogger(MongoTemplateRepository.class);

    public MongoTemplateRepository(Class<T> tClass, MongoOperations mongoOperations) {
        this.tClass = tClass;
        this.mongoOperations = mongoOperations;
    }

    public List<T> findByQuery(Query query) {
        return mongoOperations.find(query, tClass);
    }

    public T findSingleByQuery(Query query) { return mongoOperations.findOne(query, tClass); }
}
