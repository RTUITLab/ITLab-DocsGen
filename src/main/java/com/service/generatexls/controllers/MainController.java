package com.service.generatexls.controllers;

import com.service.generatexls.dto.Event;
import com.service.generatexls.service.RestTemplateService;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    RestTemplateService restTeammateService;



    @GetMapping("downloadXls")
    public ResponseEntity<ByteArrayResource> downloadTemplate(@RequestParam String end, @RequestParam String begin) throws Exception {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            val a =restTeammateService.getXls(end,begin);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductTemplate.xlsx");
            a.write(stream);
            a.close();
            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                    header, HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}