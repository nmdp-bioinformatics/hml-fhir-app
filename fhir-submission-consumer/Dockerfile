FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

ADD process-kafka-fhir-submission-consumer/target/process-kafka-fhir-submission-consumer-1.0.0-SNAPSHOT-jar-with-dependencies.jar /usr/src/app/app.jar

ENTRYPOINT [ "sh", "-c", "java -Djava.security.edg=file:/dev/./urandom -jar app.jar" ]