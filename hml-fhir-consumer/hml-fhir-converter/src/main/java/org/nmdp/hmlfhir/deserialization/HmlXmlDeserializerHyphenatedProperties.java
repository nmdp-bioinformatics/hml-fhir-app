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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.nmdp.hmlfhirconvertermodels.dto.hml.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HmlXmlDeserializerHyphenatedProperties extends Deserializer<Hml> {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public Hml deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rootJson = json.getAsJsonObject();
        JsonObject jsonObject = rootJson.get("hml").getAsJsonObject();

        Hml hml = new Hml();

        hml.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        hml.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        hml.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        hml.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        hml.setHmlId(createHmlId(jsonObject.has("hmlid") ? jsonObject.get("hmlid").getAsJsonObject() : null));
        hml.setVersion(handleVersion(jsonObject.has("version") ? jsonObject.get("version") : null));
        hml.setProject(createProject(jsonObject.has("project") ? jsonObject.get("project").getAsJsonObject() : null));
        hml.setSamples(handleSamples(jsonObject.has("sample") ? jsonObject.get("sample") : null));
        hml.setReportingCenters(createRepotingCenters(jsonObject.has("reporting-center") ? jsonObject.get("reporting-center").getAsJsonObject() : null));
        hml.setProperties(handleProperties(jsonObject.has("properties") ? jsonObject.get("properties") : null));
        hml.setTypingTestNames(createTypingTestNames(jsonObject.has("typing-test-name") ? jsonObject.get("typing-test-name").getAsJsonObject() : null));

        return hml;
    }

    private Version handleVersion(Object object) {
        Version version = new Version();

        if (object == null) {
            return version;
        }

        if (object instanceof JsonObject) {
            JsonObject json = (JsonObject) object;
            version = createVersion(json.has("name") ? json.get("name").getAsString() : null);
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

        hmlId.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        hmlId.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        hmlId.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        hmlId.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        hmlId.setExtension(jsonObject.has("extension") ? jsonObject.get("extension").getAsString() : null);
        hmlId.setRootName(jsonObject.has("root") ? jsonObject.get("root").getAsString() : null);

        return hmlId;
    }

    private Project createProject(JsonObject jsonObject) {
        Project project = new Project();

        if (jsonObject == null) {
            return project;
        }

        project.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        project.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        project.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        project.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
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
        sample.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        sample.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        sample.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        sample.setCenterCode(jsonObject.has("center-code") ? jsonObject.get("center-code").getAsString() : null);
        sample.setSampleId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        sample.setTyping(handleTyping(jsonObject.has("typing") ? jsonObject.get("typing") : null));
        sample.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        sample.setCollectionMethods(handleCollectionMethods(jsonObject.has("collection-method") ? jsonObject.get("collection-method") : null));

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

        property.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        property.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        property.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        property.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        property.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        property.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);
        property.setValue(jsonObject.has("value") ? jsonObject.get("value").getAsString() : null);
        property.setExtendedItems(createExtendedItems(jsonObject.has("extended-items") ? jsonObject.get("extended-items").getAsJsonObject() : null));

        properties.add(property);
        return properties;
    }

    private List<ReportingCenter> createRepotingCenters(JsonObject jsonObject) {
        List<ReportingCenter> reportingCenters = new ArrayList<>();
        ReportingCenter reportingCenter = new ReportingCenter();

        if (jsonObject == null) {
            return reportingCenters;
        }

        reportingCenter.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        reportingCenter.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        reportingCenter.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        reportingCenter.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        reportingCenter.setContext(jsonObject.has("context") ? jsonObject.get("context").getAsString() : null);
        reportingCenter.setCenterId(jsonObject.has("center-id") ? jsonObject.get("center-id").getAsString() : null);

        reportingCenters.add(reportingCenter);
        return reportingCenters;
    }

    private List<TypingTestName> createTypingTestNames(JsonObject jsonObject) {
        List<TypingTestName> typingTestNames = new ArrayList<>();
        TypingTestName typingTestName = new TypingTestName();

        if (jsonObject == null) {
            return typingTestNames;
        }

        typingTestName.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        typingTestName.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        typingTestName.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        typingTestName.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        typingTestName.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        typingTestName.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);

        typingTestNames.add(typingTestName);
        return typingTestNames;
    }

    private List<ExtendedItem> createExtendedItems(JsonObject jsonObject) {
        List<ExtendedItem> extendedItems = new ArrayList<>();
        ExtendedItem extendedItem = new ExtendedItem();

        if (jsonObject == null) {
            return extendedItems;
        }

        extendedItem.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        extendedItem.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        extendedItem.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        extendedItem.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
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

        typing.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        typing.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        typing.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        typing.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        typing.setDate(jsonObject.has("date") ? formatter.parseDateTime(jsonObject.get("date").getAsString()).toDate() : null);
        typing.setGeneFamily(jsonObject.has("gene-family") ? jsonObject.get("gene-family").getAsString() : null);
        typing.setTypingMethod(createTypingMethod(jsonObject.has("typing-method") ? jsonObject.get("typing-method").getAsJsonObject() : null));
        typing.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        typing.setAlleleAssignment(handleAlleleAssignment(jsonObject.has("allele-assignment") ? jsonObject.get("allele-assignment") : null));
        typing.setConsensusSequence(createConsensusSequence(jsonObject.has("consensus-sequence") ? jsonObject.get("consensus-sequence").getAsJsonObject() : null));

        typingList.add(typing);

        return typingList;
    }

    private ConsensusSequence createConsensusSequence(JsonObject jsonObject) {
        ConsensusSequence consensusSequence = new ConsensusSequence();

        if (jsonObject == null) {
            return consensusSequence;
        }

        consensusSequence.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        consensusSequence.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        consensusSequence.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        consensusSequence.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        consensusSequence.setDate(jsonObject.has("date") ? formatter.parseDateTime(jsonObject.get("date").getAsString()).toDate() : null);
        consensusSequence.setReferenceDatabase(handleReferenceDatabase(jsonObject.has("reference-database") ? jsonObject.get("reference-database") : null));
        consensusSequence.setConsensusSequenceBlocks(handleConsensusSequenceBlocks(jsonObject.has("consensus-sequence-block") ? jsonObject.get("consensus-sequence-block") : null));

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

        consensusSequenceBlock.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        consensusSequenceBlock.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        consensusSequenceBlock.setContinuity(jsonObject.has("continuity") ? jsonObject.get("continuity").getAsBoolean() : null);
        consensusSequenceBlock.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        consensusSequenceBlock.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        consensusSequenceBlock.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        consensusSequenceBlock.setEnd(jsonObject.has("end") ? jsonObject.get("end").getAsInt() : null);
        consensusSequenceBlock.setExpectedCopyNumber(jsonObject.has("expected-copy-number") ? jsonObject.get("expected-copy-number").getAsInt() : null);
        consensusSequenceBlock.setPhaseSet(jsonObject.has("phase-set") ? jsonObject.get("phase-set").getAsString() : null);
        consensusSequenceBlock.setPhasingGroup(jsonObject.has("phasing-group") ? jsonObject.get("phasing-group").getAsString() : null);
        consensusSequenceBlock.setReferenceSequenceId(jsonObject.has("reference-sequence-id") ? jsonObject.get("reference-sequence-id").getAsString() : null);
        consensusSequenceBlock.setSequence(createSequence(jsonObject.has("sequence") ? jsonObject.get("sequence").getAsString() : null));
        consensusSequenceBlock.setStart(jsonObject.has("start") ? jsonObject.get("start").getAsInt() : null);
        consensusSequenceBlock.setStrand(jsonObject.has("strand") ? jsonObject.get("strand").getAsString() : null);
        consensusSequenceBlock.setVariant(createVariant(jsonObject.has("variant") ? jsonObject.get("variant").getAsJsonObject() : null));
        consensusSequenceBlock.setSequenceQuality(createSequenceQuality(jsonObject.has("sequence-quality") ? jsonObject.get("sequence-quality").getAsJsonObject() : null));

        consensusSequenceBlockList.add(consensusSequenceBlock);

        return consensusSequenceBlockList;
    }

    private Variant createVariant(JsonObject jsonObject) {
        Variant variant = new Variant();

        if (jsonObject == null) {
            return variant;
        }

        variant.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        variant.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        variant.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        variant.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        variant.setAlternateBases(jsonObject.has("alternate-bases") ? jsonObject.get("alternate-bases").getAsString() : null);
        variant.setAnyAttribute(jsonObject.has("any-attribute") ? jsonObject.get("any-attribute").getAsJsonObject() : null);
        variant.setEnd(jsonObject.has("end") ? jsonObject.get("end").getAsInt() : null);
        variant.setFilter(jsonObject.has("filter") ? jsonObject.get("filter").getAsString() : null);
        variant.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);
        variant.setQualityScore(jsonObject.has("quality-score") ? jsonObject.get("quality-score").getAsString() : null);
        variant.setReferenceBases(jsonObject.has("reference-bases") ? jsonObject.get("reference-bases").getAsString() : null);
        variant.setStart(jsonObject.has("start") ? jsonObject.get("start").getAsInt() : null);
        variant.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);
        variant.setVariantId(jsonObject.has("variant-id") ? jsonObject.get("variant-id").getAsString() : null);
        variant.setVariantEffect(createVariantEffect(jsonObject.has("variant-effect") ? jsonObject.get("variant-effect").getAsJsonObject() : null));

        return variant;
    }

    private VariantEffect createVariantEffect(JsonObject jsonObject) {
        VariantEffect variantEffect = new VariantEffect();

        if (jsonObject == null) {
            return variantEffect;
        }

        variantEffect.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        variantEffect.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        variantEffect.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        variantEffect.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        variantEffect.setAnyAttribute(jsonObject.has("any-attribute") ? jsonObject.get("any-attribute").getAsJsonObject() : null);
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

        sequenceQuality.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        sequenceQuality.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        sequenceQuality.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        sequenceQuality.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        sequenceQuality.setQualityScore(jsonObject.has("quality-score") ? jsonObject.get("quality-score").getAsString() : null);
        sequenceQuality.setSequenceEnd(jsonObject.has("sequence-end") ? jsonObject.get("sequence-end").getAsInt() : null);
        sequenceQuality.setSequenceStart(jsonObject.has("sequence-start") ? jsonObject.get("sequence-start").getAsInt() : null);

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

        referenceDatabase.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        referenceDatabase.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        referenceDatabase.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        referenceDatabase.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);

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

        alleleAssignment.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        alleleAssignment.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        alleleAssignment.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        alleleAssignment.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        alleleAssignment.setDate(jsonObject.has("date") ? formatter.parseDateTime(jsonObject.get("date").getAsString()).toDate() : null);
        alleleAssignment.setAlleleDb(jsonObject.has("allele-db") ? jsonObject.get("allele-db").getAsString() : null);
        alleleAssignment.setAlleleVersion(jsonObject.has("allele-version") ? jsonObject.get("allele-version").getAsString() : null);
        alleleAssignment.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        alleleAssignment.setGenotypes(createGenotypes(jsonObject.has("genotype") ? jsonObject.get("genotype").getAsJsonObject() : null));
        alleleAssignment.setGlString(createGlString(jsonObject.has("glstring") ? jsonObject.get("glstring").getAsString() : null));
        alleleAssignment.setHaploid(handleHaploid(jsonObject.has("haploid") ? jsonObject.get("haploid") : null));

        alleleAssignments.add(alleleAssignment);

        return alleleAssignments;
    }

    private GlString createGlString(String glstr) {
        GlString glstring = new GlString();

        if (glstr == null) {
            return glstring;
        }

        glstring.setValue(glstr);

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

        haploid.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        haploid.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        haploid.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        haploid.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        haploid.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        haploid.setMethod(jsonObject.has("method") ? jsonObject.get("method").getAsString() : null);
        haploid.setType(jsonObject.has("type") ? jsonObject.get("type").getAsString() : null);

        haploids.add(haploid);

        return haploids;
    }

    private List<Genotype> createGenotypes(JsonObject jsonObject) {
        List<Genotype> genotypes = new ArrayList<>();
        Genotype genotype = new Genotype();

        if (jsonObject == null) {
            return  genotypes;
        }

        genotype.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        genotype.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        genotype.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        genotype.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        genotype.setDiploidCombinations(createDiploidCombinations(jsonObject.has("diploid-combination") ? jsonObject.get("diploid-combination").getAsJsonObject() : null));

        genotypes.add(genotype);
        return genotypes;
    }

    private List<DiploidCombination> createDiploidCombinations(JsonObject jsonObject) {
        List<DiploidCombination> diploidCombinations = new ArrayList<>();
        DiploidCombination diploidCombination = new DiploidCombination();

        if (jsonObject == null) {
            return diploidCombinations;
        }

        diploidCombination.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        diploidCombination.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        diploidCombination.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        diploidCombination.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        diploidCombination.setLocusBlock(createLocusBlock(jsonObject.has("locus-block") ? jsonObject.get("locus-block").getAsJsonObject() : null));

        diploidCombinations.add(diploidCombination);
        return diploidCombinations;
    }

    private LocusBlock createLocusBlock(JsonObject jsonObject) {
        LocusBlock locusBlock = new LocusBlock();

        if (jsonObject == null) {
            return locusBlock;
        }

        locusBlock.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        locusBlock.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        locusBlock.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        locusBlock.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        locusBlock.setAlleles(createAlleles(jsonObject.has("allele-list") ? jsonObject.get("allele-list").getAsJsonObject() : null));

        return locusBlock;
    }

    private List<Allele> createAlleles(JsonObject jsonObject) {
        List<Allele> alleles = new ArrayList<>();
        Allele allele = new Allele();

        if (jsonObject == null) {
            return alleles;
        }

        allele.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        allele.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        allele.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        allele.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
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

        typingMethod.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        typingMethod.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        typingMethod.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        typingMethod.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        typingMethod.setSbtSanger(createSbtSanger(jsonObject.has("sbt-sanger") ? jsonObject.get("sbt-sanger").getAsJsonObject() : null));
        typingMethod.setSbtNgs(handleSbtNgs(jsonObject.has("sbt-ngs") ? jsonObject.get("sbt-ngs") : null));
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

        sbtNgs.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        sbtNgs.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        sbtNgs.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        sbtNgs.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        sbtNgs.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sbtNgs.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        sbtNgs.setRawReads(handleRawReads(jsonObject.has("raw-reads") ? jsonObject.get("raw-reads") : null));
        sbtNgs.setTestId(jsonObject.has("test-id") ? jsonObject.get("test-id").getAsString() : null);
        sbtNgs.setTestIdSource(jsonObject.has("test-id-source") ? jsonObject.get("test-id-source").getAsString() : null);

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

        rawRead.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        rawRead.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        rawRead.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        rawRead.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        rawRead.setAdapterTrimmed(jsonObject.has("adapter-trimmed") ? jsonObject.get("adapter-trimmed").getAsBoolean() : null);
        rawRead.setAvailability(jsonObject.has("availability") ? jsonObject.get("availability").getAsString() : null);
        rawRead.setFormat(jsonObject.has("format") ? jsonObject.get("format").getAsString() : null);
        rawRead.setPaired(jsonObject.has("paired") ? jsonObject.get("paired").getAsBoolean() : null);
        rawRead.setPooled(jsonObject.has("pooled") ? jsonObject.get("pooled").getAsBoolean() : null);
        rawRead.setQualityTrimmed(jsonObject.has("quality-trimmed") ? jsonObject.get("quality-trimmed").getAsBoolean() : null);
        rawRead.setUri(jsonObject.has("uri") ? jsonObject.get("uri").getAsString() : null);

        rawReads.add(rawRead);
        return rawReads;
    }

    private Sso createSso(JsonObject jsonObject) {
        Sso sso = new Sso();

        if (jsonObject == null) {
            return sso;
        }

        sso.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        sso.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        sso.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        sso.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        sso.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sso.setTestId(jsonObject.has("test-id") ? jsonObject.get("test-id").getAsString() : null);
        sso.setTestIdSource(jsonObject.has("test-id-source") ? jsonObject.get("test-id-source").getAsString() : null);
        sso.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));

        return sso;
    }

    private Ssp createSsp(JsonObject jsonObject) {
        Ssp ssp = new Ssp();

        if (jsonObject == null) {
            return ssp;
        }

        ssp.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        ssp.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        ssp.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        ssp.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        ssp.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        ssp.setScores(jsonObject.has("scores") ? jsonObject.get("scores").getAsString() : null);
        ssp.setTestId(jsonObject.has("test-id") ? jsonObject.get("test-id").getAsString() : null);
        ssp.setTestIdSource(jsonObject.has("test-id-source") ? jsonObject.get("test-id-source").getAsString() : null);
        ssp.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));

        return ssp;
    }

    private SbtSanger createSbtSanger(JsonObject jsonObject) {
        SbtSanger sbtSanger = new SbtSanger();

        if (jsonObject == null) {
            return  sbtSanger;
        }

        sbtSanger.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        sbtSanger.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        sbtSanger.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        sbtSanger.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        sbtSanger.setLocus(jsonObject.has("locus") ? jsonObject.get("locus").getAsString() : null);
        sbtSanger.setTestId(jsonObject.has("test-id") ? jsonObject.get("test-id").getAsString() : null);
        sbtSanger.setTestIdSource(jsonObject.has("test-id-source") ? jsonObject.get("test-id-source").getAsString() : null);
        sbtSanger.setProperties(createProperties(jsonObject.has("property") ? jsonObject.get("property").getAsJsonObject() : null));
        sbtSanger.setAmplification(createAmplification(jsonObject.has("amplification") ? jsonObject.get("amplification").getAsJsonObject() : null));
        sbtSanger.setSubAmplification(createSubAmplification(jsonObject.has("sub-amplification") ? jsonObject.get("sub-amplification").getAsJsonObject() : null));
        sbtSanger.setGssp(createGssp(jsonObject.has("gssp") ? jsonObject.get("gssp").getAsJsonObject() : null));

        return sbtSanger;
    }

    private Amplification createAmplification(JsonObject jsonObject) {
        Amplification amplification = new Amplification();

        if (jsonObject == null) {
            return amplification;
        }

        amplification.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        amplification.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        amplification.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        amplification.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        amplification.setRegisteredName(jsonObject.has("registered-name") ? jsonObject.get("registered-name").getAsString() : null);
        amplification.setSequence(createSequence(jsonObject.has("sequence") ? jsonObject.get("sequence").getAsString() : null));

        return amplification;
    }

    private SubAmplification createSubAmplification(JsonObject jsonObject) {
        SubAmplification subAmplification = new SubAmplification();

        if (jsonObject == null) {
            return subAmplification;
        }

        subAmplification.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        subAmplification.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        subAmplification.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        subAmplification.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        subAmplification.setRegisteredName(jsonObject.has("registered-name") ? jsonObject.get("registered-name").getAsString() : null);
        subAmplification.setSequence(createSequence(jsonObject.has("sequence") ? jsonObject.get("sequence").getAsString() : null));

        return subAmplification;
    }

    private Gssp createGssp(JsonObject jsonObject) {
        Gssp gssp = new Gssp();

        if (jsonObject == null) {
            return gssp;
        }

        gssp.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        gssp.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        gssp.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        gssp.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        gssp.setRegisteredName(jsonObject.has("registered-name") ? jsonObject.get("registered-name").getAsString() : null);
        gssp.setPrimerTarget(jsonObject.has("primer-target") ? jsonObject.get("primer-target").getAsString() : null);
        gssp.setPrimerSequence(jsonObject.has("primer-sequence") ? jsonObject.get("primer-sequence").getAsString() : null);
        gssp.setSequence(createSequence(jsonObject.has("sequence") ? jsonObject.get("sequence").getAsString() : null));

        return gssp;
    }

    private Sequence createSequence(String seq) {
        Sequence sequence = new Sequence();

        if (seq == null) {
            return sequence;
        }

        sequence.setSequence(seq);

        return sequence;
    }

    private List<IupacBases> createIupacBases(JsonObject jsonObject) {
        List<IupacBases> iupacBases = new ArrayList<>();
        IupacBases iupacBase = new IupacBases();

        if (jsonObject == null) {
            return iupacBases;
        }

        iupacBase.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        iupacBase.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        iupacBase.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        iupacBase.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
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

        collectionMethod.setId(jsonObject.has("id") ? jsonObject.get("id").getAsString() : null);
        collectionMethod.setActive(jsonObject.has("active") ? jsonObject.get("active").getAsBoolean() : null);
        collectionMethod.setDateCreated(jsonObject.has("date-created") ? formatter.parseDateTime(jsonObject.get("date-created").getAsString()).toDate() : null);
        collectionMethod.setDateUpdated(jsonObject.has("date-updated") ? formatter.parseDateTime(jsonObject.get("date-updated").getAsString()).toDate() : null);
        collectionMethod.setDescription(jsonObject.has("description") ? jsonObject.get("description").getAsString() : null);
        collectionMethod.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : null);

        collectionMethods.add(collectionMethod);
        return collectionMethods;
    }
}