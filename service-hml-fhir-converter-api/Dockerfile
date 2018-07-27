FROM java:8

VOLUME /usr

ADD target/service-hml-fhir-converter-api-2.0.0.jar app.jar
ADD conf/kafka-configuration.yaml /usr/conf/kafka-configuration.yaml
ADD conf/mongo-configuration.yaml /usr/conf/mongo-configuration.yaml

EXPOSE 8099

RUN bash -c 'touch /app.jar'

ENV JAVA_OPTS=""

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
