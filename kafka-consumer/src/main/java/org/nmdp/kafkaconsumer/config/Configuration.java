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

public class Configuration {
    public String bootstrapServers;
    public String consumerGroup;
    public int sessionTimeout;
    public int heartBeatInterval;
    public int minBytes;
    public String autoOffsetReset;
    public int maxWait;
    public int maxPartitionFetchBytes;
    public int sendBufferBytes;
    public int receiveBufferBytes;
    public String clientId;
    public int reconnectBackoffMs;
    public int retryBackoffMs;
    public long metadataMaxAgeMs;
    public long connectionMaxIdleMs;
    public int requestTimeoutMs;
    public int maxMessagesBeforeCommit;
    public int hwmRefreshIntervalMs;
    public int maxPollRecords;
    public int maxTimeBeforeCommit;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(int heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public int getMinBytes() {
        return minBytes;
    }

    public void setMinBytes(int minBytes) {
        this.minBytes = minBytes;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxPartitionFetchBytes() {
        return maxPartitionFetchBytes;
    }

    public void setMaxPartitionFetchBytes(int maxPartitionFetchBytes) {
        this.maxPartitionFetchBytes = maxPartitionFetchBytes;
    }

    public int getSendBufferBytes() {
        return sendBufferBytes;
    }

    public void setSendBufferBytes(int sendBufferBytes) {
        this.sendBufferBytes = sendBufferBytes;
    }

    public int getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public void setReceiveBufferBytes(int receiveBufferBytes) {
        this.receiveBufferBytes = receiveBufferBytes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getReconnectBackoffMs() {
        return reconnectBackoffMs;
    }

    public void setReconnectBackoffMs(int reconnectBackoffMs) {
        this.reconnectBackoffMs = reconnectBackoffMs;
    }

    public int getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public void setRetryBackoffMs(int retryBackoffMs) {
        this.retryBackoffMs = retryBackoffMs;
    }

    public long getMetadataMaxAgeMs() {
        return metadataMaxAgeMs;
    }

    public void setMetadataMaxAgeMs(long metadataMaxAgeMs) {
        this.metadataMaxAgeMs = metadataMaxAgeMs;
    }

    public long getConnectionMaxIdleMs() {
        return connectionMaxIdleMs;
    }

    public void setConnectionMaxIdleMs(long connectionMaxIdleMs) {
        this.connectionMaxIdleMs = connectionMaxIdleMs;
    }

    public int getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(int requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public int getMaxMessagesBeforeCommit() {
        return maxMessagesBeforeCommit;
    }

    public void setMaxMessagesBeforeCommit(int maxMessagesBeforeCommit) {
        this.maxMessagesBeforeCommit = maxMessagesBeforeCommit;
    }

    public int getHwmRefreshIntervalMs() {
        return hwmRefreshIntervalMs;
    }

    public void setHwmRefreshIntervalMs(int hwmRefreshIntervalMs) {
        this.hwmRefreshIntervalMs = hwmRefreshIntervalMs;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public int getMaxTimeBeforeCommit() {
        return maxTimeBeforeCommit;
    }

    public void setMaxTimeBeforeCommit(int maxTimeBeforeCommit) {
        this.maxTimeBeforeCommit = maxTimeBeforeCommit;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", heartBeatInterval=" + heartBeatInterval +
                ", minBytes=" + minBytes +
                ", autoOffsetReset='" + autoOffsetReset + '\'' +
                ", maxWait=" + maxWait +
                ", maxPartitionFetchBytes=" + maxPartitionFetchBytes +
                ", sendBufferBytes=" + sendBufferBytes +
                ", receiveBufferBytes=" + receiveBufferBytes +
                ", clientId='" + clientId + '\'' +
                ", reconnectBackoffMs=" + reconnectBackoffMs +
                ", retryBackoffMs=" + retryBackoffMs +
                ", metadataMaxAgeMs=" + metadataMaxAgeMs +
                ", connectionMaxIdleMs=" + connectionMaxIdleMs +
                ", requestTimeoutMs=" + requestTimeoutMs +
                ", maxMessagesBeforeCommit=" + maxMessagesBeforeCommit +
                ", hwmRefreshIntervalMs=" + hwmRefreshIntervalMs +
                ", maxPollRecords=" + maxPollRecords +
                '}';
    }
}
