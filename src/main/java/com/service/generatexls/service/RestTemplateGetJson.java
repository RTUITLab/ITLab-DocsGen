package com.service.generatexls.service;

import com.service.generatexls.dto.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestTemplateGetJson {

    private final RestTemplate restTemplate;
    @Value("${Url}")
    private String url;
    @Value("${Key}")
    private String key;


    public List<Event> getJson(String end, String begin) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", key);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<List<Event>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Event>>() {
                },
                end,
                begin


        );
        return response.getBody();
    }
}


