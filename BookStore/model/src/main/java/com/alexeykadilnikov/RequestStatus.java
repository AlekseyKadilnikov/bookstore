package com.alexeykadilnikov;

public enum RequestStatus {
    COMMON(0),
    NEW(1),
    SUCCESS(2);

    private final int statusCode;

    RequestStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}