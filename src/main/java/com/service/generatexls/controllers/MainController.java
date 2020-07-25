package com.service.generatexls.controllers;

import com.service.generatexls.dto.Event;
import com.service.generatexls.service.CheckAuthService;
import com.service.generatexls.service.GenerateXlsService;
import com.service.generatexls.service.RestTemplateGetJson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    final RestExceptionHandler restExceptionHandler;
    final GenerateXlsService generateXlsService;
    final RestTemplateGetJson restTemplateGetJson;
    final CheckAuthService checkAuthService;


    @GetMapping("/docsgen/downloadxls")
    public ResponseEntity<ByteArrayResource> downloadTemplate(@RequestHeader("Authorization") String authorization, @RequestParam @NonNull String end, @RequestParam @NonNull String begin) throws Exception {

        try {
           checkAuthService.checkToken(authorization);
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        List<Event> events;

        try {
            events = restTemplateGetJson.getJson(end, begin);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("A TRACE Message", e);
            return restExceptionHandler.handleEntityServerIsNotAvailableEx();
        }
        if (events.isEmpty()) {
            return restExceptionHandler.handleEntityNoContentEx();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        val workbook = generateXlsService.getXls(events);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("xls", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=svodka.xlsx");
        workbook.write(stream);
        workbook.close();
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                header, HttpStatus.OK);


    }
}



