FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

ADD target/service-hml-fhir-converter-api-1.1.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /app.jar"]