package org.nmdp.hmlfhirconverterapi.config;

/**
 * Created by Andrew S. Brown, Ph.D., <abrown3@nmdp.org>, on 12/27/16.
 * <p>
 * service-hmlFhirConverter-api
 * Copyright (c) 2012-2016 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.nmdp.hmlfhirconvertermodels.domain.internal.MongoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.nmdp.hmlfhirconverterapi.dao")
public class MongoConfig extends AbstractMongoConfiguration {
    private static final Logger LOG = Logger.getLogger(MongoConfig.class);

    @Value("${spring.profiles.active}")
    private String profileActive;

    @Value("${spring.application.name}")
    private String proAppName;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String mongoDb;

    @Override
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        return super.mongoMappingContext();
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        try {
            return new MongoClient(mongoHost + ":" + mongoPort);
        } catch (Exception ex) {
            LOG.error("Error instantiating MongoDB.", ex);
            throw ex;
        }
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        try {
            return new MongoTemplate(new MongoClient(mongoHost + ":" + mongoPort), mongoDb);
        } catch (Exception ex) {
            LOG.error("Error instantiating MongoDB.", ex);
            throw ex;
        }
    }

    @Bean
    public MongoConfiguration mongoConfiguration() {
        return new MongoConfiguration(mongoHost, Integer.parseInt(mongoPort), mongoDb);
    }

    @Override
    protected String getDatabaseName() {
        return mongoDb;
    }
}
