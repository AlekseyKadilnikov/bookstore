package com.alexeykadilnikov.entity;

public enum  OrderStatus {
    NEW(0),
    COMPLETED(1),
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
