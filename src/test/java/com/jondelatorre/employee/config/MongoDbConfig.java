package com.jondelatorre.employee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;

import io.github.hzpz.spring.boot.autoconfigure.mongeez.MongeezAutoConfiguration;

@Configuration
@EnableAutoConfiguration(exclude={MongeezAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class})
public class MongoDbConfig  extends AbstractMongoConfiguration{
    
    @Autowired
    private Environment env;

    @Override
    public MongoClient mongoClient() {
        return new Fongo(getDatabaseName()).getMongo();
    }

    @Override
    protected String getDatabaseName() {
        return env.getRequiredProperty("spring.data.mongodb.database");
    }
    
    
}
