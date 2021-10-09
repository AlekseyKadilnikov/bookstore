package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.Response;
import com.alexeykadilnikov.security.JwtAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> nullPointerException(NullPointerException e) {

        HttpStatus internalError = HttpStatus.BAD_REQUEST;

        Response response = new Response(
                e.getMessage(),
                internalError.name(),
                ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString()
        );

        logger.error("error", e);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> jwtAuthenticationException(JwtAuthenticationException e) {

        HttpStatus internalError = HttpStatus.BAD_REQUEST;
        Response response = new Response(
                e.getMessage(),
                internalError.name(),
                ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString()
        );

        logger.error("error", e);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handlerException(Exception e) {

        HttpStatus internalError = HttpStatus.BAD_REQUEST;

        Response response = new Response(
                e.getMessage(),
                internalError.name(),
                ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString()
        );

        logger.error("error", e);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
