package com.alexeykadilnikov;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private String message;
    private String httpStatus;
    private String dateTime;

    public Response(String message, String httpStatus, String dateTime) {
        this.message = message;
        this.dateTime = dateTime;
        this.httpStatus = httpStatus;
    }
}
