package org.nmdp.hmlfhirconvertermodels.domain.base;

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

import com.mongodb.*;

import org.apache.log4j.Logger;

import org.bson.types.ObjectId;

import org.nmdp.hmlfhirconvertermodels.domain.hml.ICascadable;
import org.nmdp.hmlfhirconvertermodels.domain.internal.MongoConfiguration;
import org.nmdp.hmlfhirconvertermodels.util.*;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.xml.bind.annotation.XmlAttribute;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

abstract class CascadingUpdate<T extends SwaggerConverter<T, U>, U> implements ICascadingUpdate<T, U> {

    @Transient
    private final static Logger LOG = Logger.getLogger(CascadingUpdate.class);

    @Override
    public void updateCollectionProperties(T entity, MongoConfiguration mongoConfiguration) {
        save(entity, mongoConfiguration, true);
    }

    @Override
    public void saveCollectionProperties(T entity, MongoConfiguration mongoConfiguration) {
        save(entity, mongoConfiguration, false);
    }

    private void save(T entity, MongoConfiguration mongoConfiguration, Boolean isUpdate) {
        List<Field> cascadingFields = getSavableFields(entity);
        MongoOperations mongoOperations = getMongoOperations(mongoConfiguration);

        for (Field field : cascadingFields) {
            Class<?> propertyClass = field.getType();
            Document document = getDocumentFromField(propertyClass, field);
            String collectionName = document.collection();
            field.setAccessible(true);
            handleCascade(field, entity, mongoOperations, collectionName, isUpdate);
        }
    }

