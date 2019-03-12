package org.nmdp.hmlfhirconverterapi.config;

/**
 * hml-fhir-app
 */


import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static final Logger LOG = Logger.getLogger(ApplicationProperties.class);

    private static final String KAFKA_CONFIGURATION_PATH_KEY = "kafka.configuration.path";
    private static final String MONGO_CONFIGURATION_PATH_KEY = "mongo.configuration.path";

    private static final String APPLICATION_PROPERTIES_PATH = "/application.properties";

    private final String kafkaConfigurationPath;
    private final String mongoConfigurationPath;

    public ApplicationProperties() throws IOException {
        Properties properties = new Properties();

        LOG.info(String.format("Loading application properteis file from: %s", APPLICATION_PROPERTIES_PATH));

        try (InputStream stream = getClass().getResourceAsStream(APPLICATION_PROPERTIES_PATH)) {
            properties.load(stream);
        }

        LOG.info(String.format("Successfully loaded properties file: %s", properties.toString()));

        this.kafkaConfigurationPath = properties.getProperty(KAFKA_CONFIGURATION_PATH_KEY);
        this.mongoConfigurationPath = properties.getProperty(MONGO_CONFIGURATION_PATH_KEY);
    }

    public String getKafkaConfigurationPath() {
        return kafkaConfigurationPath;
    }

    public String getMongoConfigurationPath() {
        return mongoConfigurationPath;
    }
}
