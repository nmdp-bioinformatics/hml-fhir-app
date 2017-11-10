package org.nmdp.hmlfhirmongo.mongo;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/2/17.
 * <p>
 * hml-fhir-mongo
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage;
import org.nmdp.hmlfhirmongo.config.MongoConfiguration;

public class MongoFhirDatabase extends MongoDatabase {

    private final MongoCollection<Document> collection;

    public MongoFhirDatabase(MongoConfiguration config) {
        super(config.getConnectionString(), config.getPort(), config.getDatabaseName());
        collection = super.database.getCollection("fhir");
    }

    public FhirMessage save(FhirMessage fhir) {
        Document document = toDocument(fhir);
        collection.insertOne(document);
        fhir.setId(document.get("_id").toString());
        return fhir;
    }

    public org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage save(org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage fhir) {
        Document document = toDocument(fhir);
        collection.insertOne(document);
        fhir.setId(document.get("_id").toString());
        return fhir;
    }
    
    public Document get(String id) {
        return collection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    public String toJson(FhirMessage fhir) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(fhir);
    }

    public String toJson(org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage fhir) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(fhir);
    }

    private Document toDocument(FhirMessage fhir) {
        return Document.parse(toJson(fhir));
    }

    private Document toDocument(org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage fhir) {
        return Document.parse(toJson(fhir));
    }
}
