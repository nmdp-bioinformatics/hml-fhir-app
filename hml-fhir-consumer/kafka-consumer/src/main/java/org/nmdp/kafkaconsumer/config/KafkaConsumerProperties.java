package org.nmdp.kafkaconsumer.config;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumerProperties {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerProperties.class);

    private final String bootstrapServers;
    private final String consumerGroup;
    private final int sessionTimeout;
    private final int heartbeatInterval;
    private final int minBytes;
    private final String autoOffsetReset;
    private final int maxWait;
    private final int maxPartitionFetchBytes;
    private final int sendBufferBytes;
    private final int receiveBufferBytes;
    private final String clientId;
    private final int reconnectBackoffMs;
    private final int retryBackoffMs;
    private final long metadataMaxAgeMs;
    private final long connectionsMaxIdleMs;
    private final int requestTimeoutMs;
    private final int hwmRefreshIntervalMs;
    private final int maxPollRecords;

    // non-Kafka properties
    private final int maxMessagesBeforeCommit;
    private final int maxTimeBeforeCommit;

    private KafkaConsumerProperties(String bootstrapServers, String consumerGroup, int sessionTimeout, int heartbeatInterval,
                                    int minBytes, String autoOffsetReset, int maxWait, int maxPartitionFetchBytes, int sendBufferBytes,
                                    int receiveBufferBytes, String clientId, int reconnectBackoffMs, int retryBackoffMs, long metadataMaxAgeMs,
                                    long connectionsMaxIdleMs, int requestTimeoutMs, int maxMessagesBeforeCommit, int maxTimeBeforeCommit,
                                    int hwmRefreshIntervalMs, int maxPollRecords) {

        this.bootstrapServers = bootstrapServers;
        this.consumerGroup = consumerGroup;
        this.sessionTimeout = (sessionTimeout < 1) ? 1 : sessionTimeout;
        this.heartbeatInterval = (heartbeatInterval < 1) ? 1 : heartbeatInterval;
        this.autoOffsetReset = autoOffsetReset;
        this.maxWait = (maxWait < 0) ? 0 : maxWait;
        this.maxPartitionFetchBytes = (maxPartitionFetchBytes < 1) ? 1 : maxPartitionFetchBytes;
        this.minBytes = (minBytes < 0) ? 0 : (minBytes > maxPartitionFetchBytes) ? maxPartitionFetchBytes : minBytes;
        this.sendBufferBytes = (sendBufferBytes < 1) ? 1 : sendBufferBytes;
        this.receiveBufferBytes = (receiveBufferBytes < 1) ? 1 : receiveBufferBytes;
        this.clientId = clientId;
        this.reconnectBackoffMs = (reconnectBackoffMs < 1) ? 1 : reconnectBackoffMs;
        this.retryBackoffMs = (retryBackoffMs < 1) ? 1 : retryBackoffMs;
        this.metadataMaxAgeMs = (metadataMaxAgeMs < 1000L) ? 1000L : metadataMaxAgeMs;
        this.connectionsMaxIdleMs = (connectionsMaxIdleMs < 1000L) ? 1000L : connectionsMaxIdleMs;
        this.requestTimeoutMs = (requestTimeoutMs < 1) ? 1 : requestTimeoutMs;
        this.maxPollRecords = (maxPollRecords < 1) ? 1 : maxPollRecords;

        this.maxMessagesBeforeCommit = (maxMessagesBeforeCommit < 1) ? 1 : maxMessagesBeforeCommit;
        this.maxTimeBeforeCommit = (maxTimeBeforeCommit < 0) ? 0 : maxTimeBeforeCommit;
        this.hwmRefreshIntervalMs = (hwmRefreshIntervalMs < 1000) ? 1000 : hwmRefreshIntervalMs;

        if (bootstrapServers == null || bootstrapServers.trim().length() == 0) {
            throw new IllegalArgumentException("bootstrapServers is required");
        }

        if (consumerGroup == null || consumerGroup.trim().length() == 0) {
            throw new IllegalArgumentException("consumerGroup is required");
        }

        if (consumerGroup.contains("/")) {
            throw new IllegalArgumentException("consumerGroup cannot contain slashes");
        }
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public int getMaxPartitionFetchBytes() {
        return maxPartitionFetchBytes;
    }

    public int getSendBufferBytes() {
        return sendBufferBytes;
    }

    public int getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public String getClientId() {
        return clientId;
    }

    public int getMinBytes() {
        return minBytes;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public int getReconnectBackoffMs() {
        return reconnectBackoffMs;
    }

    public int getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public long getMetadataMaxAgeMs() {
        return metadataMaxAgeMs;
    }

    public long getConnectionsMaxIdleMs() {
        return connectionsMaxIdleMs;
    }

    public int getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public int getMaxMessagesBeforeCommit() {
        return maxMessagesBeforeCommit;
    }

    public int getMaxTimeBeforeCommit() {
        return maxTimeBeforeCommit;
    }

    public int getHwmRefreshIntervalMs() {
        return hwmRefreshIntervalMs;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    /**
     * @deprecated use {@link #getSessionTimeout()} instead
     */
    @Deprecated
    public int getSoTimeout() {
        return getSessionTimeout();
    }

    /**
     * @deprecated use {@link #getReceiveBufferBytes()} instead
     */
    @Deprecated
    public int getBufferSize() {
        return receiveBufferBytes;
    }

    /**
     * @deprecated use {@link #getMaxPartitionFetchBytes()} instead
     */
    @Deprecated
    public int getMaxMessageSize() {
        return getMaxPartitionFetchBytes();
    }

    /**
     * @deprecated no replacement
     */
    @Deprecated
    public int getThrottleWait() {
        return 1000;
    }

    /**
     * @deprecated no replacement
     */
    public int getRetryTime() {
        return 1000;
    }

    /**
     * @deprecated use {@link #getAutoOffsetReset()} instead
     */
    @Deprecated
    public boolean isRestartFromBeginning() {
        return "earliest".equals(autoOffsetReset);
    }

    /**
     * @deprecated no replacement
     */
    @Deprecated
    public int getBalancerFrequency() {
        return Integer.MAX_VALUE;
    }

    /**
     * @deprecated no replacement
     */
    @Deprecated
    public int getUpdateTopologyFrequency() {
        return Integer.MAX_VALUE;
    }

    public static class Builder {
        private static final Map<String, String> OBSOLETE_KEYS;

        static {
            Map<String, String> obsolete = new HashMap<>();
            obsolete.put("consumer.group", "group.id");
            obsolete.put("min.bytes", "fetch.min.bytes");
            obsolete.put("restart.from.beginning", "auto.offset.reset");
            obsolete.put("max.wait.ms", "fetch.max.wait.ms");
            obsolete.put("socket.timeout.ms", "session.timeout.ms");
            obsolete.put("buffer.size", "receive.buffer.bytes");
            obsolete.put("max.message.size", "max.partition.fetch.bytes");
            obsolete.put("throttle.wait.ms", null);
            obsolete.put("retry.time.ms", null);

            OBSOLETE_KEYS = Collections.unmodifiableMap(obsolete);
        }

        private String bootstrapServers;
        private String consumerGroup;
        private int sessionTimeout = 30 * 1000;
        private int heartbeatInterval = 3 * 1000;
        private int minBytes = 0;
        private int maxWait = 0;
        private String autoOffsetReset = "latest";
        private int maxPartitionFetchBytes = 1 * 1024 * 1024;
        private int sendBufferBytes = 128 * 1024;
        private int receiveBufferBytes = 32 * 1024;
        private String clientId = "";
        private int reconnectBackoffMs = 50;
        private int retryBackoffMs = 100;
        private long metadataMaxAgeMs = 5L * 60L * 1000L;
        private long connectionsMaxIdleMs = 9L * 60L * 1000L;
        private int requestTimeoutMs = 40 * 1000;
        private int hwmRefreshIntervalMs = 30 * 1000;
        private int maxPollRecords = Integer.MAX_VALUE;

        // non-Kafka properties

        private int maxMessagesBeforeCommit = 1000;
        private int maxTimeBeforeCommit = 10 * 1000;

        private Builder() {
        }

        public Builder configure(Properties props) {
            return configure(props, "kafka.");
        }

        public Builder configure(Properties props, String prefix) {
            if (prefix == null) {
                prefix = "";
            }

            warnAboutObsoleteProperties(props, prefix);

            bootstrapServers = getStringProperty(props, prefix, "bootstrap.servers", bootstrapServers);
            consumerGroup = getStringProperty(props, prefix, "consumer.group", consumerGroup); // deprecated
            consumerGroup = getStringProperty(props, prefix, "group.id", consumerGroup); // new name

            sessionTimeout = getIntProperty(props, prefix, "socket.timeout.ms", sessionTimeout); // deprecated
            sessionTimeout = getIntProperty(props, prefix, "session.timeout.ms", sessionTimeout);

            heartbeatInterval = getIntProperty(props, prefix, "heartbeat.interval.ms", heartbeatInterval);

            minBytes = getIntProperty(props, prefix, "min.bytes", minBytes); // deprecated
            minBytes = getIntProperty(props, prefix, "fetch.min.bytes", minBytes);

            if (props.containsKey(prefix + "restart.from.beginning")) {
                boolean restartFromBeginning = getBooleanProperty(props, prefix, "restart.from.beginning", false);
                autoOffsetReset = restartFromBeginning ? "earliest" : "latest";
            }

            autoOffsetReset = getStringProperty(props, prefix, "auto.offset.reset", autoOffsetReset);

            maxWait = getIntProperty(props, prefix, "max.wait.ms", maxWait); // deprecated
            maxWait = getIntProperty(props, prefix, "fetch.max.wait.ms", maxWait);

            maxPartitionFetchBytes = getIntProperty(props, prefix, "max.message.size", maxPartitionFetchBytes); // deprecated
            maxPartitionFetchBytes = getIntProperty(props, prefix, "max.partition.fetch.bytes", maxPartitionFetchBytes);

            sendBufferBytes = getIntProperty(props, prefix, "send.buffer.bytes", sendBufferBytes);
            receiveBufferBytes = getIntProperty(props, prefix, "receive.buffer.bytes", receiveBufferBytes);
            clientId = getStringProperty(props, prefix, "client.id", clientId);
            reconnectBackoffMs = getIntProperty(props, prefix, "reconnect.backoff.ms", reconnectBackoffMs);
            retryBackoffMs = getIntProperty(props, prefix, "retry.backoff.ms", retryBackoffMs);
            metadataMaxAgeMs = getLongProperty(props, prefix, "metadata.max.age.ms", metadataMaxAgeMs);
            connectionsMaxIdleMs = getLongProperty(props, prefix, "connections.max.idle.ms", connectionsMaxIdleMs);
            requestTimeoutMs = getIntProperty(props, prefix, "request.timeout.ms", requestTimeoutMs);

            maxMessagesBeforeCommit = getIntProperty(props, prefix, "max.messages.before.commit", maxMessagesBeforeCommit);
            maxTimeBeforeCommit = getIntProperty(props, prefix, "max.time.before.commit.ms", maxTimeBeforeCommit);
            hwmRefreshIntervalMs = getIntProperty(props, prefix, "hwm.refresh.interval.ms", hwmRefreshIntervalMs);

            maxPollRecords = getIntProperty(props, prefix, "max.poll.records", maxPollRecords);

            return this;
        }

        private void warnAboutObsoleteProperties(Properties props, String prefix) {
            for (String key : OBSOLETE_KEYS.keySet()) {
                if (props.containsKey(prefix + key)) {
                    String replacement = OBSOLETE_KEYS.get(key);
                    if (replacement == null) {
                        LOG.warn("Property " + prefix + key + " is deprecated.");
                    } else {
                        LOG.warn("Property " + prefix + key + " is deprecated. Use " + prefix + replacement + " instead.");
                    }
                }
            }
        }

        private String getStringProperty(Properties props, String prefix, String key, String defaultValue) {
            return props.getProperty(prefix + key, defaultValue);
        }

        private int getIntProperty(Properties props, String prefix, String key, int defaultValue) {
            return Integer.parseInt(props.getProperty(prefix + key, Integer.toString(defaultValue, 10)), 10);
        }

        private long getLongProperty(Properties props, String prefix, String key, long defaultValue) {
            return Long.parseLong(props.getProperty(prefix + key, Long.toString(defaultValue, 10)), 10);
        }

        private boolean getBooleanProperty(Properties props, String prefix, String key, boolean defaultValue) {
            return Boolean.parseBoolean(props.getProperty(prefix + key, Boolean.toString(defaultValue)));
        }

        public Builder bootstrapServers(String value) {
            bootstrapServers = value;
            return this;
        }

        public Builder consumerGroup(String value) {
            consumerGroup = value;
            return this;
        }

        public Builder sessionTimeout(int value) {
            sessionTimeout = value;
            return this;
        }

        public Builder heartbeatInterval(int value) {
            heartbeatInterval = value;
            return this;
        }

        public Builder autoOffsetReset(String value) {
            autoOffsetReset = value;
            return this;
        }

        public Builder maxPartitionFetchBytes(int value) {
            maxPartitionFetchBytes = value;
            return this;
        }

        public Builder sendBufferBytes(int value) {
            sendBufferBytes = value;
            return this;
        }

        public Builder receiveBufferBytes(int value) {
            receiveBufferBytes = value;
            return this;
        }

        public Builder clientId(String value) {
            clientId = value;
            return this;
        }

        public Builder maxWait(int value) {
            maxWait = value;
            return this;
        }

        public Builder minBytes(int value) {
            minBytes = value;
            return this;
        }

        public Builder reconnectBackoffMs(int value) {
            reconnectBackoffMs = value;
            return this;
        }

        public Builder retryBackoffMs(int value) {
            retryBackoffMs = value;
            return this;
        }

        public Builder metadataMaxAgeMs(long value) {
            metadataMaxAgeMs = value;
            return this;
        }

        public Builder connectionsMaxIdleMs(long value) {
            connectionsMaxIdleMs = value;
            return this;
        }

        public Builder requestTimeoutMs(int value) {
            requestTimeoutMs = value;
            return this;
        }

        public Builder maxMessagesBeforeCommit(int value) {
            maxMessagesBeforeCommit = value;
            return this;
        }

        public Builder maxTimeBeforeCommit(int value) {
            maxTimeBeforeCommit = value;
            return this;
        }

        public Builder hwmRefreshIntervalMs(int value) {
            hwmRefreshIntervalMs = value;
            return this;
        }

        public Builder maxPollRecords(int value) {
            maxPollRecords = value;
            return this;
        }

        /**
         * @deprecated use {@link #sessionTimeout(int)} instead
         */
        @Deprecated
        public Builder soTimeout(int value) {
            sessionTimeout = value;
            return this;
        }

        /**
         * @deprecated use {@link #receiveBufferBytes(int)} instead
         */
        @Deprecated
        public Builder bufferSize(int value) {
            receiveBufferBytes = value;
            return this;
        }

        /**
         * @deprecated use {@link #maxPartitionFetchBytes(int)} instead
         */
        @Deprecated
        public Builder maxMessageSize(int value) {
            maxPartitionFetchBytes = value;
            return this;
        }

        /**
         * Does nothing.
         *
         * @deprecated no replacement
         */
        @Deprecated
        public Builder throttleWait(int value) {
            return this;
        }

        /**
         * Does nothing.
         *
         * @deprecated no replacement
         */
        public Builder retryTime(int value) {
            return this;
        }

        /**
         * Does nothing.
         *
         * @deprecated no replacement
         */
        @Deprecated
        public Builder balancerFrequency(int value) {
            return this;
        }

        /**
         * Does nothing.
         *
         * @deprecated no replacement
         */
        @Deprecated
        public Builder updateTopologyFrequency(int value) {
            return this;
        }

        /**
         * @deprecated Use {@link #autoOffsetReset(String)} instead
         */
        @Deprecated
        public Builder restartFromBeginning(boolean value) {
            autoOffsetReset = value ? "earliest" : "latest";
            return this;
        }

        public KafkaConsumerProperties build() {
            return new KafkaConsumerProperties(
                    bootstrapServers, consumerGroup, sessionTimeout, heartbeatInterval, minBytes, autoOffsetReset, maxWait,
                    maxPartitionFetchBytes, sendBufferBytes, receiveBufferBytes, clientId, reconnectBackoffMs, retryBackoffMs,
                    metadataMaxAgeMs, connectionsMaxIdleMs, requestTimeoutMs, maxMessagesBeforeCommit, maxTimeBeforeCommit,
                    hwmRefreshIntervalMs, maxPollRecords);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
