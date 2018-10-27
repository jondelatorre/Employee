package com.jondelatorre.employee.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jondelatorre.employee.controller.EmployeeController;
import com.jondelatorre.employee.repository.RepositoryPackageMarker;

import static com.jondelatorre.employee.controller.EmployeeController.EMPLOYEE_REQUEST_MAPPING;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = {RepositoryPackageMarker.class})
public class Config extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private static final String MATCH_ALL = "/**";

@Bean
    @Override
    public UserDetailsService userDetailsService() {
    UserDetails user =
            User.withDefaultPasswordEncoder()
               .username("user")
               .password("password")
               .roles("ADMIN")
               .build();

       return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug(
                "Setting up http configuration only for a single method: DELETE /employee/{employeeId}");

        http
        .httpBasic().and()
        .csrf().disable()
        .authorizeRequests()
                .antMatchers(HttpMethod.POST,EMPLOYEE_REQUEST_MAPPING).permitAll()
                .antMatchers(HttpMethod.GET,EMPLOYEE_REQUEST_MAPPING+MATCH_ALL).permitAll()
                .antMatchers(HttpMethod.PUT,EMPLOYEE_REQUEST_MAPPING+MATCH_ALL).permitAll()
                .antMatchers(HttpMethod.DELETE,EMPLOYEE_REQUEST_MAPPING+MATCH_ALL).hasRole("ADMIN");
    }

}
