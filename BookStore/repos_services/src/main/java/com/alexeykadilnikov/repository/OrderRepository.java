package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class OrderRepository implements IOrderRepository {
    private List<Order> orders = new ArrayList<>();

    @Override
    public List<Order> findAll() {
        return orders;
    }

    @Override
    public Order getById(Long id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public void delete(Order order) {
        orders.remove(order);
    }

    @Override
    public void saveAll(List<Order> all) {
        orders = all;
    }
}
