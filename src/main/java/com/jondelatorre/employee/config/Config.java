package com.jondelatorre.employee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jondelatorre.employee.repository.RepositoryPackageMarker;

@Configuration
@EnableWebMvc
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = {RepositoryPackageMarker.class})
public class Config {

}
