package org.nmdp.hmlfhir;

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

import com.google.gson.*;

import org.json.JSONObject;

import org.modelmapper.ModelMapper;

import org.nmdp.hmlfhir.deserialization.Deserializer;
import org.nmdp.hmlfhir.mapping.fhir.*;
import org.nmdp.hmlfhirconvertermodels.domain.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.domain.base.SwaggerConverter;
import org.nmdp.hmlfhirconvertermodels.domain.fhir.*;

import org.apache.log4j.Logger;

import org.nmdp.hmlfhirconvertermodels.domain.fhir.lists.*;

public class ConvertHmlToFhirImpl extends Convert implements ConvertHmlToFhir {

    private static final Logger LOG = Logger.getLogger(ConvertHmlToFhir.class);
    private Deserializer deserializer;

    public ConvertHmlToFhirImpl(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public FhirMessage convert(Hml hml) throws Exception {
        try {
            return toFhir(hml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public FhirMessage convert(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml hml) throws Exception {
        try {
            return toFhir(hml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public FhirMessage convert(JsonObject hml) throws Exception {
        try {
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml = convertToDto(hml);
            return toFhir(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public FhirMessage convert(JSONObject hml, String prefix) throws Exception {
        try {
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml = convertToDto(hml, prefix);
            return toFhir(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public FhirMessage convert(String hml, String prefix) throws Exception {
        try {
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml = convertToDto(hml);
            return toFhir(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public Hml convertToDo(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml hml) throws Exception {
        try {
            return toDomain(hml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public Hml convertToDo(JsonObject hml) throws Exception {
        try {
            Gson gson = getGsonConverter();
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml =
                    gson.fromJson(hml, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);

            return toDomain(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public Hml convertToDo(JSONObject hml, String prefix) throws Exception {
        try {
            JsonParser parser = new JsonParser();
            Gson gson = getGsonConverter();

            if (prefix != null) {
                hml = mutatePropertyNames(hml, prefix);
            }

            Object obj = parser.parse(hml.toString());
            JsonObject json = (JsonObject) obj;
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml =
                    gson.fromJson(json, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);

            return toDomain(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public Hml convertToDo(String hml) throws Exception {
        try {
            String prefix = getPrefixFromXml(hml);
            JSONObject jsonObj = convertXmlStringToJson(hml);
            JsonParser parser = new JsonParser();
            Gson gson = getGsonConverter();

            LOG.info(String.format("Modifying XML with Prefix remova, prefix: %s", prefix));

            if (prefix == null) {
                prefix = "";
            }

            jsonObj = mutatePropertyNames(jsonObj, prefix);
            Object obj = parser.parse(jsonObj.toString());
            JsonObject json = (JsonObject) obj;
            org.nmdp.hmlfhirconvertermodels.dto.hml.Hml javaHml =
                    gson.fromJson(json, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);

            return toDomain(javaHml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.dto.hml.Hml convertToDto(Hml hml) throws Exception {
        try {
            return toDto(hml);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.dto.hml.Hml convertToDto(JsonObject hml) throws Exception {
        try {
            LOG.info(String.format("Converting hml json to java object\n%s", hml.toString()));
            Gson gson = getGsonConverter();
            return gson.fromJson(hml, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.dto.hml.Hml convertToDto(JSONObject hml, String prefix) throws Exception {
        try {
            JsonParser parser = new JsonParser();
            Gson gson = getGsonConverter();

            if (prefix != null) {
                hml = mutatePropertyNames(hml, prefix);
            }

            Object obj = parser.parse(hml.toString());
            JsonObject json = (JsonObject) obj;

            return gson.fromJson(json, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.dto.hml.Hml convertToDto(String hml) throws Exception {
        try {
            String prefix = getPrefixFromXml(hml);
            JSONObject jsonObj = convertXmlStringToJson(hml);
            JsonParser parser = new JsonParser();
            Gson gson = getGsonConverter();

            if (prefix != null) {
                jsonObj = mutatePropertyNames(jsonObj, prefix);
            }

            Object obj = parser.parse(jsonObj.toString());
            JsonObject json = (JsonObject) obj;

            return gson.fromJson(json, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);
        } catch (Exception ex) {
            LOG.error(ex);
            throw (Exception) ex;
        }
    }

    private FhirMessage toFhir(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml hml) {
        ModelMapper mapper = createMapper();
        FhirMessage message = new FhirMessage();
        Organization organization = mapper.map(hml, Organization.class);
        Patients patients = mapper.map(hml, Patients.class);

        message.setOrganization(organization);
        message.setPatients(patients);

        return message;
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(new OrganizationMap());
        mapper.addConverter(new PatientMap());

        return  mapper;
    }

    private FhirMessage toFhir(Hml hml) {
        org.nmdp.hmlfhirconvertermodels.dto.hml.Hml dtoHml = toDto(hml);
        return toFhir(dtoHml);
    }

    private Hml toDomain(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml hml) {
        SwaggerConverter<Hml, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml> converter =
                new SwaggerConverter<>(Hml.class, org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class);
        return converter.convertFromSwagger(hml);
    }

    private org.nmdp.hmlfhirconvertermodels.dto.hml.Hml toDto(Hml hml) {
        return hml.toDto(hml);
    }

    private Gson getGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(org.nmdp.hmlfhirconvertermodels.dto.hml.Hml.class, deserializer);
        return gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
    }
}