    private void handleCascade(Field field, T entity, MongoOperations mongoOperations, String collectionName, Boolean isUpdate) {
        try {
            Object model = convertToDataModel(field, entity);

            if (isUpdate) {
                handleUpdatedDate(model, field);
            }

            handleDateField(model, field);
            setModelField(model, field, mongoOperations, collectionName, entity);
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private void setModelField(Object model, Field field, MongoOperations mongoOperations, String collectionName, T entity) {
        field.setAccessible(true);

        try {
            if (field.getGenericType() instanceof ParameterizedType) {
                List<IMongoDataRepositoryModel> savedCollection = new ArrayList<>();
                for (IMongoDataRepositoryModel item : (List<IMongoDataRepositoryModel>)model) {
                    IMongoDataRepositoryModel savedItem = upsert(item, mongoOperations, collectionName);
                    savedCollection.add(savedItem);
                }

                field.set(entity, savedCollection);
                return;
            }

            field.set(entity, upsert((IMongoDataRepositoryModel)model, mongoOperations, collectionName));
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private void handleDateField(Object model, Field field) {
        field.setAccessible(true);

        try {
            if (field.getGenericType() instanceof  ParameterizedType) {
                Converters.handleListDateField((List<IMongoDataRepositoryModel>)model);
                return;
            }

            Converters.handleDateField((IMongoDataRepositoryModel)model);
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private Object convertToDataModel(Field field, T entity) {
        field.setAccessible(true);

        try {
            if (field.getGenericType() instanceof ParameterizedType) {
                return (List<IMongoDataRepositoryModel>) field.get(entity);
            }

            return (IMongoDataRepositoryModel)field.get(entity);
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }

    private void handleUpdatedDate(Object model, Field field) {
        field.setAccessible(true);

        if (field.getGenericType() instanceof ParameterizedType) {
            for (IMongoDataRepositoryModel m : (List<IMongoDataRepositoryModel>)model) {
                setUpdatedDateOnModel(m);
            }

            return;
        }

        setUpdatedDateOnModel((IMongoDataRepositoryModel)model);
    }

    private void setUpdatedDateOnModel(IMongoDataRepositoryModel model) {
        try {
            Field field = model.getClass().getDeclaredField("dateUpdated");
            field.setAccessible(true);
            field.set(model, new Date());
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private MongoOperations getMongoOperations(MongoConfiguration configuration) {
        return new MongoTemplate(new MongoClient(createMongoConnectionString(configuration)),
                configuration.getDatabaseName());
    }

    private List<Field> getSavableFields(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(Objects::nonNull)
                .filter(r -> implementsCascading(r))
                .filter(r -> !isStaticField(r))
                .collect(Collectors.toList());
    }

    private IMongoDataRepositoryModel upsert(IMongoDataRepositoryModel model, MongoOperations mongoOperations, String collectionName) throws Exception {
        WriteResult writeResult = mongoOperations.upsert(buildUpsertQuery(model), buildUpsertUpdate(model), collectionName);
        DBCollection collection = mongoOperations.getCollection(collectionName);
        Object upsertedId;

        if (writeResult.isUpdateOfExisting()) {
            try {
                Field field = model.getClass().getDeclaredField("id");
                field.setAccessible(true);
                String id = field.get(model).toString();
                upsertedId = objectifyId(id);
            } catch (Exception ex) {
                LOG.error(ex);
                throw new Exception("Unable to evaluate 'id' property", ex);
            }
        } else {
            upsertedId = writeResult.getUpsertedId();
        }

        DBObject result = collection.findOne(upsertedId);
        return model.convertGenericResultToModel(result, model, getDocumentProperties(model));
    }

    private ObjectId objectifyId(String id) {
        return new ObjectId(id);
    }

    private Query buildUpsertQuery(IMongoDataRepositoryModel model) {
        List<String> propertyNames = new ArrayList<>();

        if (hasDocumentId(model)) {
            propertyNames.add("id");
            return org.nmdp.hmlfhirconvertermodels.util.QueryBuilder.buildEqualsPropertyQuery(model, propertyNames, true);
        }

        propertyNames = getDocumentProperties(model).stream()
                .filter(Objects::nonNull)
                .filter(obj -> obj.getName() != "dateUpdated" && obj.getName() != "id")
                .map(obj -> obj.getName())
                .collect(Collectors.toList());

        return org.nmdp.hmlfhirconvertermodels.util.QueryBuilder.buildEqualsPropertyQuery(model, propertyNames, false);
    }

    private boolean hasDocumentId(IMongoDataRepositoryModel model) {
        Field id = getDocumentProperties(model).stream()
                .filter(prop -> prop.getName() == "id")
                .findFirst()
                .get();

        id.setAccessible(true);
        if (getFieldValue(model, id) == null) {
            return false;
        }

        return true;
    }

    private Update buildUpsertUpdate(IMongoDataRepositoryModel model) {
        Update update = new Update();
        List<Field> documentPropertyFields = getDocumentProperties(model);

        for (Field field : documentPropertyFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = getFieldValue(model, field);

            if (fieldName == "id") {
                continue;
            }

            update.set(fieldName, fieldValue);
        }

        return update;
    }

    private List<Field> getDocumentProperties(IMongoDataRepositoryModel model) {
        return Arrays.stream(model.getClass().getDeclaredFields())
                .filter(Objects::nonNull)
                .filter(obj -> !Arrays.stream(obj.getAnnotations())
                        .filter(Objects::nonNull)
                        .filter(a -> a.annotationType().equals(XmlAttribute.class))
                        .collect(Collectors.toList()).isEmpty())
                .collect(Collectors.toList());
    }

    private Object getFieldValue(Object model, Field field) {
        try {
            return field.get(model);
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return null;
    }

    private Document getDocumentFromField(Class<?> propertyClass, Field field) {
        Type type = field.getGenericType();

        if (!(type instanceof ParameterizedType)) {
            return (Document)Arrays.stream(propertyClass.getAnnotations())
                    .filter(Objects::nonNull)
                    .filter(a -> a.annotationType().equals(Document.class))
                    .findFirst()
                    .orElse(null);
        }

        return (Document)Arrays.stream(((ParameterizedType)field.getGenericType()).getActualTypeArguments())
                .filter(Objects::nonNull)
                .map(t -> getDocumentAnnotation((Class<?>)t))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Annotation getDocumentAnnotation(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredAnnotations())
                .filter(Objects::nonNull)
                .filter(annotation -> annotation.annotationType().equals(Document.class))
                .findFirst()
                .orElse(null);
    }

    private Boolean implementsCascading(Field field) {
        Type type = field.getGenericType();

        if (!(type instanceof ParameterizedType)) {
            return Arrays.stream(field.getType().getInterfaces())
                    .filter(Objects::nonNull)
                    .anyMatch(i -> i.equals(ICascadable.class));
        }

        return Arrays.stream(field.getDeclaringClass().getInterfaces())
            .filter(Objects::nonNull)
            .anyMatch(c -> c.equals(ICascadable.class));
    }

    private Boolean isStaticField(Field field) {
        field.setAccessible(true);
        return Modifier.isStatic(field.getModifiers());
    }

    private String createMongoConnectionString(MongoConfiguration config) {
        return config.getHost() + ":" + config.getPortNo();
    }
}
