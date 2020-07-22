package com.service.generatexls.controllers;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.service.generatexls.dto.Event;
import com.service.generatexls.service.GenerateXlsService;
import com.service.generatexls.service.RestTemplateGetJson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MainController {
    final RestExceptionHandler restExceptionHandler;
    final GenerateXlsService generateXlsService;
    final RestTemplateGetJson restTemplateGetJson;
    private List<Event> ResourceAccessException;
    @Value("${jwkUrl}")
    private String jwkUrl;

    private RsaVerifier verifier(String kid) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(jwkUrl));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    @GetMapping("/api/docsgen/downloadxls")
    public ResponseEntity<ByteArrayResource> downloadTemplate(Map claims, @RequestParam @NonNull String end, @RequestParam @NonNull String begin) throws Exception {
        int exp = (int) claims.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();
        if (expireDate.before(now) || !claims.get("iss").equals(jwkUrl) ||
                !claims.get("aud").equals("itlab_mobile_app")) {
            throw new RuntimeException("Invalid claims");
        } else {
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
}
