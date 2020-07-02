package com.service.generatexls.controllers;

import com.service.generatexls.service.RestTemplateGenerateXls;
import com.service.generatexls.service.RestTemplateGetJson;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @Autowired
    RestTemplateGetJson restTemplateGetJson;
    @Autowired
    RestTemplateGenerateXls restTeammateService;
    @Autowired
    RestExceptionHandler restExceptionHandler;

    @GetMapping("/api/docsgen/downloadxls")

    public ResponseEntity<ByteArrayResource> downloadTemplate(@RequestParam String end, @RequestParam String begin) {
        try{
                if(restTemplateGetJson.getJson(end, begin).isEmpty()){
                    return restExceptionHandler.handleEntityNoContentEx();
                }
                    logger.info("Request received");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    val workbook = restTeammateService.getXls(end, begin);
                    logger.info("XLS document created");
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(new MediaType("application", "force-download"));
                    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=svodka.xlsx");
                    workbook.write(stream);
                    workbook.close();
                    logger.info("Document sent");
                    return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                            header, HttpStatus.CREATED);
        }catch (Exception e){
            logger.log(Level.SEVERE, "Error", e);
            return null;
        }
    }


}