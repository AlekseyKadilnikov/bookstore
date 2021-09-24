package com.alexeykadilnikov;

public enum Permission {
    ORDERS_READ("orders:read"),
    ORDERS_WRITE("orders:write"),
    ORDERS_DELETE("orders:delete"),
    ORDERS_UPDATE("orders:update"),
    BOOKS_READ("books:read"),
    BOOKS_WRITE("books:write"),
    BOOKS_DELETE("books:delete"),
    BOOKS_UPDATE("books:update"),
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    USERS_DELETE("users:delete"),
    USERS_UPDATE("users:update"),
    REQUESTS_READ("requests:read"),
    REQUESTS_WRITE("requests:write"),
    REQUESTS_DELETE("requests:delete"),
    REQUESTS_UPDATE("requests:update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}