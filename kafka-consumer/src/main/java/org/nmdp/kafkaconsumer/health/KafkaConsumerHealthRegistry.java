package org.nmdp.kafkaconsumer.health;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/30/17.
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

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nmdp.kafkaconsumer.consumer.KafkaMessageConsumer;

public class KafkaConsumerHealthRegistry extends MetricRegistryListener.Base {
    private static final String HEALTH_FETCH_AGE = "fetch_age";

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumerHealthRegistry.class);

    private final HealthCheckRegistry health;
    private final long allowedConsumerDelayMs;
    private final Map<String, Gauge<Date>> lastFetched = new HashMap<>();
    private final Map<String, Gauge<Integer>> consumerActive = new HashMap<>();

    public KafkaConsumerHealthRegistry(HealthCheckRegistry health, long allowedConsumerDelayMs) {
        this.health = health;
        this.allowedConsumerDelayMs = allowedConsumerDelayMs;
    }

    private boolean isConsumerMetric(String name) {
        return name.contains("name=" + KafkaMessageConsumer.class.getName());
    }

    private String metricType(String name) {
        String[] props = name.split(",");
        for (String prop : props) {
            String[] parts = prop.split("=", 2);
            if (parts.length != 2) {
                continue;
            }
            if ("metric".equals(parts[0])) {
                return parts[1];
            }
        }
        return null;
    }

    private String healthCheckName(String name, String check) {
        StringBuilder buf = new StringBuilder();
        String[] props = name.split(",");
        for (String prop : props) {
            String[] parts = prop.split("=", 2);
            if (parts.length != 2) {
                continue;
            }
            if ("name".equals(parts[0])) {
                continue;
            }
            if ("metric".equals(parts[0])) {
                continue;
            }
            if (buf.length() > 0) {
                buf.append(".");
            }
            buf.append(parts[1]);
        }
        if (buf.length() > 0) {
            buf.append(".");
            buf.append(check);
        }
        return buf.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void onGaugeAdded(String name, Gauge<?> gauge) {
        if (!isConsumerMetric(name)) {
            LOG.debug("Skipping non-consumer metric {}", name);
            return;
        }

        boolean foundConsuming = false;
        String consumingName = healthCheckName(name, HEALTH_FETCH_AGE);
        String metricType = metricType(name);
        if ("lastFetched".equals(metricType)) {
            lastFetched.put(consumingName, (Gauge<Date>) gauge);
            if (consumerActive.containsKey(consumingName)) {
                foundConsuming = true;
            }
        } else if ("consumerActive".equals(metricType)) {
            consumerActive.put(consumingName, (Gauge<Integer>) gauge);
            if (lastFetched.containsKey(consumingName)) {
                foundConsuming = true;
            }
        }

        if (foundConsuming) {
            health.unregister(consumingName);
            health.register(consumingName,
                    new FetchAgeCheck(consumerActive.get(consumingName), lastFetched.get(consumingName),
                            allowedConsumerDelayMs));
            LOG.info("Registered health check {}", consumingName);
        }
    }

    @Override
    public void onGaugeRemoved(String name) {
        if (!isConsumerMetric(name)) {
            return;
        }

        boolean foundConsuming = false;
        String consumingName = healthCheckName(name, HEALTH_FETCH_AGE);
        String metricType = metricType(name);
        if ("lastFetched".equals(metricType)) {
            lastFetched.remove(consumingName);
            foundConsuming = true;
        } else if ("consumerActive".equals(metricType)) {
            consumerActive.remove(consumingName);
            foundConsuming = true;
        }

        if (foundConsuming) {
            health.unregister(consumingName);
            LOG.info("Unregistered health check {}", consumingName);
        }

    }

    private static final class FetchAgeCheck extends HealthCheck {
        private final Gauge<Integer> consumerActive;
        private final Gauge<Date> lastFetched;

        private final long allowedConsumerDelayMs;

        public FetchAgeCheck(Gauge<Integer> consumerActive, Gauge<Date> lastFetched, long allowedConsumerDelayMs) {
            this.consumerActive = consumerActive;
            this.lastFetched = lastFetched;
            this.allowedConsumerDelayMs = allowedConsumerDelayMs;
        }

        @Override
        protected Result check() throws Exception {
            if (consumerActive.getValue().intValue() > 0) {
                long age = Math.max(0L, System.currentTimeMillis() - lastFetched.getValue().getTime());
                String msg = "state=ACTIVE age=" + new DecimalFormat("####################").format(age) + "ms";
                if (age <= allowedConsumerDelayMs) {
                    return Result.healthy(msg);
                } else {
                    return Result.unhealthy(msg);
                }
            } else {
                return Result.healthy("state=INACTIVE");
            }
        }

    }
}
