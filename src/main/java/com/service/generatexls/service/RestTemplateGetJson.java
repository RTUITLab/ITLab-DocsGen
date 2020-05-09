package com.service.generatexls.service;

import com.service.generatexls.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class RestTemplateGetJson {
    @Autowired
    private RestTemplate restTemplate;
    @Value("stringUrl")
    private String stringUrl;
    @Value("stringKey")
    private String stringKey;
    @Value("stringBody")
    private String stringBody;

    public List<Event> getJson(String end, String begin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(stringBody, stringKey);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<List<Event>> response = restTemplate.exchange(
                stringUrl,
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
