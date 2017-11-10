FROM openjdk:8-jdk-alpine

VOLUME /usr

ADD target/process-kafka-fhir-submission-consumer-1.0.0-SNAPSHOT-jar-with-dependencies.jar app.jar
ADD conf/fhir-submission-consumer-configuration.yaml /usr/conf/fhir-submission-consumer-configuration.yaml
ADD conf/mongo-configuration.yaml /usr/conf/mongo-configuration.yaml
ADD conf/url-config.yaml /usr/conf/url-config.yaml

ENTRYPOINT ["java", "-jar", "app.jar"]