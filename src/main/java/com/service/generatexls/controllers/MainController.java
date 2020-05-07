package com.service.generatexls.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.generatexls.dto.Event;
import com.service.generatexls.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    RestTemplateService restTeammateService;
    @GetMapping("getJson")
    public List<Event> getJosn() throws JsonProcessingException {

        return restTeammateService.getJson();
    }


}
