package org.nmdp.hmlfhirmongo.mongo;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/1/17.
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

import org.nmdp.hmlfhirmongo.config.MongoConfiguration;
import org.nmdp.hmlfhirmongo.models.FhirSubmission;

public class MongoFhirSubmissionDatabase extends MongoDatabase {

    private final MongoCollection<Document> collection;

    public MongoFhirSubmissionDatabase(MongoConfiguration config) {
        super(config.getConnectionString(), config.getPort(), config.getDatabaseName());
        collection = super.database.getCollection("fhirSubmission");
    }

    public FhirSubmission save(FhirSubmission fhirSubmission) {
        Document document = toDocument(fhirSubmission);
        collection.insertOne(document);
        fhirSubmission.setId(document.get("_id").toString());
        return fhirSubmission;
    }

    public Document get(String id) {
        return collection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    public String toJson(FhirSubmission fhirSubmission) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(fhirSubmission);
    }

    private Document toDocument(FhirSubmission fhirSubmission) {
        return Document.parse(toJson(fhirSubmission));
    }
}
