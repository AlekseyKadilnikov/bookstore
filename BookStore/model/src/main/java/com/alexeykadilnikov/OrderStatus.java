package com.alexeykadilnikov;

public enum  OrderStatus {
    NEW(0),
    SUCCESS(1),
    CANCELED(2)
    ;

    private final int statusCode;

    OrderStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
