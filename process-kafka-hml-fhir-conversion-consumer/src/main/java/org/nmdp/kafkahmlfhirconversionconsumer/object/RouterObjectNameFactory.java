package org.nmdp.kafkahmlfhirconversionconsumer.object;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/30/17.
 * <p>
 * process-kafka-hml-fhir-conversion-consumer
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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.codahale.metrics.DefaultObjectNameFactory;
import com.codahale.metrics.ObjectNameFactory;

public class RouterObjectNameFactory implements ObjectNameFactory {
    private final DefaultObjectNameFactory defaultFactory = new DefaultObjectNameFactory();

    @Override
    public ObjectName createName(String type, String domain, String name) {
        try {
            if (name.contains("=")) {
                return new ObjectName(domain + ":" + name);
            }
            return defaultFactory.createName(type, domain, name);
        } catch (MalformedObjectNameException e) {
            return defaultFactory.createName(type, domain, name);
        }
    }
}
