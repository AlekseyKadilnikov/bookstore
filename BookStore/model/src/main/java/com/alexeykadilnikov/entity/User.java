package com.alexeykadilnikov.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1624659229198950104L;
    private static long idCount = 0;
    private String username;
    private Set<Long> ordersId = new HashSet<>();

    public User() {
        super(idCount++);
    }

    public User(String username) {
        super(idCount++);
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id = " + getId() +
                ", username='" + username + '\'' +
                ", orders=" + ordersId.toString() +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Long> getOrders() {
        return ordersId;
    }

    public void setOrders(Set<Long> ordersId) {
        this.ordersId = ordersId;
    }

    public void addOrder(Long orderId) {
        ordersId.add(orderId);
    }
}
