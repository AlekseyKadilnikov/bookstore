package com.alexeykadilnikov.entity;

public enum  OrderStatus {
    New(0),
    Completed(1),
    Canceled(2)
    ;

    private final int statusCode;

    OrderStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
