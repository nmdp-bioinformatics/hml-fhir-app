package org.nmdp.kafkafhirsubmissionconsumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * hml-fhir-app
 */

public class ApplicationProperties {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final String CONSUMER_CONFIGURATION_PATH_KEY = "consumer.configuration.path";
    private static final String MONGO_CONFIGURATION_PATH_KEY = "mongo.configuration.path";
    private static final String URL_CONFIG_PATH = "url.config.path";

    private final String consumerConfigurationPath;
    private final String mongoConfigurationPath;
    private final String urlConfigPath;

    public ApplicationProperties(String applicationPropertiesPath) throws IOException {
        Properties properties = new Properties();

        LOG.info(String.format("Loading application properteis file from: %s", applicationPropertiesPath));

        try (InputStream stream = getClass().getResourceAsStream(applicationPropertiesPath)) {
            properties.load(stream);
        }

        LOG.info(String.format("Successfully loaded properties file: %s", properties.toString()));

        this.consumerConfigurationPath = properties.getProperty(CONSUMER_CONFIGURATION_PATH_KEY);
        this.mongoConfigurationPath = properties.getProperty(MONGO_CONFIGURATION_PATH_KEY);
        this.urlConfigPath = properties.getProperty(URL_CONFIG_PATH);
    }

    public String getConsumerConfigurationPath() {
        return consumerConfigurationPath;
    }

    public String getMongoConfigurationPath() {
        return mongoConfigurationPath;
    }

    public String getUrlConfigPath() {
        return urlConfigPath;
    }
}
