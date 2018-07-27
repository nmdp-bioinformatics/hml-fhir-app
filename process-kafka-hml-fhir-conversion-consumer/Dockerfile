FROM openjdk:8-jdk-alpine

VOLUME /usr

ADD target/process-kafka-hml-fhir-conversion-consumer-2.0.0-jar-with-dependencies.jar app.jar
ADD conf/hml-fhir-converter-consumer-configuration.yaml /usr/conf/hml-fhir-converter-consumer-configuration.yaml
ADD conf/mongo-configuration.yaml /usr/conf/mongo-configuration.yaml
ADD conf/url-config.yaml /usr/conf/url-config.yaml

ENTRYPOINT ["java", "-jar", "app.jar"]
