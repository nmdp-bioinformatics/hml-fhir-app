package org.nmdp.hmlfhirconverterapi.util;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/22/17.
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;
import org.apache.log4j.Logger;
import org.bson.Document;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class Serializer {

    private static final Logger LOG = Logger.getLogger(Serializer.class);

    public static <T> String toJson(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> String toJson(T object, String id) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement json = gson.toJsonTree(object);
        JsonObject jsonObject = (JsonObject) json;
        jsonObject.addProperty("fhirId", id);

        return gson.toJson(jsonObject);
    }

    public static String toXml(Document document) {
        try {
            StringWriter stringWriter = new StringWriter();
            InputStream stream = new ByteArrayInputStream(document.toJson().getBytes());
            JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).prettyPrint(true).build();
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(stream);
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(stringWriter);

            writer = new PrettyXMLEventWriter(writer);
            writer.add(reader);

            return stringWriter.getBuffer().toString();
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }
}
