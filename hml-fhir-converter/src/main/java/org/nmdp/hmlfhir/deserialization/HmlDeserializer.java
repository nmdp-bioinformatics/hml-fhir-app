package org.nmdp.hmlfhir.deserialization;

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
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class HmlDeserializer extends Deserializer<Hml> {
    
    @Override
    public Hml deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootJson = json.getAsJsonObject();
        JsonObject jsonObject = rootJson.get("hml").getAsJsonObject();

        Hml hml = new Hml();

        hml.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        hml.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        hml.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        hml.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        hml.setHmlId(createHmlId(jsonObject.has("hmlId") ? jsonObject.get("hmlId").getAsJsonObject() : null));
        hml.setVersion(handleVersion(jsonObject.has("version") ? jsonObject.get("version") : null));
        hml.setProject(createProject(jsonObject.has("project") ? jsonObject.get("project").getAsJsonObject() : null));
        hml.setSamples(handleSamples(jsonObject.has("samples") ? jsonObject.get("samples") : null));
        hml.setReportingCenters(handleReportingCenters(jsonObject.has("reportingCenters") ? jsonObject.get("reportingCenters") : null));
        hml.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        hml.setTypingTestNames(createTypingTestNames(jsonObject.has("typingTestName") ? jsonObject.get("typingTestName").getAsJsonObject() : null));

        return hml;
    }

    private Boolean handleActive(JsonObject json) {
        if (json.get("active") instanceof JsonNull) {
            return null;
        }

        return Boolean.parseBoolean(json.get("active").getAsString());
    }
    
    private Date handleDateCreated(JsonObject json) {
        if (json.get("dateCreated") instanceof JsonNull) {
            return null;
        }

        return new Date(json.get("dateCreated").getAsString());
    }

    private Date handleDateUpdated(JsonObject json) {
        if (json.get("dateUpdated") instanceof JsonNull) {
            return null;
        }

        return new Date(json.get("dateUpdated").getAsString());
    }
    
    private String handleId(JsonObject json) {
        if (json.get("id") instanceof JsonNull) {
            return null;
        }
        
        return json.get("id").getAsString();
    }

    private Version handleVersion(Object object) {
        Version version = new Version();

        if (object == null) {
            return version;
        }

        if (object instanceof JsonObject) {
            JsonObject json = (JsonObject) object;
            version = createVersion(json.has("name") ? json.get("name") instanceof JsonNull ? null : json.get("name").getAsString() : null);
        } else if (object instanceof String) {
            version = createVersion((String)object);
        }

        return version;
    }

    private Version createVersion(String versionName) {
        Version version = new Version();

        if (versionName == null) {
            return version;
        }

        version.setName(versionName);
        return version;
    }

    private HmlId createHmlId(JsonObject jsonObject) {
        HmlId hmlId = new HmlId();

        if (jsonObject == null) {
            return hmlId;
        }

        hmlId.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        hmlId.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        hmlId.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        hmlId.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        hmlId.setExtension(jsonObject.has("extension") ? jsonObject.get("extension").getAsString() : null);
        hmlId.setRootName(jsonObject.has("rootName") ? jsonObject.get("rootName").getAsString() : null);

        return hmlId;
    }

    private Project createProject(JsonObject jsonObject) {
        Project project = new Project();

        if (jsonObject == null) {
            return project;
        }

        project.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        project.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        project.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        project.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        project.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        project.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);

        return project;
    }

    private List<Sample> handleSamples(Object object) {
        List<Sample> samples = new ArrayList<>();

        if (object == null) {
            return samples;
        }

        if (object instanceof JsonObject) {
            samples = createSamples((JsonObject)object);
        } else if (object instanceof JsonArray) {
            samples = createSamples((JsonArray)object);
        }

        return samples;
    }

    private List<Sample> createSamples(JsonObject jsonObject) {
        List<Sample> samples = new ArrayList<>();
        Sample sample = new Sample();

        if (jsonObject == null) {
            return samples;
        }

        sample.setId(jsonObject.has("_id") ? jsonObject.get("_id").getAsString() : null);
        sample.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sample.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sample.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        sample.setCenterCode(jsonObject.has("centerCode") ? jsonObject.get("centerCode").getAsString() : null);
        sample.setSampleId(jsonObject.has("sampleId") ? jsonObject.get("sampleId").getAsString() : null);
        sample.setTyping(handleTyping(jsonObject.has("typing") ? jsonObject.get("typing") : null));
        sample.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        sample.setCollectionMethods(handleCollectionMethods(jsonObject.has("collectionMethod") ? jsonObject.get("collectionMethod") : null));

        samples.add(sample);

        return samples;
    }

    private List<Sample> createSamples(JsonArray jsonArray) {
        List<Sample> samples = new ArrayList<>();

        if (jsonArray == null) {
            return samples;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            samples.addAll(createSamples((JsonObject)jsonArray.get(i)));
        }

        return samples;
    }

    private List<Property> handleProperties(Object object) {
        List<Property> properties = new ArrayList<>();

        if (object == null) {
            return properties;
        }

        if (object instanceof JsonObject) {
            properties = createProperties((JsonObject)object);
        } else if (object instanceof JsonArray) {
            properties = createProperties((JsonArray)object);
        }

        return properties;
    }

    private List<Property> createProperties(JsonArray jsonArray) {
        List<Property> properties = new ArrayList<>();

        if (jsonArray == null) {
            return properties;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            properties.addAll(createProperties((JsonObject)jsonArray.get(i)));
        }

        return properties;
    }

    private List<Property> createProperties(JsonObject jsonObject) {
        List<Property> properties = new ArrayList<>();
        Property property = new Property();

        if (jsonObject == null) {
            return properties;
        }

        property.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        property.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        property.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        property.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        property.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        property.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);
        property.setValue(jsonObject.has("value") ? jsonObject.get("value").getAsString() : null);
        property.setExtendedItems(handleExtendedItems(jsonObject.has("extendedItems") ? jsonObject.get("extendedItems") : null));

        properties.add(property);
        return properties;
    }

    private List<ReportingCenter> handleReportingCenters(Object object) {
        List<ReportingCenter> reportingCenters = new ArrayList<>();

        if (object == null) {
            return reportingCenters;
        }

        if (object instanceof JsonObject) {
            reportingCenters = createReportingCenters((JsonObject)object);
        } else if (object instanceof JsonArray) {
            reportingCenters = createReportingCenters((JsonArray)object);
        }

        return reportingCenters;
    }
    
    private List<ReportingCenter> createReportingCenters(JsonArray jsonArray) {
        List<ReportingCenter> reportingCenters = new ArrayList<>();

        if (jsonArray == null) {
            return reportingCenters;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            reportingCenters.addAll(createReportingCenters((JsonObject)jsonArray.get(i)));
        }

        return reportingCenters;
    }
    
    private List<ReportingCenter> createReportingCenters(JsonObject jsonObject) {
        List<ReportingCenter> reportingCenters = new ArrayList<>();
        ReportingCenter reportingCenter = new ReportingCenter();

        if (jsonObject == null) {
            return reportingCenters;
        }

        reportingCenter.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        reportingCenter.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        reportingCenter.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        reportingCenter.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        reportingCenter.setContext(jsonObject.has("context") ? jsonObject.get("context").getAsString() : null);
        reportingCenter.setCenterId(jsonObject.has("centerId") ? jsonObject.get("centerId").getAsString() : null);

        reportingCenters.add(reportingCenter);
        return reportingCenters;
    }

    private List<TypingTestName> createTypingTestNames(JsonObject jsonObject) {
        List<TypingTestName> typingTestNames = new ArrayList<>();
        TypingTestName typingTestName = new TypingTestName();

        if (jsonObject == null) {
            return typingTestNames;
        }

        typingTestName.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        typingTestName.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        typingTestName.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        typingTestName.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        typingTestName.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        typingTestName.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);

        typingTestNames.add(typingTestName);
        return typingTestNames;
    }

    private List<ExtendedItem> handleExtendedItems(Object object) {
        List<ExtendedItem> extendedItems = new ArrayList<>();

        if (object == null) {
            return extendedItems;
        }

        if (object instanceof JsonObject) {
            extendedItems = createExtendedItems((JsonObject) object);
        } else if (object instanceof JsonArray) {
            extendedItems = createExtendedItems((JsonArray) object);
        }

        return extendedItems;
    }

    private List<ExtendedItem> createExtendedItems(JsonArray jsonArray) {
        List<ExtendedItem> extendedItems = new ArrayList<>();

        if (jsonArray == null) {
            return extendedItems;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            extendedItems.addAll(createExtendedItems((JsonObject)jsonArray.get(i)));
        }

        return extendedItems;
    }

    private List<ExtendedItem> createExtendedItems(JsonObject jsonObject) {
        List<ExtendedItem> extendedItems = new ArrayList<>();
        ExtendedItem extendedItem = new ExtendedItem();

        if (jsonObject == null) {
            return extendedItems;
        }

        extendedItem.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        extendedItem.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        extendedItem.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        extendedItem.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        extendedItem.setItem(jsonObject.has("item") ? jsonObject.get("item").getAsJsonObject() : null);

        extendedItems.add(extendedItem);
        return extendedItems;
    }

    private List<Typing> handleTyping(Object object) {
        List<Typing> typings = new ArrayList<>();

        if (object == null) {
            return typings;
        }

        if (object instanceof JsonObject) {
            typings = createTyping((JsonObject) object);
        } else if (object instanceof JsonArray) {
            typings = createTyping((JsonArray) object);
        }

        return typings;
    }

    private List<Typing> createTyping(JsonArray jsonArray) {
        List<Typing> typings = new ArrayList<>();

        if (jsonArray == null) {
            return typings;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            typings.addAll(createTyping((JsonObject)jsonArray.get(i)));
        }

        return typings;
    }

    private List<Typing> createTyping(JsonObject jsonObject) {
        List<Typing> typingList = new ArrayList<>();
        Typing typing = new Typing();

        if (jsonObject == null) {
            return typingList;
        }

        typing.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        typing.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        typing.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        typing.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        typing.setDate(jsonObject.has("date") ? new Date(jsonObject.get("date").getAsString()) : null);
        typing.setGeneFamily(jsonObject.has("geneFamily") ? jsonObject.get("geneFamily").getAsString() : null);
        typing.setTypingMethod(createTypingMethod(jsonObject.has("typingMethod") ? jsonObject.get("typingMethod").getAsJsonObject() : null));
        typing.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        typing.setAlleleAssignment(handleAlleleAssignment(jsonObject.has("alleleAssignment") ? jsonObject.get("alleleAssignment") : null));
        typing.setConsensusSequence(createConsensusSequence(jsonObject.has("consensusSequence") ? jsonObject.get("consensusSequence").getAsJsonObject() : null));

        typingList.add(typing);

        return typingList;
    }

    private ConsensusSequence createConsensusSequence(JsonObject jsonObject) {
        ConsensusSequence consensusSequence = new ConsensusSequence();

        if (jsonObject == null) {
            return consensusSequence;
        }

        consensusSequence.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        consensusSequence.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        consensusSequence.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        consensusSequence.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        consensusSequence.setDate(jsonObject.has("date") ? new Date(jsonObject.get("date").getAsString()) : null);
        consensusSequence.setReferenceDatabase(handleReferenceDatabase(jsonObject.has("referenceDatabase") ? jsonObject.get("referenceDatabase") : null));
        consensusSequence.setConsensusSequenceBlocks(handleConsensusSequenceBlocks(jsonObject.has("consensusSequenceBlocks") ? jsonObject.get("consensusSequenceBlocks") : null));

        return consensusSequence;
    }

    private List<ConsensusSequenceBlock> handleConsensusSequenceBlocks(Object object) {
        List<ConsensusSequenceBlock> consensusSequenceBlockList = new ArrayList<>();

        if (object == null) {
            return consensusSequenceBlockList;
        }

        if (object instanceof JsonObject) {
            consensusSequenceBlockList = createConsensusSequenceBlocks((JsonObject) object);
        } else if (object instanceof JsonArray) {
            consensusSequenceBlockList = createConsensusSequenceBlocks((JsonArray) object);
        }

        return consensusSequenceBlockList;
    }

    private List<ConsensusSequenceBlock> createConsensusSequenceBlocks(JsonArray jsonArray) {
        List<ConsensusSequenceBlock> consensusSequenceBlocks = new ArrayList<>();

        if (jsonArray == null) {
            return consensusSequenceBlocks;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            consensusSequenceBlocks.addAll(createConsensusSequenceBlocks((JsonObject)jsonArray.get(i)));
        }

        return consensusSequenceBlocks;
    }

    private List<ConsensusSequenceBlock> createConsensusSequenceBlocks(JsonObject jsonObject) {
        List<ConsensusSequenceBlock> consensusSequenceBlockList = new ArrayList<>();
        ConsensusSequenceBlock consensusSequenceBlock = new ConsensusSequenceBlock();

        if (jsonObject == null) {
            return  consensusSequenceBlockList;
        }

        consensusSequenceBlock.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        consensusSequenceBlock.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        consensusSequenceBlock.setContinuity(jsonObject.has("continuity") ? jsonObject.get("continuity").getAsBoolean() : null);
        consensusSequenceBlock.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        consensusSequenceBlock.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        consensusSequenceBlock.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        consensusSequenceBlock.setEnd(jsonObject.has("end") ? jsonObject.get("end").getAsInt() : null);
        consensusSequenceBlock.setExpectedCopyNumber(jsonObject.has("expectedCopyNumber") ? jsonObject.get("expectedCopyNumber").getAsInt() : null);
        consensusSequenceBlock.setPhaseSet(jsonObject.has("phaseSet") ? jsonObject.get("phaseSet").getAsString() : null);
        consensusSequenceBlock.setPhasingGroup(jsonObject.has("phasingGroup") ? jsonObject.get("phasingGroup").getAsString() : null);
        consensusSequenceBlock.setReferenceSequenceId(jsonObject.has("referenceSequenceId") ? jsonObject.get("referenceSequenceId").getAsString() : null);
        consensusSequenceBlock.setSequence(handleSequence(jsonObject.has("sequence") ? jsonObject.get("sequence") : null));
        consensusSequenceBlock.setStart(jsonObject.has("start") ? jsonObject.get("start").getAsInt() : null);
        consensusSequenceBlock.setStrand(jsonObject.has("strand") ? jsonObject.get("strand").getAsString() : null);
        consensusSequenceBlock.setVariant(createVariant(jsonObject.has("variant") ? jsonObject.get("variant").getAsJsonObject() : null));
        consensusSequenceBlock.setSequenceQuality(createSequenceQuality(jsonObject.has("sequenceQuality") ? jsonObject.get("sequenceQuality").getAsJsonObject() : null));

        consensusSequenceBlockList.add(consensusSequenceBlock);

        return consensusSequenceBlockList;
    }

    private Variant createVariant(JsonObject jsonObject) {
        Variant variant = new Variant();

        if (jsonObject == null) {
            return variant;
        }

        variant.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        variant.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        variant.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        variant.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        variant.setAlternateBases(jsonObject.has("alternateBases") ? jsonObject.get("alternateBases").getAsString() : null);
        variant.setAnyAttribute(jsonObject.has("anyAttribute") ? jsonObject.get("anyAttribute").getAsJsonObject() : null);
        variant.setEnd(jsonObject.has("end") ? jsonObject.get("end").getAsInt() : null);
        variant.setFilter(jsonObject.has("filter") ? jsonObject.get("filter").getAsString() : null);
        variant.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);
        variant.setQualityScore(jsonObject.has("qualityScore") ? jsonObject.get("qualityScore").getAsString() : null);
        variant.setReferenceBases(jsonObject.has("referenceBases") ? jsonObject.get("referenceBases").getAsString() : null);
        variant.setStart(jsonObject.has("start") ? jsonObject.get("start").getAsInt() : null);
        variant.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);
        variant.setVariantId(jsonObject.has("variantId") ? jsonObject.get("variantId").getAsString() : null);
        variant.setVariantEffect(createVariantEffect(jsonObject.has("variantEffect") ? jsonObject.get("variantEffect").getAsJsonObject() : null));

        return variant;
    }

    private VariantEffect createVariantEffect(JsonObject jsonObject) {
        VariantEffect variantEffect = new VariantEffect();

        if (jsonObject == null) {
            return variantEffect;
        }

        variantEffect.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        variantEffect.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        variantEffect.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        variantEffect.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        variantEffect.setAnyAttribute(jsonObject.has("anyAttribute") ? jsonObject.get("anyAttribute").getAsJsonObject() : null);
        variantEffect.setHgvs(jsonObject.has("hgvs") ? jsonObject.get("hgvs").getAsString() : null);
        variantEffect.setTerm(jsonObject.has("term") ? jsonObject.get("term").getAsString() : null);
        variantEffect.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);

        return variantEffect;
    }

    private SequenceQuality createSequenceQuality(JsonObject jsonObject) {
        SequenceQuality sequenceQuality = new SequenceQuality();

        if (jsonObject == null) {
            return  sequenceQuality;
        }

        sequenceQuality.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        sequenceQuality.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sequenceQuality.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sequenceQuality.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        sequenceQuality.setQualityScore(jsonObject.has("qualityScore") ? jsonObject.get("qualityScore").getAsString() : null);
        sequenceQuality.setSequenceEnd(jsonObject.has("sequenceEnd") ? jsonObject.get("sequenceEnd").getAsInt() : null);
        sequenceQuality.setSequenceStart(jsonObject.has("sequenceStart") ? jsonObject.get("sequenceStart").getAsInt() : null);

        return sequenceQuality;
    }

    private List<ReferenceDatabase> handleReferenceDatabase(Object object) {
        List<ReferenceDatabase> referenceDatabases = new ArrayList<>();

        if (object == null) {
            return referenceDatabases;
        }

        if (object instanceof JsonObject) {
            referenceDatabases = createReferenceDatabase((JsonObject)object);
        } else if (object instanceof JsonArray) {
            referenceDatabases = createReferenceDatabase((JsonArray)object);
        }

        return referenceDatabases;
    }

    private List<ReferenceDatabase> createReferenceDatabase(JsonArray jsonArray) {
        List<ReferenceDatabase> referenceDatabases = new ArrayList<>();

        if (jsonArray == null) {
            return referenceDatabases;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            referenceDatabases.addAll(createReferenceDatabase((JsonObject)jsonArray.get(i)));
        }

        return referenceDatabases;
    }

    private List<ReferenceDatabase> createReferenceDatabase(JsonObject jsonObject) {
        List<ReferenceDatabase> referenceDatabases = new ArrayList<>();
        ReferenceDatabase referenceDatabase = new ReferenceDatabase();

        if (jsonObject == null) {
            return  referenceDatabases;
        }

        referenceDatabase.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        referenceDatabase.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        referenceDatabase.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        referenceDatabase.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);

        referenceDatabases.add(referenceDatabase);

        return referenceDatabases;
    }

    private List<AlleleAssignment> handleAlleleAssignment(Object object) {
        List<AlleleAssignment> alleleAssignments = new ArrayList<>();

        if (object == null) {
            return alleleAssignments;
        }

        if (object instanceof JsonObject) {
            alleleAssignments = createAlleleAssignment((JsonObject)object);
        } else if (object instanceof JsonArray) {
            alleleAssignments = createAlleleAssignment((JsonArray)object);
        }

        return alleleAssignments;
    }

    private List<AlleleAssignment> createAlleleAssignment(JsonArray jsonArray) {
        List<AlleleAssignment> alleleAssignments = new ArrayList<>();

        if (jsonArray == null) {
            return alleleAssignments;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            alleleAssignments.addAll(createAlleleAssignment((JsonObject)jsonArray.get(i)));
        }

        return alleleAssignments;
    }

    private List<AlleleAssignment> createAlleleAssignment(JsonObject jsonObject) {
        List<AlleleAssignment> alleleAssignments = new ArrayList<>();
        AlleleAssignment alleleAssignment = new AlleleAssignment();

        if (jsonObject == null) {
            return alleleAssignments;
        }

        alleleAssignment.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        alleleAssignment.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        alleleAssignment.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        alleleAssignment.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        alleleAssignment.setDate(jsonObject.has("date") ? new Date(jsonObject.get("date").getAsString()) : null);
        alleleAssignment.setAlleleDb(jsonObject.has("alleleDb") ? jsonObject.get("alleleDb").getAsString() : null);
        alleleAssignment.setAlleleVersion(jsonObject.has("alleleVersion") ? jsonObject.get("alleleVersion").getAsString() : null);
        alleleAssignment.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        alleleAssignment.setGenotypes(handleGenotypes(jsonObject.has("genotypes") ? jsonObject.get("genotypes") : null));
        alleleAssignment.setGlString(createGlString(jsonObject.has("glString") ? jsonObject.get("glString").getAsJsonObject() : null));
        alleleAssignment.setHaploid(handleHaploid(jsonObject.has("haploid") ? jsonObject.get("haploid") : null));

        alleleAssignments.add(alleleAssignment);

        return alleleAssignments;
    }

    private GlString createGlString(JsonObject jsonObject) {
        GlString glstring = new GlString();

        if (jsonObject == null) {
            return glstring;
        }

        glstring.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        glstring.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        glstring.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        glstring.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        glstring.setValue(jsonObject.has("value") ? jsonObject.get("value").getAsString() : null);
        glstring.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);

        return glstring;
    }

    private List<Haploid> handleHaploid(Object object) {
        List<Haploid> haploids = new ArrayList<>();

        if (object == null) {
            return haploids;
        }

        if (object instanceof JsonObject) {
            haploids = createHaploid((JsonObject)object);
        } else if (object instanceof JsonArray) {
            haploids = createHaploid((JsonArray)object);
        }

        return haploids;
    }

    private List<Haploid> createHaploid(JsonArray jsonArray) {
        List<Haploid> haploids = new ArrayList<>();

        if (jsonArray == null) {
            return haploids;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            haploids.addAll(createHaploid((JsonObject)jsonArray.get(i)));
        }

        return haploids;
    }

    private List<Haploid> createHaploid(JsonObject jsonObject) {
        List<Haploid> haploids = new ArrayList<>();
        Haploid haploid = new Haploid();

        if (jsonObject == null) {
            return haploids;
        }

        haploid.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        haploid.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        haploid.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        haploid.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        haploid.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        haploid.setMethod(jsonObject.has("method") ? jsonObject.get("method").getAsString() : null);
        haploid.setType(jsonObject.has("type") ? jsonObject.get("type").getAsString() : null);

        haploids.add(haploid);

        return haploids;
    }

    private List<Genotype> handleGenotypes(Object object) {
        List<Genotype> genotypes = new ArrayList<>();

        if (object == null) {
            return genotypes;
        }

        if (object instanceof JsonObject) {
            genotypes = createGenotypes((JsonObject)object);
        } else if (object instanceof JsonArray) {
            genotypes = createGenotypes((JsonArray)object);
        }

        return genotypes;
    }

    private List<Genotype> createGenotypes(JsonArray jsonArray) {
        List<Genotype> genotypes = new ArrayList<>();

        if (jsonArray == null) {
            return genotypes;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            genotypes.addAll(createGenotypes((JsonObject)jsonArray.get(i)));
        }

        return genotypes;
    }

    private List<Genotype> createGenotypes(JsonObject jsonObject) {
        List<Genotype> genotypes = new ArrayList<>();
        Genotype genotype = new Genotype();

        if (jsonObject == null) {
            return  genotypes;
        }

        genotype.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        genotype.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        genotype.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        genotype.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        genotype.setDiploidCombinations(createDiploidCombinations(jsonObject.has("diploidCombination") ? jsonObject.get("diploidCombination").getAsJsonObject() : null));

        genotypes.add(genotype);
        return genotypes;
    }

    private List<DiploidCombination> createDiploidCombinations(JsonObject jsonObject) {
        List<DiploidCombination> diploidCombinations = new ArrayList<>();
        DiploidCombination diploidCombination = new DiploidCombination();

        if (jsonObject == null) {
            return diploidCombinations;
        }

        diploidCombination.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        diploidCombination.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        diploidCombination.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        diploidCombination.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        diploidCombination.setLocusBlock(createLocusBlock(jsonObject.has("locusBlock") ? jsonObject.get("locusBlock").getAsJsonObject() : null));

        diploidCombinations.add(diploidCombination);
        return diploidCombinations;
    }

    private LocusBlock createLocusBlock(JsonObject jsonObject) {
        LocusBlock locusBlock = new LocusBlock();

        if (jsonObject == null) {
            return locusBlock;
        }

        locusBlock.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        locusBlock.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        locusBlock.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        locusBlock.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        locusBlock.setAlleles(createAlleles(jsonObject.has("alleleList") ? jsonObject.get("alleleList").getAsJsonObject() : null));

        return locusBlock;
    }

    private List<Allele> createAlleles(JsonObject jsonObject) {
        List<Allele> alleles = new ArrayList<>();
        Allele allele = new Allele();

        if (jsonObject == null) {
            return alleles;
        }

        allele.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        allele.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        allele.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        allele.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        allele.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);
        allele.setPresent(jsonObject.has("present") ? jsonObject.get("present").getAsString() : null);

        alleles.add(allele);
        return alleles;
    }

    private TypingMethod createTypingMethod(JsonObject jsonObject) {
        TypingMethod typingMethod = new TypingMethod();

        if (jsonObject == null) {
            return typingMethod;
        }

        typingMethod.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        typingMethod.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        typingMethod.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        typingMethod.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        typingMethod.setSbtSanger(createSbtSanger(jsonObject.has("sbtSanger") ? jsonObject.get("sbtSanger").getAsJsonObject() : null));
        typingMethod.setSbtNgs(handleSbtNgs(jsonObject.has("sbtNgs") ? jsonObject.get("sbtNgs") : null));
        typingMethod.setSso(createSso(jsonObject.has("sso") ? jsonObject.get("sso").getAsJsonObject() : null));
        typingMethod.setSsp(createSsp(jsonObject.has("ssp") ? jsonObject.get("ssp").getAsJsonObject() : null));

        return typingMethod;
    }

    private List<SbtNgs> handleSbtNgs(Object object) {
        List<SbtNgs> sbtNgsList = new ArrayList<>();

        if (object == null) {
            return sbtNgsList;
        }

        if (object instanceof JsonObject) {
            sbtNgsList = createSbtNgs((JsonObject)object);
        } else if (object instanceof JsonArray) {
            sbtNgsList = createSbtNgs((JsonArray)object);
        }

        return sbtNgsList;
    }

    private List<SbtNgs> createSbtNgs(JsonArray jsonArray) {
        List<SbtNgs> sbtNgsList = new ArrayList<>();

        if (jsonArray == null) {
            return sbtNgsList;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            sbtNgsList.addAll(createSbtNgs((JsonObject)jsonArray.get(i)));
        }

        return sbtNgsList;
    }

    private List<SbtNgs>createSbtNgs(JsonObject jsonObject) {
        List<SbtNgs> sbtNgsList = new ArrayList<>();
        SbtNgs sbtNgs = new SbtNgs();

        if (jsonObject == null) {
            return sbtNgsList;
        }

        sbtNgs.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        sbtNgs.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sbtNgs.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sbtNgs.setDateUpdated(jsonObject.has("dateUupdated") ? handleDateUpdated(jsonObject) : null);
        sbtNgs.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sbtNgs.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        sbtNgs.setRawReads(handleRawReads(jsonObject.has("rawReads") ? jsonObject.get("rawReads") : null));
        sbtNgs.setTestId(jsonObject.has("testId") ? jsonObject.get("testId").getAsString() : null);
        sbtNgs.setTestIdSource(jsonObject.has("testIdSource") ? jsonObject.get("testIdSource").getAsString() : null);

        sbtNgsList.add(sbtNgs);

        return sbtNgsList;
    }

    private List<RawRead> handleRawReads(Object object) {
        List<RawRead> rawReads = new ArrayList<>();

        if (object == null) {
            return rawReads;
        }

        if (object instanceof JsonObject) {
            rawReads = createRawReads((JsonObject)object);
        } else if (object instanceof JsonArray) {
            rawReads = createRawReads((JsonArray)object);
        }

        return rawReads;
    }

    private List<RawRead> createRawReads(JsonArray jsonArray) {
        List<RawRead> rawReads = new ArrayList<>();

        if (jsonArray == null) {
            return rawReads;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            rawReads.addAll(createRawReads((JsonObject)jsonArray.get(i)));
        }

        return rawReads;
    }

    private List<RawRead> createRawReads(JsonObject jsonObject) {
        List<RawRead> rawReads = new ArrayList<>();
        RawRead rawRead = new RawRead();

        if (jsonObject == null) {
            return rawReads;
        }

        rawRead.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        rawRead.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        rawRead.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        rawRead.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        rawRead.setAdapterTrimmed(jsonObject.has("adapterTrimmed") ? jsonObject.get("adapterTrimmed").getAsBoolean() : null);
        rawRead.setAvailability(jsonObject.has("availability") ? jsonObject.get("availability").getAsString() : null);
        rawRead.setFormat(jsonObject.has("format") ? jsonObject.get("format").getAsString() : null);
        rawRead.setPaired(jsonObject.has("paired") ? jsonObject.get("paired").getAsBoolean() : null);
        rawRead.setPooled(jsonObject.has("pooled") ? jsonObject.get("pooled").getAsBoolean() : null);
        rawRead.setQualityTrimmed(jsonObject.has("qualityTrimmed") ? jsonObject.get("qualityTrimmed").getAsBoolean() : null);
        rawRead.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);

        rawReads.add(rawRead);
        return rawReads;
    }

    private Sso createSso(JsonObject jsonObject) {
        Sso sso = new Sso();

        if (jsonObject == null) {
            return sso;
        }

        sso.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        sso.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sso.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sso.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        sso.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sso.setTestId(jsonObject.has("testId") ? jsonObject.get("testId").getAsString() : null);
        sso.setTestIdSource(jsonObject.has("testIdSource") ? jsonObject.get("testIdSource").getAsString() : null);
        sso.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));

        return sso;
    }

    private Ssp createSsp(JsonObject jsonObject) {
        Ssp ssp = new Ssp();

        if (jsonObject == null) {
            return ssp;
        }

        ssp.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        ssp.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        ssp.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        ssp.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        ssp.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        ssp.setScores(jsonObject.has("scores") ? jsonObject.get("scores").getAsString() : null);
        ssp.setTestId(jsonObject.has("testId") ? jsonObject.get("testId").getAsString() : null);
        ssp.setTestIdSource(jsonObject.has("testIdSource") ? jsonObject.get("testIdSource").getAsString() : null);
        ssp.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));

        return ssp;
    }

    private SbtSanger createSbtSanger(JsonObject jsonObject) {
        SbtSanger sbtSanger = new SbtSanger();

        if (jsonObject == null) {
            return  sbtSanger;
        }

        sbtSanger.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        sbtSanger.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sbtSanger.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sbtSanger.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        sbtSanger.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sbtSanger.setTestId(jsonObject.has("testId") ? jsonObject.get("testId").getAsString() : null);
        sbtSanger.setTestIdSource(jsonObject.has("testIdSource") ? jsonObject.get("testIdSource").getAsString() : null);
        sbtSanger.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        sbtSanger.setAmplification(createAmplification(jsonObject.has("amplification") ? jsonObject.get("amplification").getAsJsonObject() : null));
        sbtSanger.setSubAmplification(createSubAmplification(jsonObject.has("subAmplification") ? jsonObject.get("subAmplification").getAsJsonObject() : null));
        sbtSanger.setGssp(createGssp(jsonObject.has("gssp") ? jsonObject.get("gssp").getAsJsonObject() : null));

        return sbtSanger;
    }

    private Amplification createAmplification(JsonObject jsonObject) {
        Amplification amplification = new Amplification();

        if (jsonObject == null) {
            return amplification;
        }

        amplification.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        amplification.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        amplification.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        amplification.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        amplification.setRegisteredName(jsonObject.has("registeredName") ? jsonObject.get("registeredName").getAsString() : null);
        amplification.setSequence(handleSequence(jsonObject.has("sequence") ? jsonObject.get("sequence") : null));

        return amplification;
    }

    private SubAmplification createSubAmplification(JsonObject jsonObject) {
        SubAmplification subAmplification = new SubAmplification();

        if (jsonObject == null) {
            return subAmplification;
        }

        subAmplification.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        subAmplification.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        subAmplification.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        subAmplification.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        subAmplification.setRegisteredName(jsonObject.has("registeredName") ? jsonObject.get("registeredName").getAsString() : null);
        subAmplification.setSequence(handleSequence(jsonObject.has("sequence") ? jsonObject.get("sequence") : null));

        return subAmplification;
    }

    private Gssp createGssp(JsonObject jsonObject) {
        Gssp gssp = new Gssp();

        if (jsonObject == null) {
            return gssp;
        }

        gssp.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        gssp.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        gssp.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        gssp.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        gssp.setRegisteredName(jsonObject.has("registeredName") ? jsonObject.get("registeredName").getAsString() : null);
        gssp.setPrimerTarget(jsonObject.has("primerTarget") ? jsonObject.get("primerTarget").getAsString() : null);
        gssp.setPrimerSequence(jsonObject.has("primerSequence") ? jsonObject.get("primerSequence").getAsString() : null);
        gssp.setSequence(handleSequence(jsonObject.has("sequence") ? jsonObject.get("sequence") : null));

        return gssp;
    }

    private Sequence handleSequence(Object object) {
        Sequence sequence = new Sequence();

        if (object == null) {
            return sequence;
        }

        if (object instanceof String) {
            sequence = createSequence(object.toString());
        } else if (object instanceof JsonObject) {
            sequence = createSequence((JsonObject)object);
        }

        return sequence;
    }

    private Sequence createSequence(JsonObject jsonObject) {
        Sequence sequence = new Sequence();

        if (jsonObject == null) {
            return sequence;
        }

        sequence.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        sequence.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        sequence.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        sequence.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        sequence.setSequence(jsonObject.has("sequence") ? jsonObject.get("sequence").getAsString() : null);
        sequence.setIupacBases(handleIupacBases(jsonObject.has("iupacBases") ? jsonObject.get("iupacBases") : null));

        return sequence;
    }

    private Sequence createSequence(String seq) {
        Sequence sequence = new Sequence();

        if (seq == null) {
            return sequence;
        }

        sequence.setSequence(seq);

        return sequence;
    }

    private List<IupacBases> handleIupacBases(Object object) {
        List<IupacBases> iupacBases = new ArrayList<>();

        if (object == null) {
            return iupacBases;
        }

        if (object instanceof JsonObject) {
            iupacBases = createIupacBases((JsonObject)object);
        } else if (object instanceof JsonArray) {
            iupacBases = createIupacBases((JsonArray)object);
        }

        return iupacBases;
    }

    private List<IupacBases> createIupacBases(JsonArray jsonArray) {
        List<IupacBases> iupacBases = new ArrayList<>();

        if (jsonArray == null) {
            return iupacBases;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            iupacBases.addAll(createIupacBases((JsonObject)jsonArray.get(i)));
        }

        return iupacBases;
    }

    private List<IupacBases> createIupacBases(JsonObject jsonObject) {
        List<IupacBases> iupacBases = new ArrayList<>();
        IupacBases iupacBase = new IupacBases();

        if (jsonObject == null) {
            return iupacBases;
        }

        iupacBase.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        iupacBase.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        iupacBase.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        iupacBase.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        iupacBase.setProperty(jsonObject.has("property") ? jsonObject.get("property").getAsString() : null);
        iupacBase.setValue(jsonObject.has("value") ? jsonObject.get("value").getAsString() : null);

        iupacBases.add(iupacBase);
        return iupacBases;
    }

    private List<CollectionMethod> handleCollectionMethods(Object object) {
        List<CollectionMethod> collectionMethods = new ArrayList<>();

        if (object instanceof JsonObject) {
            collectionMethods = createCollectionMethods((JsonObject)object);
        } else if (object instanceof String) {
            collectionMethods = createCollectionMethods((String)object);
        }

        return collectionMethods;
    }

    private List<CollectionMethod> createCollectionMethods(String collectionMethodValue) {
        List<CollectionMethod> collectionMethods = new ArrayList<>();
        CollectionMethod collectionMethod = new CollectionMethod();

        if (collectionMethodValue == null) {
            return collectionMethods;
        }

        collectionMethod.setName(collectionMethodValue);
        collectionMethods.add(collectionMethod);

        return collectionMethods;
    }

    private List<CollectionMethod> createCollectionMethods(JsonObject jsonObject) {
        List<CollectionMethod> collectionMethods = new ArrayList<>();
        CollectionMethod collectionMethod = new CollectionMethod();

        if (jsonObject == null) {
            return collectionMethods;
        }

        collectionMethod.setId(jsonObject.has("id") ? handleId(jsonObject) : null);
        collectionMethod.setActive(jsonObject.has("active") ? handleActive(jsonObject) : null);
        collectionMethod.setDateCreated(jsonObject.has("dateCreated") ? handleDateCreated(jsonObject) : null);
        collectionMethod.setDateUpdated(jsonObject.has("dateUpdated") ? handleDateUpdated(jsonObject) : null);
        collectionMethod.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        collectionMethod.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);

        collectionMethods.add(collectionMethod);
        return collectionMethods;
    }
}
