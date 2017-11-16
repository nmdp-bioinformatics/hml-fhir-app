package org.nmdp.hmlfhir;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 6/6/17.
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


import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

public abstract class Convert {

    private static final Logger LOG = Logger.getLogger(Convert.class);
    private static final String HML = "hml";
    private static final Character COLON = ':';
    private static final String COLON_STR = ":";

    protected JSONObject convertXmlStringToJson(String xml) throws Exception {
        try {
            return XML.toJSONObject(xml);
        } catch (Exception ex) {
            LOG.error("Error parsing Xml to Json.", ex);
            throw ex;
        }
    }

    protected String getPrefixFromXml(String xml) {
        JSONObject json = XML.toJSONObject(xml);
        Set<String> keySet = json.keySet();
        String prefix = "";

        for (String key : keySet) {
            String[] segments = key.split(COLON_STR);

            for (int i = 0; i < segments.length; i++) {
                String segment = segments[i];

                if (segment.charAt(0) == COLON) {
                    segment = segment.substring(1, segment.length());
                    prefix += COLON;
                }

                if (segment.contains(HML)) {
                    return prefix;
                }

                prefix += String.format("%s%s", segment, COLON_STR);
            }
        }

        return prefix;
    }

    protected JSONObject mutatePropertyNames(JSONObject json, String prefix) {
        JSONObject mutatedJson = new JSONObject();
        Iterator<String> jsonIterator = json.keys();

        while (jsonIterator.hasNext()) {
            String key = jsonIterator.next();
            Object property = json.get(key);

            if (property instanceof JSONObject) {
                JSONObject mutatedProperty = mutatePropertyNames((JSONObject) property, prefix);
                mutatedJson.put(key.replace(prefix, ""), mutatedProperty);
                continue;
            } else if (property instanceof JSONArray) {
                JSONArray arrayProperty = (JSONArray)property;
                JSONArray mutatedJsonArray = new JSONArray();

                for (int i = 0; i < arrayProperty.length(); i++) {
                    JSONObject obj = arrayProperty.getJSONObject(i);
                    JSONObject mutatedObj = mutatePropertyNames(obj, prefix);

                    mutatedJsonArray.put(mutatedObj);
                }

                mutatedJson.put(key.replace(prefix, ""), mutatedJsonArray);
                continue;
            }

            mutatedJson.put(key.replace(prefix, ""), property);
        }

        return mutatedJson;
    }

    protected Boolean isValidXml(String xml) {
        Boolean valid = false;

        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            xmlReader.parse(new InputSource(new StringReader(xml)));
            valid = true;
        } catch (Exception ex) {
            LOG.error("General exception validating XML.", ex);
        } finally {
            return valid;
        }
    }
}
