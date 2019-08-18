package com.rhapps.show_of_hands.showofhands.config;


import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.database}")
    private String database;
    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(),mappingMongoConverter());
    }
}
