FROM openjdk:8-jdk-alpine

VOLUME /usr

COPY kafka_2.12-0.10.2.1/bin/kafka-topics.sh kafka-topics.sh
COPY kafka_2.12-0.10.2.1/bin/kafka-run-class.sh kafka-run-class.sh

ENTRYPOINT ["sh", "kafka-topics.sh", "--create", "--zookeeper", "zookeeper:2181", "--replication-factor", "1", "--partitions", "1", "--topic", "fhir-submission"]
