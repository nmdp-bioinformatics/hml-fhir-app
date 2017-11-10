package org.nmdp.kafkaconsumer.consumer;

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

import org.apache.kafka.clients.consumer.RangeAssignor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.nmdp.kafkaconsumer.config.KafkaConsumerProperties;
import org.nmdp.kafkaconsumer.handler.KafkaMessageHandler;

import org.nmdp.kafkaconsumer.metrics.MetricNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class KafkaMessageConsumer extends Thread implements Closeable {

    private static final String NEWLINE = System.getProperty("line.separator");
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    private final AtomicLong messagesBehind = new AtomicLong(0L);
    private final AtomicLong lastFetched = new AtomicLong(System.currentTimeMillis());

    private final KafkaConsumer<byte[], byte[]> consumer;
    private final KafkaMessageHandler handler;
    private final String topic;
    private final String consumerGroup;
    private final String clientId;
    private final long maxWait;
    private final int maxMessagesBeforeCommit;
    private final long maxTimeBeforeCommit;
    private final String brokerId;
    private final int hwmRefreshIntervalMs;

    private final Timer handlerProcess;
    private final Timer handlerCommit;
    private final Timer handlerRollback;
    private final Timer kafkaCommit;
    private final Timer kafkaFetch;

    private final AtomicLong nextRefreshTime = new AtomicLong(0L);

    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final Map<TopicPartition, Long> rollbackOffsets = new ConcurrentHashMap<>();
    private final Map<TopicPartition, OffsetAndMetadata> commitOffsets = new ConcurrentHashMap<>();
    private final Map<TopicPartition, Long> messagesReceived = new ConcurrentHashMap<>();
    private final Map<TopicPartition, Long> nextCommitTime = new ConcurrentHashMap<>();
    private final Map<TopicPartition, Long> highWaterMarks = new ConcurrentHashMap<>();
    private final AtomicReference<Set<TopicPartition>> assignmentCache = new AtomicReference<>(Collections.emptySet());

    public KafkaMessageConsumer(String brokerId, String topic, KafkaMessageHandler handler, MetricRegistry metrics, KafkaConsumerProperties consumerProperties) {
        super("Consumer " + consumerProperties.getClientId() + ":" + consumerProperties.getConsumerGroup() + "@" + brokerId + "://" + topic);

        this.handler = handler;
        this.topic = topic;
        this.consumerGroup = consumerProperties.getConsumerGroup();
        this.clientId = consumerProperties.getClientId();
        this.maxWait = consumerProperties.getMaxWait();
        this.maxMessagesBeforeCommit = consumerProperties.getMaxMessagesBeforeCommit();
        this.maxTimeBeforeCommit = (long) consumerProperties.getMaxTimeBeforeCommit();
        this.brokerId = brokerId;
        this.hwmRefreshIntervalMs = consumerProperties.getHwmRefreshIntervalMs();

        this.handlerProcess = metrics.timer(metricName("handlerProcess"));
        this.handlerCommit = metrics.timer(metricName("handlerCommit"));
        this.handlerRollback = metrics.timer(metricName("handlerRollback"));
        this.kafkaCommit = metrics.timer(metricName("kafkaCommit"));
        this.kafkaFetch = metrics.timer(metricName("kafkaFetch"));

        this.consumer = instantiateKafkaConsumer(consumerProperties);

        metrics.register(metricName("consumerActive"), (Gauge<Integer>) () -> getActiveConsumers());
        metrics.register(metricName("messagesBehind"), (Gauge<Long>) messagesBehind::get);
        metrics.register(metricName("lastFetched"), (Gauge<Date>) () -> new Date(lastFetched.get()));
        metrics.register(metricName("uncommittedMessages"), (Gauge<Long>) () -> getUncommittedMessages());
    }

    private KafkaConsumer<byte[], byte[]> instantiateKafkaConsumer(KafkaConsumerProperties properties) {
        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getConsumerGroup());
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getClientId());
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.getSessionTimeout());
        configs.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, properties.getHeartbeatInterval());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getAutoOffsetReset());
        configs.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, properties.getMinBytes());
        configs.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, properties.getMaxWait());
        configs.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, properties.getMaxPartitionFetchBytes());
        configs.put(ConsumerConfig.SEND_BUFFER_CONFIG, properties.getSendBufferBytes());
        configs.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, properties.getReceiveBufferBytes());
        configs.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, properties.getReconnectBackoffMs());
        configs.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, properties.getRetryBackoffMs());
        configs.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, properties.getMetadataMaxAgeMs());
        configs.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, properties.getConnectionsMaxIdleMs());
        configs.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, properties.getRequestTimeoutMs());
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaxPollRecords());

        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 0);
        configs.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RangeAssignor.class.getName());

        return new KafkaConsumer<>(configs, new ByteArrayDeserializer(), new ByteArrayDeserializer());
    }

    private String metricName(String type) {
        return MetricNameBuilder.of(getClass())
                .and("brokerId", brokerId)
                .and("clientId", clientId)
                .and("group", consumerGroup)
                .and("topic", topic)
                .and("metric", type)
                .build();
    }

    private long getUncommittedMessages() {
        long total = 0L;

        for (TopicPartition tp : assignmentCache.get()) {
            Long value = messagesReceived.get(tp);
            if (value != null) {
                total += value.longValue();
            }
        }

        return total;
    }

    private int getActiveConsumers() {
        return assignmentCache.get().size();
    }

    private long getEstimatedHwm(String topic, int partition, long offset) {
        long hwm = 0L;
        Long lastHwm = highWaterMarks.get(new TopicPartition(topic, partition));
        if (lastHwm == null) {
            hwm = offset + 1L;
        } else {
            hwm = lastHwm.longValue();
            if (hwm < offset) {
                hwm = offset + 1L;
            }
        }
        return hwm;
    }

    private long commit(String topic, int partition, long offset) throws Exception {
        try {
            LOG.debug("Committing messages for {}-{} through offset {}", new Object[] { topic, partition, offset });
            try (Timer.Context context = handlerCommit.time()) {
                handler.commit(topic, partition, offset);
            }

            long hwm = getEstimatedHwm(topic, partition, offset);

            LOG.info("Handler commit on {}-{} for consumer group {} through offset {} (hwm {}, behind {})", new Object[] {
                    topic, partition, consumerGroup, offset, hwm, hwm - offset - 1L });

        } catch (Exception e) {
            LOG.error("Error committing messages in handler", e);
            throw e;
        }

        try {
            try (Timer.Context context = kafkaCommit.time()) {
                TopicPartition tp = new TopicPartition(topic, partition);
                consumer.commitSync(Collections.singletonMap(tp, new OffsetAndMetadata(offset + 1L)));
                clearState(tp);
            }

            long hwm = getEstimatedHwm(topic, partition, offset);

            LOG.info("Kafka commit on {}-{} for consumer group {} through offset {} (hwm {}, behind {})",
                    new Object[] { topic, partition, consumerGroup, offset, hwm, hwm - offset - 1L });
        } catch (Exception e) {
            LOG.error("Unable to commit offsets to Kafka", e);
        }

        return offset + 1L;
    }

    private void refreshHighWaterMarks() {

        long totalBehind = 0L;

        for (TopicPartition tp : consumer.assignment()) {
            try {
                long currentOffset = consumer.position(tp);
                consumer.seekToEnd(Arrays.asList(tp));
                long hwm = consumer.position(tp);
                consumer.seek(tp, currentOffset);
                LOG.debug("HWM for {}-{}: {}", tp.topic(), tp.partition(), hwm);
                LOG.debug("Resetting offset to {} for {}-{}", currentOffset, tp.topic(), tp.partition());

                long behind = Math.max(0L, hwm - currentOffset - 1L);
                totalBehind += behind;

                highWaterMarks.put(tp, hwm);
            } catch (WakeupException e) {
                LOG.debug("Got wakeup exception while retrieving partition high water marks");
                return;
            } catch (Exception e) {
                LOG.error("Error while retrieving partition high water marks", e);
                abortAll();
            }
        }

        messagesBehind.set(totalBehind);
    }

    private void abortAll() {
        rollbackAll();
        clearState();
        consumer.unsubscribe();
        consumer.subscribe(Collections.singletonList(topic), new RebalanceListener());
    }

    @Override
    public void run() {
        nextRefreshTime.set(0L);

        consumer.subscribe(Collections.singletonList(topic), new RebalanceListener());

        while (true) {

            try {
                if (System.currentTimeMillis() >= nextRefreshTime.get()) {
                    refreshHighWaterMarks();
                    nextRefreshTime.set(System.currentTimeMillis() + (long) hwmRefreshIntervalMs);
                }

                if (shutdown.get()) {
                    commitAll();
                    return;
                }

                long fetchTimeElapsed = 0L;
                ConsumerRecords<byte[], byte[]> records = null;
                Timer.Context kafkaFetchContext = kafkaFetch.time();
                try {
                    records = consumer.poll(maxWait);
                    lastFetched.set(System.currentTimeMillis());
                } catch (WakeupException we) {
                    if (shutdown.get()) {
                        continue;
                    }
                    throw we;
                } finally {
                    fetchTimeElapsed = kafkaFetchContext.stop() / 1_000_000L;
                }

                LOG.debug("Fetched {} records in {} ms from topic {}", new Object[] { records.count(), fetchTimeElapsed, topic });

                for (TopicPartition tp : records.partitions()) {

                    List<ConsumerRecord<byte[], byte[]>> partRecords = records.records(tp);
                    if (partRecords.isEmpty()) {
                        continue;
                    }

                    for (ConsumerRecord<byte[], byte[]> record : partRecords) {

                        long commitTime = System.currentTimeMillis() + maxTimeBeforeCommit;
                        nextCommitTime.compute(tp, (k, v) -> (v == null) ? commitTime : Math.min(v.longValue(), commitTime));

                        try {
                            try (Timer.Context context = handlerProcess.time()) {
                                handler.process(tp.topic(), tp.partition(), record.offset(), record.key(), record.value());
                            }
                        } catch (Exception e) {
                            // contract of handler is that errors are not recoverable
                            LOG.error("Error in message handler", e);
                        }

                        rollbackOffsets.compute(tp, (k, v) -> (v == null) ? record.offset() : Math.max(v.longValue(), record.offset()));
                        messagesReceived.compute(tp,
                                (k, v) -> (v == null) ? (long) 1L : v.longValue() + (long) 1L);

                        commitOffsets.compute(tp, (k, v) -> new OffsetAndMetadata(record.offset()));

                        Long currentMessagesReceived = messagesReceived.get(tp);
                        Long currentCommitTime = nextCommitTime.get(tp);
                        if (currentMessagesReceived != null && currentCommitTime != null &&
                                needsCommit(tp.topic(), tp.partition(), currentMessagesReceived.intValue(),
                                        currentCommitTime.longValue())) {
                            handleCommit(tp);
                        }

                    }
                }

                for (TopicPartition tp : consumer.assignment()) {
                    Long currentMessagesReceived = messagesReceived.get(tp);
                    Long currentCommitTime = nextCommitTime.get(tp);
                    if (currentMessagesReceived != null && currentCommitTime != null &&
                            needsCommit(tp.topic(), tp.partition(), currentMessagesReceived.intValue(), currentCommitTime.longValue())) {
                            handleCommit(tp);
                    }
                }

            } catch (WakeupException we) {
                if (shutdown.get()) {
                    commitAll();
                    return;
                }
                throw we;
            } catch (AuthorizationException ae) {
                LOG.error("Authorization failed", ae);
                throw ae;
            } catch (Throwable t) {
                LOG.error("Caught exception", t);
                rollbackAll();
            }
        }

    }

    private void handleCommit(TopicPartition tp) {
        try {
            OffsetAndMetadata oam = commitOffsets.get(tp);
            if (oam != null) {
                commit(tp.topic(), tp.partition(), oam.offset());
                clearState(tp);
            }
        } catch (Exception ex) {
            LOG.error("Error while committing offsets to " + tp.topic() + "-" + tp.partition() + ": ", ex);
            abortAll();
        }
    }

    protected void commitAll() {
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : commitOffsets.entrySet()) {
            TopicPartition tp = entry.getKey();
            OffsetAndMetadata oam = entry.getValue();
            if (oam != null) {
                try {
                    commit(tp.topic(), tp.partition(), oam.offset());
                } catch (Exception e) {
                    LOG.error("Error while committing offsets to " + tp.topic() + "-" + tp.partition() + ": ", e);

                }
            }
        }
        commitOffsets.clear();
    }

    private void clearState(TopicPartition tp) {
        rollbackOffsets.remove(tp);
        commitOffsets.remove(tp);
        messagesReceived.remove(tp);
        nextCommitTime.remove(tp);
    }

    private void clearState() {
        rollbackOffsets.clear();
        commitOffsets.clear();
        messagesReceived.clear();
        nextCommitTime.clear();
    }

    private void rollbackAll() {
        for (Map.Entry<TopicPartition, Long> entry : rollbackOffsets.entrySet()) {
            TopicPartition tp = entry.getKey();
            Long offset = entry.getValue();
            if (offset != null) {
                rollback(tp.topic(), tp.partition(), offset.longValue());
            }
        }
    }

    private void rollback(String topic, int partition, long offset) {
        try {
            LOG.debug("Rolling back messages for {}-{} before offset {}", new Object[] { topic, partition, offset });
            try (Timer.Context context = handlerRollback.time()) {
                handler.rollback(topic, partition, offset - 1L);
            }
            LOG.info("Handler rollback on {}-{} for consumer group {} from offset {}", new Object[] { topic, partition, consumerGroup,
                    offset });
        } catch (Exception e) {
            LOG.error("Error while rolling back message handler", e);
        }
        clearState(new TopicPartition(topic, partition));
    }

    private boolean needsCommit(String topic, int partition, int messagesSinceLastCommit, long nextCommitTime) {
        boolean needsCommit = false;

        if (messagesSinceLastCommit >= maxMessagesBeforeCommit) {
            LOG.debug("Commit required on {}-{} due to maxMessagesBeforeCommit: {} >= {}", topic, partition, messagesSinceLastCommit,
                    maxMessagesBeforeCommit);
            needsCommit = true;
        }

        if (System.currentTimeMillis() >= nextCommitTime && messagesSinceLastCommit >= 1) {
            LOG.debug("Commit required on {}-{} due to maximum amount of time reached: {} ms", topic, partition, maxTimeBeforeCommit);
            needsCommit = true;
        }
        return needsCommit;
    }

    @Override
    public String toString() {
        return "KafkaMessageConsumer[clientId=" + clientId + ",consumerGroup=" + consumerGroup + ",brokerId=" + brokerId + ",topic="
                + topic + "]";
    }

    @Override
    public synchronized void close() throws IOException {
        if (closed.get()) {
            LOG.warn("Consumer {}:{}@{}://{} already closed", clientId, consumerGroup, brokerId, topic);
            return;
        }

        shutdown.set(true);
        LOG.info("Shutting down consumer {}:{}@{}://{}", clientId, consumerGroup, brokerId, topic);
        consumer.wakeup();
        do {
            try {
                LOG.info("Waiting for consumer {}:{}@{}://{}", clientId, consumerGroup, brokerId, topic);
                join(1000L);
            } catch (InterruptedException e) {
                throw new IOException("Interrupted", e);
            }
        } while (isAlive());
        LOG.info("Consumer {}:{}@{}://{} shutdown.", clientId, consumerGroup, brokerId, topic);

        consumer.close();

        closed.set(true);
    }

    private final class RebalanceListener implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            StringBuilder buf = new StringBuilder();
            buf.append("Partitions revoked:");
            for (TopicPartition tp : partitions) {
                buf.append(NEWLINE);
                buf.append("    ");
                buf.append(tp.toString());
            }
            LOG.info(buf.toString());

            for (TopicPartition tp : partitions) {

                OffsetAndMetadata oam = commitOffsets.get(tp);
                if (oam != null) {
                    try {
                        commit(tp.topic(), tp.partition(), oam.offset());
                    } catch (Exception e) {
                        LOG.error("Error while committing offsets to " + tp.topic() + "-" + tp.partition() + ": ", e);
                    }
                }

                clearState(tp);
            }

            assignmentCache.set(consumer.assignment());
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            StringBuilder buf = new StringBuilder();
            buf.append("Partitions assigned:");
            for (TopicPartition tp : partitions) {
                buf.append(NEWLINE);
                buf.append("    ");
                buf.append(tp.toString());
            }
            LOG.info(buf.toString());

            nextRefreshTime.set(0L);

            assignmentCache.set(consumer.assignment());
        }
    }
}
