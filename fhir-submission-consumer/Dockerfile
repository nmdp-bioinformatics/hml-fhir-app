FROM openjdk:8-jdk-alpine

RUN apk add --no-cache curl tar bash

ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DRIR="/root"

RUN mkdir -p /usr/share/maven && \
	curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
	ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
	
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2" 
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
ENTRYPOINT ["/usr/bin/mvn"]

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

ADD hml-fhir-converter/target/hml-fhir-2.0.0-SNAPSHOT.jar /usr/src/app/
ADD hml-fhir-converter-mongo/target/hml-fhir-mongo-1.3.0-SNAPSHOT.jar /usr/src/app/
ADD kafka-consumer/target/kafka-consumer-1.2.0-SNAPSHOT.jar /usr/src/app/
ADD service-hml-fhir-converter-models/target/service-hml-fhir-converter-models-2.0.0-SNAPSHOT.jar /usr/src/app/
ADD service-kafka-producer-model/target/service-kafka-producer-model-2.0.0-SNAPSHOT.jar /usr/src/app/
ADD fhir-submission/target/fhir-submission-1.0.0-SNAPSHOT.jar /usr/src/app/

RUN mvn install:install-file -Dfile=/usr/src/app/service-hml-fhir-converter-models-2.0.0-SNAPSHOT.jar -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=service-hml-fhir-converter-models -DgroupId=org.nmdp 
RUN mvn install:install-file -Dfile=/usr/src/app/service-kafka-producer-model-2.0.0-SNAPSHOT.jar -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=service-kafka-producer-model -DgroupId=org.nmdp 
RUN mvn install:install-file -Dfile=/usr/src/app/hml-fhir-mongo-1.3.0-SNAPSHOT.jar -Dversion=1.3.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=hml-fhir-mongo -DgroupId=org.nmdp 
RUN mvn install:install-file -Dfile=/usr/src/app/kafka-consumer-1.2.0-SNAPSHOT.jar -Dversion=1.2.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=kafka-consumer -DgroupId=org.nmdp 
RUN mvn install:install-file -Dfile=/usr/src/app/hml-fhir-2.0.0-SNAPSHOT.jar -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=hml-fhir -DgroupId=org.nmdp 
RUN mvn install:install-file -Dfile=/usr/src/app/fhir-submission-1.0.0-SNAPSHOT.jar -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true -DartifactId=fhir-submission -DgroupId=org.nmdp

ADD process-kafka-fhir-submission-consumer/ /usr/src/app/process-kafka-fhir-submission-consumer/

WORKDIR /usr/src/app/process-kafka-fhir-submission-consumer

RUN mvn clean install

ADD target/process-kafka-fhir-submission-consumer-1.0.0-SNAPSHOT-jar-with-dependencies.jar /user/src/app/app.jar

ENTRYPOINT [ "sh", "-c", "java -Djava.security.edg=file:/dev/./urandom -jar app.jar" ]