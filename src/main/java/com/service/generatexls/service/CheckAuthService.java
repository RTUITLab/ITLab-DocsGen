package com.service.generatexls.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CheckAuthService {

    final RestTemplate restTemplate;

    @Value("${identityUrl}/connect/userinfo")
    String userInfoUrl;

    public void checkToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        restTemplate.exchange(
                userInfoUrl,
                HttpMethod.POST,
                new HttpEntity<String>(null, headers),
                Object.class
        );
    }
}
