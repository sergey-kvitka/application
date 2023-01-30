package com.kvitka.application.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders contentTypeJsonHeader = new HttpHeaders();

    {
        contentTypeJsonHeader.setContentType(MediaType.APPLICATION_JSON);
    }

    public <I, O> ResponseEntity<O> postForEntity(String url, I requestBody, Class<O> responseType) {
        HttpEntity<I> httpEntity = new HttpEntity<>(requestBody, contentTypeJsonHeader);
        log.info("POST request is about to be sent with values: (URL = {}, HTTP entity = {})", url, httpEntity);
        ResponseEntity<O> responseEntity = restTemplate.postForEntity(url, httpEntity, responseType);
        log.info("POST request sent and response entity received (response entity = {})", responseEntity);
        return responseEntity;
    }

    public <I> void put(String url, I requestBody) {
        HttpEntity<I> httpEntity = new HttpEntity<>(requestBody, contentTypeJsonHeader);
        log.info("PUT request is about to be sent with values: (URL = {}, HTTP entity = {})", url, httpEntity);
        restTemplate.put(url, httpEntity);
        log.info("PUT request sent successfully!");
    }
}
