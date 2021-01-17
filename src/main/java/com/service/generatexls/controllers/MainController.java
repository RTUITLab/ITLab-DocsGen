package com.service.generatexls.controllers;

import com.service.generatexls.dto.Event;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MainController {
    final RestExceptionHandler restExceptionHandler;
    final GenerateXlsService generateXlsService;
    final RestTemplateGetJson restTemplateGetJson;
    private List<Event> ResourceAccessException;

    @GetMapping("/api/docsgen/downloadxls")
    public ResponseEntity<ByteArrayResource> downloadTemplate(@RequestParam @NonNull String end, @RequestParam @NonNull String begin, @RequestParam(required = false) String[] eventTypeId) throws Exception {

        List<Event> events;
        //ArrayList<Event> sortedEvents = new ArrayList<>();

        try {
            events = restTemplateGetJson.getJson(end, begin);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("A TRACE Message", e);
            return restExceptionHandler.handleEntityServerIsNotAvailableEx();
        }
        if (eventTypeId != null) {

            events = events.stream().filter(event -> Arrays.asList(eventTypeId).contains(event.getEventType().getId())).collect(Collectors.toList());

        }
        if (events.isEmpty()) {
            return restExceptionHandler.handleEntityNoContentEx();
        }
        log.info("Request received");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        val workbook = generateXlsService.getXls(events);
        log.info("XLS document created");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("xls", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=svodka.xlsx");
        workbook.write(stream);
        workbook.close();
        log.info("Document sent");
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                header, HttpStatus.OK);


    }

}
