package com.service.generatexls.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${IssuerUrl}")
    String issuerUri;
    @Value("${UseJwtSecure}")
    Boolean useJwtSecure;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!useJwtSecure) {
            return;
        }
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api").hasAuthority("SCOPE_itlab:user")
                                .anyRequest()
                                .authenticated()

                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt ->
                                        jwt.decoder(JwtDecoders.fromOidcIssuerLocation(issuerUri))
                                )
                );
    }


}