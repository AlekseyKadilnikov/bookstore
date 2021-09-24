package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class DefaultAdvice {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAdvice.class);
    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleException1(Exception e) {

        HttpStatus internalError = HttpStatus.BAD_REQUEST;

        Response response = new Response(
                e.getMessage(),
                internalError.name(),
                ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString()
        );

        logger.error("error", e);

        if(e instanceof MappingException) {
            response.setMessage("Wrong input values");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
