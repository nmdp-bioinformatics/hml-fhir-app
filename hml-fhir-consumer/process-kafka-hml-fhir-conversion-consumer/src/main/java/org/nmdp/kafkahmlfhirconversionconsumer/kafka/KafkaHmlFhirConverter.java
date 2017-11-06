package org.nmdp.kafkahmlfhirconversionconsumer.kafka;

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

import org.apache.log4j.PropertyConfigurator;

import org.nmdp.kafkaconsumer.metrics.KafkaConsumerAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yaml.snakeyaml.Yaml;

import com.google.common.annotations.VisibleForTesting;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.health.HealthCheckRegistry;

import org.nmdp.kafkaconsumer.consumer.KafkaMessageConsumer;
import org.nmdp.kafkaconsumer.health.HealthReporter;
import org.nmdp.kafkaconsumer.health.KafkaConsumerHealthRegistry;
import org.nmdp.kafkahmlfhirconversionconsumer.object.RouterObjectNameFactory;
import org.nmdp.kafkahmlfhirconversionconsumer.config.RootConfiguration;

public class KafkaHmlFhirConverter {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaHmlFhirConverter.class);
    private final List<Closeable> closeables = new ArrayList<>();

    private static void usage() {
        System.err.println("Usage: " + KafkaHmlFhirConverter.class.getSimpleName() + " <conf-file>");
        System.err.println();
        System.err.println("  conf-file     Configuration file to read (YAML)");
        System.err.println();
    }

    @VisibleForTesting
    public final MetricRegistry metrics;

    public KafkaHmlFhirConverter(URL configUrl) throws Exception {
        Yaml yaml = new Yaml();
        RootConfiguration config;

        try (InputStream is = configUrl.openStream()) {
            config = yaml.loadAs(is, RootConfiguration.class);
        }

        LOG.info("Configuration: {}", config);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        metrics = new MetricRegistry();
        final HealthCheckRegistry health = new HealthCheckRegistry();

        metrics.addListener(new KafkaConsumerHealthRegistry(health, config.getAllowedConsumerDelayMs()));
        metrics.addListener(new KafkaConsumerAggregate(metrics));

        closeables.add(new HealthReporter(health, config.getHealthReportingIntervalMs()));

        final JmxReporter jmxReporter = JmxReporter.forRegistry(metrics)
                .createsObjectNamesWith(new RouterObjectNameFactory())
                .build();

        jmxReporter.start();
        closeables.add(jmxReporter);

        final Slf4jReporter slf4jReporter = Slf4jReporter.forRegistry(metrics).build();
        slf4jReporter.start(60, TimeUnit.SECONDS);
        closeables.add(slf4jReporter);

        try {
            Map<String, List<KafkaMessageConsumer>> connectors = config.initConnectors(closeables, metrics);
            LOG.info("Connectors: {}", connectors.values().stream()
                    .flatMap(List::stream)
                    .map(KafkaMessageConsumer::toString)
                    .collect(Collectors.joining("\n  ", "\n  ", "")));
            LOG.info("Application started");
        } catch (Exception e) {
            LOG.error("Error starting app", e);
            shutdown();
            throw e;
        }
    }

    public void shutdown() {
        List<Closeable> shutdown = new ArrayList<>();
        shutdown.addAll(closeables);
        Collections.reverse(shutdown);
        shutdown.stream().forEach(c -> {
            try {
                c.close();
            } catch (Exception e) {
                LOG.error("Error shutting down " + c, e);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            usage();
            System.exit(1);
        }

        String confFile = args[0];
        URL url = null;

        if (confFile.startsWith("classpath:")) {
            url = KafkaHmlFhirConverter.class.getResource(confFile.substring(10));
        } else {
            url = new File(confFile).getAbsoluteFile().toURI().toURL();
        }

        new KafkaHmlFhirConverter(url);
    }

    /**
     * Entry point for development work. Should <strong>not</strong> be used under normal
     * circumstances.
     */
    public static class DevDriver {
        static {
            PropertyConfigurator.configure(KafkaHmlFhirConverter.DevDriver.class.getResource("/resources/log4j.properties"));
        }

        public static void main(String[] args) throws Exception {
            KafkaHmlFhirConverter.main(new String[] { "classpath:/resources/consumer-configuration.yml" });
        }
    }
}
