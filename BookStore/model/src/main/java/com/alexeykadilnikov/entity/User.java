package com.alexeykadilnikov.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User extends BaseEntity {
    private static final long serialVersionUID = -1624659229198950104L;
    private static long idCount = 0;
    private String username;
    private Set<Order> orders = new HashSet<>();

    public User(String username) {
        super(idCount++);
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id = " + getId() +
                ", username='" + username + '\'' +
                ", orders=" + orders.toString() +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
