package com.kvitka.application.controllers;

import com.kvitka.application.exceptions.PreScoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class) // * 400
    public ResponseEntity<String> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        String exceptionSimpleName = "HttpClientErrorException";
        String exceptionMessage = e.getMessage();

        String matchedMessage = null;
        Matcher matcher = Pattern.compile("\\[(.*?)]").matcher(exceptionMessage == null ? "" : exceptionMessage);
        if (matcher.find()) matchedMessage = matcher.group(1);
        else matcher = Pattern.compile("\"(.*?)\"").matcher(exceptionMessage == null ? "" : exceptionMessage);
        if (matcher.find()) matchedMessage = matcher.group(1);

        log.warn("{} handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(matchedMessage == null
                ? exceptionSimpleName + ": " + exceptionMessage
                : matchedMessage
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceAccessException.class) // * connection refused
    public ResponseEntity<String> resourceAccessExceptionHandler(ResourceAccessException e) {
        String exceptionSimpleName = "ResourceAccessException";
        String exceptionMessage = e.getMessage();
        Matcher matcher = Pattern.compile("\"(.*?)\"").matcher(exceptionMessage == null ? "" : exceptionMessage);
        if (matcher.find()) {
            exceptionMessage = String.format("connection refused (%s)", matcher.group(1));
        }
        log.warn("{} handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(exceptionSimpleName + ": " + exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PreScoreException.class)
    public ResponseEntity<String> preScoreExceptionHandler(PreScoreException e) {
        log.warn("PreScoreException handled (message: {})", e.getMessage());
        return ResponseEntity.badRequest().body(
                e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> otherExceptionHandler(Exception e) {
        String exceptionSimpleName = e.getClass().getSimpleName();
        String exceptionMessage = e.getMessage();
        log.warn("Other exception ({}) handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(exceptionSimpleName + ": " + exceptionMessage);
    }
}
