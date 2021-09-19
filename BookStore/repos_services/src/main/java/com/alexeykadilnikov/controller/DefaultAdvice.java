package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.Response;
import org.modelmapper.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleException1(Exception e) {

        HttpStatus internalError = HttpStatus.BAD_REQUEST;

        Response response = new Response(
                e.getMessage(),
                internalError.name(),
                ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString()
        );

        if(e instanceof MappingException) {
            response.setMessage("Wrong input values");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}