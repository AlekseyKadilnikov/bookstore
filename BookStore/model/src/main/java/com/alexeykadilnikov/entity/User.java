package com.alexeykadilnikov.entity;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {
    private static long idCount = 0;
    private String username;
    private List<Order> orders = new ArrayList<>();

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

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
