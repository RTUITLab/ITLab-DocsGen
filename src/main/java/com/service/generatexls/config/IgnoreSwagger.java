package com.service.generatexls.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Order(1)
@Configuration
public class IgnoreSwagger extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("api/docsgen/api-docs.yaml")
                .antMatchers("api/docsgen/swagger-ui.html");

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }

}
