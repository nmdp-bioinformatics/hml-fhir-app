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

import org.apache.log4j.Logger;

import org.nmdp.hmlfhirconvertermodels.domain.base.IMongoDataRepositoryModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Converters {

    private static final Logger LOG = Logger.getLogger(Converters.class);
    private static final Pattern pattern = Pattern.compile(".*?[Cc]reated");

    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    public static List<IMongoDataRepositoryModel> handleListDateField(List<IMongoDataRepositoryModel> model) {
        for (IMongoDataRepositoryModel m : model) {
            handleDateField(m);
        }

        return model;
    }

    public static IMongoDataRepositoryModel handleDateField(IMongoDataRepositoryModel model) {
        List<Field> fields = Arrays.stream(model.getClass().getDeclaredFields())
                .filter(Objects::nonNull)
                .filter(prop -> prop.getType().equals(Date.class))
                .collect(Collectors.toList());

        for (Field field : fields) {
            Date handledDate = handleDateField(field, model);

            try {
                field.setAccessible(true);
                field.set(model, handledDate);
            } catch (Exception ex) {
                LOG.error(ex);
            }
        }

        return model;
    }

    public static <U> Date handleDateField(Field field, U u) {
        return handleDateField(u, field);
    }

    private static Date handleDateField(Object model, Field field) {
        field.setAccessible(true);

        try {
            final Matcher matcher = pattern.matcher(field.getName());

            if (!matcher.matches()) {
                return (Date)field.get(model);
            }

            if (field.get(model) == null) {
                return new Date();
            }

            return (Date)field.get(model);
        } catch (Exception ex) {
            LOG.error(ex);
        }

        return new Date();
    }
}
