package org.nmdp.hmlfhirconvertermodels.util;

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

import org.nmdp.hmlfhirconvertermodels.dto.hml.QueryCriteria;
import org.nmdp.hmlfhirconvertermodels.dto.hml.TypeaheadQuery;
import org.apache.log4j.Logger;
import org.nmdp.hmlfhirconvertermodels.domain.base.IMongoDataRepositoryModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryBuilder {

    private final static Logger LOG = Logger.getLogger(QueryBuilder.class);

    public static Query buildQuery(Integer maxResults, TypeaheadQuery typeaheadQuery) {
        final Pageable pageable = new PageRequest(0, maxResults);
        Query query = new Query();
        query.with(pageable);

        List<List<QueryCriteria>> likeNonExclusionQueries = groupCriteriaByQueryProperty(typeaheadQuery.getCriteria().stream()
                .filter(Objects::nonNull)
                .filter(q -> q.getUseLike() && !q.getExclude())
                .collect(Collectors.toList()));

        List<List<QueryCriteria>> isNonExclusionQueries = groupCriteriaByQueryProperty(typeaheadQuery.getCriteria().stream()
                .filter(Objects::nonNull)
                .filter(q -> !q.getUseLike() && !q.getExclude())
                .collect(Collectors.toList()));

        List<List<QueryCriteria>> likeExclusionQueries = groupCriteriaByQueryProperty(typeaheadQuery.getCriteria().stream()
                .filter(Objects::nonNull)
                .filter(q -> q.getUseLike() && q.getExclude())
                .collect(Collectors.toList()));

        List<List<QueryCriteria>> isExclusionQueries = groupCriteriaByQueryProperty(typeaheadQuery.getCriteria().stream()
                .filter(Objects::nonNull)
                .filter(q -> !q.getUseLike() && q.getExclude())
                .collect(Collectors.toList()));


        assembleCriteriaInQuery(query, likeNonExclusionQueries, isNonExclusionQueries,
                likeExclusionQueries, isExclusionQueries);

        return query;
    }

    public static Query buildEqualsPropertyQuery(IMongoDataRepositoryModel model, List<String> properties, Boolean hyphenateId) {
        Query query = new Query();
        List<QueryCriteria> qcs = getQueryCriteria(model, properties, hyphenateId);

        for (QueryCriteria qc : qcs) {
            constructEqualsNonExclusionQuery(query, qc);
        }

        return query;
    }

    public static Query buildPropertyQuery(String propertyName, String propertyValue, boolean useLike) {
        Query query = new Query();
        List<QueryCriteria> qcs = new ArrayList<>();
        QueryCriteriaExtended qc = new QueryCriteriaExtended(false, useLike, propertyName, propertyValue);

        qcs.add(qc);

        if (useLike) {
            constructLikeNonExclusionQuery(query, qcs);
        } else {
            constructIsNonExclusionQuery(query, qcs);
        }

        return query;
    }

    public static Query buildPropertyQuery(IMongoDataRepositoryModel model, List<String> properties) {
        Query query = new Query();
        List<QueryCriteria> qcs = getQueryCriteria(model, properties);
        constructIsNonExclusionQuery(query, qcs);

        return query;
    }

    private static List<QueryCriteria> getQueryCriteria(IMongoDataRepositoryModel model, List<String> properties) {
        List<QueryCriteria> qcs = new ArrayList<>();

        for (String property : properties) {
            try {
                Object propertyValue = model.getPropertyValueByName(model, property);
                Object value = propertyValue == null ? "" : propertyValue;
                QueryCriteriaExtended qc = new QueryCriteriaExtended(false, false, property, value);
                qcs.add(qc);
            } catch (Exception ex) {
                LOG.error(ex);
                continue;
            }
        }

        return qcs;
    }

    private static List<QueryCriteria> getQueryCriteria(IMongoDataRepositoryModel model, List<String> properties, Boolean hyphenateId) {
        List<QueryCriteria> qcs = new ArrayList<>();

        for (String property : properties) {
            try {
                Object propertyValue = model.getPropertyValueByName(model, property);
                Object value = propertyValue == null ? "" : propertyValue;

                if (hyphenateId && property == "id") {
                    property = "_" + property;
                }

                QueryCriteriaExtended qc = new QueryCriteriaExtended(false, false, property, value);
                qcs.add(qc);
            } catch (Exception ex) {
                LOG.error(ex);
                continue;
            }
        }

        return qcs;
    }

    private static void constructLikeNonExclusionQuery(Query query, List<QueryCriteria> criterium) {
        for (QueryCriteria qCriteria : criterium) {
            String regex = regexBuilder(qCriteria.getQueryValue().toString(), false);
            query.addCriteria(Criteria.where(qCriteria.getPropertyName()).regex(regex));
        }
    }

    private static void constructIsNonExclusionQuery(Query query, List<QueryCriteria> criterium) {
        String propertyName = criterium.get(0).getPropertyName();
        query.addCriteria(Criteria.where(propertyName).in(criterium.stream()
            .filter(Objects::nonNull)
            .map(q -> q.getQueryValue().toString())
            .collect(Collectors.toList())));
    }

    private static void constructEqualsNonExclusionQuery(Query query, QueryCriteria criteria) {
        String propertyName = criteria.getPropertyName();
        query.addCriteria(Criteria.where(propertyName).is(criteria.getQueryValue()));
    }

    private static void constructLikeExclusionQuery(Query query, List<QueryCriteria> criterium) {
        for (QueryCriteria qCriteria : criterium) {
            String regex = regexBuilder(qCriteria.getQueryValue().toString(), true);
            query.addCriteria(Criteria.where(qCriteria.getPropertyName()).regex(regex));
        }
    }

    private static void constructIsExclusionQuery(Query query, List<QueryCriteria> criterium) {
        String propertyName = criterium.get(0).getPropertyName();
        query.addCriteria(Criteria.where(propertyName).nin(criterium.stream()
            .filter(Objects::nonNull)
            .map(q -> q.getQueryValue().toString())
            .collect(Collectors.toList())));
    }

    private static List<List<QueryCriteria>> groupCriteriaByQueryProperty(List<QueryCriteria> criteria) {
        List<List<QueryCriteria>> propertyGroupList = new ArrayList<>();
        List<String> properties = criteria.stream()
                .filter(Objects::nonNull)
                .filter(distinctByKey(p -> p.getPropertyName()))
                .map(p -> p.getPropertyName())
                .collect(Collectors.toList());

        for (String queryProperty : properties) {
            List<QueryCriteria> propertyCriteria = criteria.stream()
                    .filter(Objects::nonNull)
                    .filter(p -> p.getPropertyName().equals(queryProperty))
                    .collect(Collectors.toList());

            propertyGroupList.add(propertyCriteria);
        }

        return propertyGroupList;
    }

    private static void assembleCriteriaInQuery(Query query, List<List<QueryCriteria>> likeNonExclusionQueries,
        List<List<QueryCriteria>> isNonExclusionQueries, List<List<QueryCriteria>> likeExclusionQueries,
        List<List<QueryCriteria>> isExclusionQueries) {

        for (List<QueryCriteria> likeNonExlusionList : likeNonExclusionQueries) {
            constructLikeNonExclusionQuery(query, likeNonExlusionList);
        }

        for (List<QueryCriteria> isNonExclusionList: isNonExclusionQueries) {
            constructIsNonExclusionQuery(query, isNonExclusionList);
        }

        for (List<QueryCriteria> likeExclusionList : likeExclusionQueries) {
            constructLikeExclusionQuery(query, likeExclusionList);
        }

        for (List<QueryCriteria> isExclusionList : isExclusionQueries) {
            constructIsExclusionQuery(query, isExclusionList);
        }
    }

    private static String regexBuilder(String pattern, Boolean negativeMatch) {
        String regex = negativeMatch ? "((?!" : "";

        for (Character c : pattern.toCharArray()) {
            if (c.isAlphabetic(c)) {
                regex += "[" + c.toUpperCase(c) + c.toLowerCase(c) + "]";
            } else {
                regex += c;
            }
        }

        return regex + (negativeMatch ? ").)" : "");
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> observed = new ConcurrentHashMap<>();

        return t -> observed.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
