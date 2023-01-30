package com.kvitka.application.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders contentTypeJsonHeader = new HttpHeaders();

    {
        contentTypeJsonHeader.setContentType(MediaType.APPLICATION_JSON);
    }

    public <I, O> ResponseEntity<O> postForEntity(String url, I requestBody, Class<O> responseType) {
        return restTemplate.postForEntity(
                url,
                new HttpEntity<>(requestBody, contentTypeJsonHeader),
                responseType);
    }

    public <I> void put(String url, I requestBody) {
        restTemplate.put(url, new HttpEntity<>(requestBody, contentTypeJsonHeader));
    }
}
