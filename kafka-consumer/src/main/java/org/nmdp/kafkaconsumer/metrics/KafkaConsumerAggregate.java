package org.nmdp.kafkaconsumer.metrics;

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

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.Timer;
import org.nmdp.kafkaconsumer.consumer.KafkaMessageConsumer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class KafkaConsumerAggregate extends MetricRegistryListener.Base {
    private final ConcurrentMap<String, Gauge<Integer>> consumerActive = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Gauge<Long>> messagesBehind = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Gauge<Date>> lastFetched = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Counter> uncommittedMessages = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> handlerCommit = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> handlerProcess = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> handlerRollback = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> kafkaFetch = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Timer> kafkaCommit = new ConcurrentHashMap<>();

    public KafkaConsumerAggregate(MetricRegistry metrics) {
        metrics.register(metricName("consumersActive"), (Gauge<Long>) this::getConsumersActive);
        metrics.register(metricName("messagesBehind"), (Gauge<Long>) this::getMessagesBehind);
        metrics.register(metricName("uncommittedMessages"), (Gauge<Long>) this::getUncommittedMessages);
        metrics.register(metricName("maxFetchAgeMs"), (Gauge<Long>) this::getMaxFetchAgeMs);
        metrics.register(metricName("handlerCommit1MinRate"), (Gauge<Double>) this::getHandlerCommitOneMinuteRate);
        metrics.register(metricName("handlerCommit50PercDurationMs"), (Gauge<Double>) this::getHandlerCommit50PercDurationMs);
        metrics.register(metricName("handlerProcess1MinRate"), (Gauge<Double>) this::getHandlerProcessOneMinuteRate);
        metrics.register(metricName("handlerProcess50PercDurationMs"), (Gauge<Double>) this::getHandlerProcess50PercDurationMs);
        metrics.register(metricName("handlerRollback1MinRate"), (Gauge<Double>) this::getHandlerRollbackOneMinuteRate);
        metrics.register(metricName("handlerRollback50PercDurationMs"), (Gauge<Double>) this::getHandlerRollback50PercDurationMs);
        metrics.register(metricName("kafkaFetch1MinRate"), (Gauge<Double>) this::getKafkaFetchOneMinuteRate);
        metrics.register(metricName("kafkaFetch50PercDurationMs"), (Gauge<Double>) this::getKafkaFetch50PercDurationMs);
        metrics.register(metricName("kafkaCommit1MinRate"), (Gauge<Double>) this::getKafkaCommitOneMinuteRate);
        metrics.register(metricName("kafkaCommit50PercDurationMs"), (Gauge<Double>) this::getKafkaCommit50PercDurationMs);
    }

    private long getConsumersActive() {
        return consumerActive.values().stream()
                .mapToInt(e -> e.getValue().intValue())
                .sum();
    }

    private long getMessagesBehind() {
        return messagesBehind.values().stream()
                .mapToLong(v -> v.getValue().longValue())
                .sum();
    }

    private long getUncommittedMessages() {
        return uncommittedMessages.values().stream()
                .mapToLong(Counter::getCount)
                .sum();
    }

    private long getMaxFetchAgeMs() {
        long now = System.currentTimeMillis();
        return lastFetched.entrySet().stream()
                .map(Map.Entry::getValue)
                .mapToLong(v -> Math.max(0L, now - v.getValue().getTime()))
                .max().orElse(0L);
    }

    private double oneMinuteRate(ConcurrentMap<String, Timer> timers) {
        return timers.values().stream()
                .mapToDouble(Timer::getOneMinuteRate)
                .sum();
    }

    private double getHandlerCommitOneMinuteRate() {
        return oneMinuteRate(handlerCommit);
    }

    private double getHandlerProcessOneMinuteRate() {
        return oneMinuteRate(handlerProcess);
    }

    private double getHandlerRollbackOneMinuteRate() {
        return oneMinuteRate(handlerRollback);
    }

    private double getKafkaFetchOneMinuteRate() {
        return oneMinuteRate(kafkaFetch);
    }

    private double getKafkaCommitOneMinuteRate() {
        return oneMinuteRate(kafkaCommit);
    }

    private static class Tuple2<T1, T2> {
        private final T1 t1;
        private final T2 t2;

        public Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public T1 t1() {
            return t1;
        }

        public T2 t2() {
            return t2;
        }
    }

    private double p50DurationMs(ConcurrentMap<String, Timer> timers) {
        double durationFactor = 1.0 / TimeUnit.MILLISECONDS.toNanos(1L);

        List<Tuple2<Double, Double>> data = timers.values().stream()
                .map(v -> new Tuple2<>(v.getOneMinuteRate(), v.getSnapshot().getMedian()))
                .collect(Collectors.toList());

        double totalWeight = data.stream().mapToDouble(v -> v.t1() * v.t2()).sum();
        double totalScale = data.stream().mapToDouble(Tuple2::t1).sum();

        if (totalScale < 0.000000001d) {
            return 0L;
        }

        double p50 = (totalWeight / totalScale) * durationFactor;

        return p50;
    }

    private double getHandlerCommit50PercDurationMs() {
        return p50DurationMs(handlerCommit);
    }

    private double getHandlerProcess50PercDurationMs() {
        return p50DurationMs(handlerProcess);
    }

    private double getHandlerRollback50PercDurationMs() {
        return p50DurationMs(handlerRollback);
    }

    private double getKafkaFetch50PercDurationMs() {
        return p50DurationMs(kafkaFetch);
    }

    private double getKafkaCommit50PercDurationMs() {
        return p50DurationMs(kafkaCommit);
    }

    private String metricName(String type) {
        return MetricNameBuilder.of(getClass())
                .and("metric", type)
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void onGaugeAdded(String name, Gauge<?> gauge) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("consumerActive".equals(metricType)) {
            consumerActive.put(name, (Gauge<Integer>) gauge);
        }
        if ("messagesBehind".equals(metricType)) {
            messagesBehind.put(name, (Gauge<Long>) gauge);
        }
        if ("lastFetched".equals(metricType)) {
            lastFetched.put(name, (Gauge<Date>) gauge);
        }
    }

    @Override
    public void onGaugeRemoved(String name) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("consumerActive".equals(metricType)) {
            consumerActive.remove(name);
        }
        if ("messagesBehind".equals(metricType)) {
            messagesBehind.remove(name);
        }
        if ("lastFetched".equals(metricType)) {
            lastFetched.remove(name);
        }
    }

    @Override
    public void onCounterAdded(String name, Counter counter) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("uncommittedMessages".equals(metricType)) {
            uncommittedMessages.put(name, counter);
        }
    }

    @Override
    public void onCounterRemoved(String name) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("uncommittedMessages".equals(metricType)) {
            uncommittedMessages.remove(name);
        }
    }

    @Override
    public void onTimerAdded(String name, Timer timer) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("handlerCommit".equals(metricType)) {
            handlerCommit.put(name, timer);
        }
        if ("handlerProcess".equals(metricType)) {
            handlerProcess.put(name, timer);
        }
        if ("handlerRollback".equals(metricType)) {
            handlerRollback.put(name, timer);
        }
        if ("kafkaFetch".equals(metricType)) {
            kafkaFetch.put(name, timer);
        }
        if ("kafkaCommit".equals(metricType)) {
            kafkaCommit.put(name, timer);
        }
    }

    @Override
    public void onTimerRemoved(String name) {
        if (!isConsumerMetric(name)) {
            return;
        }

        String metricType = metricType(name);
        if ("handlerCommit".equals(metricType)) {
            handlerCommit.remove(name);
        }
        if ("handlerProcess".equals(metricType)) {
            handlerProcess.remove(name);
        }
        if ("handlerRollback".equals(metricType)) {
            handlerRollback.remove(name);
        }
        if ("kafkaFetch".equals(metricType)) {
            kafkaFetch.remove(name);
        }
        if ("kafkaCommit".equals(metricType)) {
            kafkaCommit.remove(name);
        }
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
}
