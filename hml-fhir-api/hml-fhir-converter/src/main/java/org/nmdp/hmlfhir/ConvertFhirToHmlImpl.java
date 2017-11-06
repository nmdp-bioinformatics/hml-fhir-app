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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import org.modelmapper.ModelMapper;
import org.nmdp.hmlfhir.deserialization.Deserializer;
import org.nmdp.hmlfhir.mapping.hml.*;
import org.nmdp.hmlfhir.mapping.object.HmlMessageToHml;
import org.nmdp.hmlfhirconvertermodels.HmlMessage;
import org.nmdp.hmlfhirconvertermodels.dto.fhir.FhirMessage;
import org.nmdp.hmlfhirconvertermodels.dto.hml.Hml;
import org.nmdp.hmlfhirconvertermodels.dto.hml.HmlId;
import org.nmdp.hmlfhirconvertermodels.lists.*;

public class ConvertFhirToHmlImpl extends Convert implements ConvertFhirToHml {

    private static final Logger LOG = Logger.getLogger(ConvertFhirToHml.class);
    private Deserializer deserializer;

    public ConvertFhirToHmlImpl(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    public ConvertFhirToHmlImpl() {

    }

    @Override
    public Hml convert(FhirMessage fhir) throws Exception {
        try {
            return toHml(fhir);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public Hml convert(JsonObject fhir) throws Exception {
        try {
            FhirMessage javaFhir = toDto(fhir);
            return toHml(javaFhir);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public Hml convert(JSONObject fhir, String prefix) throws Exception {
        try {
            FhirMessage javaFhir = toDto(fhir, prefix);
            return toHml(javaFhir);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public Hml convert(String fhir, String prefix) throws Exception {
        try {
            FhirMessage javaFhir = toDto(fhir, prefix);
            return toHml(javaFhir);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public FhirMessage toDto(JSONObject fhir, String prefix) {
        JsonParser parser = new JsonParser();
        Gson gson = getGsonConverter();

        if (prefix != null) {
            fhir = mutatePropertyNames(fhir, prefix);
        }

        Object obj = parser.parse(fhir.toString());
        JsonObject json = (JsonObject) obj;
        return gson.fromJson(json, FhirMessage.class);
    }

    @Override
    public FhirMessage toDto(String fhir, String prefix) throws Exception {
        try {
            JSONObject jsonObj = convertXmlStringToJson(fhir);
            return toDto(jsonObj, prefix);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    @Override
    public FhirMessage toDto(JsonObject fhir) {
        Gson gson = getGsonConverter();
        return gson.fromJson(fhir, FhirMessage.class);
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(JSONObject fhir, String prefix) {
        JsonParser parser = new JsonParser();
        Gson gson = getGsonConverter();

        if (prefix != null) {
            fhir = mutatePropertyNames(fhir, prefix);
        }

        Object obj = parser.parse(fhir.toString());
        JsonObject json = (JsonObject) obj;
        return gson.fromJson(json, org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage.class);
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(JsonObject fhir) {
        Gson gson = getGsonConverter();
        return gson.fromJson(fhir, org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage.class);
    }

    @Override
    public org.nmdp.hmlfhirconvertermodels.domain.fhir.FhirMessage toDomain(String fhir, String prefix) throws Exception{
        try {
            JSONObject jsonObj = convertXmlStringToJson(fhir);
            return toDomain(jsonObj, prefix);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    private Hml toHml(FhirMessage fhir) {
        ModelMapper mapper = createMapper();
        HmlMessage hmlMessage = new HmlMessage();

        hmlMessage.setAlleleDatabaseSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setAlleleNameSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setHmlId(mapper.map(fhir, HmlId.class));
        hmlMessage.setGeneticsPhaseSetSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setGenotypingResultsHaploidSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setGenotypingResultsMethodSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setGlStringSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setHaploidSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setOrganizationReportingCenters(mapper.map(fhir, ReportingCenters.class));
        hmlMessage.setPatientHml(mapper.map(fhir, Hml.class));
        hmlMessage.setSbtNgss(mapper.map(fhir, SbtNgss.class));
        hmlMessage.setSequenceSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setObservationSamples(mapper.map(fhir, Samples.class));
        hmlMessage.setSsos(mapper.map(fhir, Ssos.class));
        hmlMessage.setSsps(mapper.map(fhir, Ssps.class));

        return HmlMessageToHml.toDto(hmlMessage);
    }

    private ModelMapper createMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(new AlleleDatabaseMap());
        mapper.addConverter(new AlleleNameMap());
        mapper.addConverter(new DiagnosticReportMap());
        mapper.addConverter(new GeneticsPhaseSetMap());
        mapper.addConverter(new GenotypingResultsHaploidMap());
        mapper.addConverter(new GenotypingResultsMethodMap());
        mapper.addConverter(new GlStringMap());
        mapper.addConverter(new HaploidMap());
        mapper.addConverter(new ObservationMap());
        mapper.addConverter(new OrganizationMap());
        mapper.addConverter(new PatientMap());
        mapper.addConverter(new SbtNgsMap());
        mapper.addConverter(new SequenceMap());
        mapper.addConverter(new SsoMap());
        mapper.addConverter(new SspMap());

        return mapper;
    }

    private Gson getGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (deserializer != null) {
            gsonBuilder.registerTypeAdapter(FhirMessage.class, deserializer);
        }

        return gsonBuilder.create();
    }
}
