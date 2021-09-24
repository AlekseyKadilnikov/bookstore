package com.alexeykadilnikov;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    USER(Set.of(Permission.BOOKS_READ,
            Permission.ORDERS_WRITE,
            Permission.ORDERS_UPDATE,
            Permission.REQUESTS_WRITE)),

    ADMIN(Set.of(Permission.ORDERS_READ,
            Permission.ORDERS_WRITE,
            Permission.ORDERS_DELETE,
            Permission.ORDERS_UPDATE,
            Permission.BOOKS_READ,
            Permission.BOOKS_WRITE,
            Permission.BOOKS_DELETE,
            Permission.BOOKS_UPDATE,
            Permission.USERS_READ,
            Permission.USERS_WRITE,
            Permission.USERS_DELETE,
            Permission.USERS_UPDATE,
            Permission.REQUESTS_READ,
            Permission.REQUESTS_WRITE,
            Permission.REQUESTS_DELETE,
            Permission.REQUESTS_UPDATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
