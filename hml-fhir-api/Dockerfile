FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

ADD service-hml-fhir-converter-api/target/service-fhml-fhir-converter-api-1.0.0-SNAPSHOT-jar-with-dependencies.jar /usr/src/app/app.jar

ENTRYPOINT [ "sh", "-c", "java -Djava.security.edg=file:/dev/./urandom -jar app.jar" ]