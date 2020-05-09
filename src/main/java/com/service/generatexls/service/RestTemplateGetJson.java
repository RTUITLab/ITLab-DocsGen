package com.service.generatexls.service;

import com.service.generatexls.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UrlAndHeaders urlAndHeaders;
    public List<Event> getJson(String end, String begin){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(urlAndHeaders.Body, urlAndHeaders.Key);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<List<Event>> response = restTemplate.exchange(
                urlAndHeaders.Url,
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
