package org.nmdp.kafkaconsumer.metrics;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/26/17.
 * <p>
 * kafka-consumer
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetricNameBuilder {
    private final List<String> parts;

    private MetricNameBuilder() {
        parts = new ArrayList<>();
    }

    public static MetricNameBuilder of(Class<?> clazz) {
        return new MetricNameBuilder().and("name", clazz.getName());
    }

    public MetricNameBuilder and(String key, String value) {
        parts.add(key + "=" + value);
        return this;
    }

    public String build() {
        return parts.stream().collect(Collectors.joining(","));
    }
}
