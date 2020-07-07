package com.service.generatexls.controllers;


import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<ByteArrayResource> handleEntityNoContentEx() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), HttpStatus.NO_CONTENT);
    }

    protected ResponseEntity<ByteArrayResource> handleEntityServerIsNotAvailableEx() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


