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

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public class HealthReporter implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(HealthReporter.class);
    private final ReporterThread reporterThread;

    public HealthReporter(HealthCheckRegistry health, long reportingIntervalMs) {
        this.reporterThread = new ReporterThread(health, reportingIntervalMs);
        this.reporterThread.start();
    }

    @Override
    public void close() throws IOException {
        reporterThread.shutdown();
    }

    private static final class ReporterThread extends Thread {
        private final HealthCheckRegistry health;
        private final long reportingIntervalMs;
        private volatile boolean shutdown = false;

        public ReporterThread(HealthCheckRegistry health, long reportingIntervalMs) {
            super("Health Reporter");
            this.health = health;
            this.reportingIntervalMs = reportingIntervalMs;
        }

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    int count = 0;
                    int failures = 0;
                    for (Map.Entry<String, HealthCheck.Result> entry : health.runHealthChecks().entrySet()) {
                        Logger logger = LoggerFactory.getLogger("healthcheck." + entry.getKey());
                        count++;
                        HealthCheck.Result result = entry.getValue();
                        if (result.isHealthy()) {
                            if (result.getMessage() == null) {
                                logger.info("HEALTHY");
                            } else {
                                logger.info("HEALTHY: {}", result.getMessage());
                            }
                        } else {
                            failures++;
                            if (result.getMessage() == null) {
                                if (result.getError() == null) {
                                    logger.error("UNHEALTHY");
                                } else {
                                    logger.error("UNHEALTHY", result.getError());
                                }
                            } else {
                                if (result.getError() == null) {
                                    logger.error("UNHEALTHY: {}", result.getMessage());
                                } else {
                                    logger.error("UNHEALTHY: " + result.getMessage(), result.getError());
                                }
                            }
                        }
                    }

                    Logger all = LoggerFactory.getLogger("healthcheck.all");
                    if (failures > 0) {
                        all.error("UNHEALTHY: {} of {} checks failed", failures, count);
                    } else {
                        all.info("HEALTHY: {} checks succeeded", count);
                    }
                } catch (Throwable t) {
                    LOG.error("Exception while reporting health", t);
                    Logger all = LoggerFactory.getLogger("healthcheck.all");
                    all.error("UNHEALTHY: Failed to report checks", t);
                }
                try {
                    Thread.sleep(reportingIntervalMs);
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }

        public void shutdown() {
            shutdown = true;
            interrupt();
        }
    }
}
