package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IOrderRepository {
//    private static OrderRepository instance;

    private List<Order> orders = new ArrayList<>();

//    private OrderRepository() {
//    }

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

//    public static OrderRepository getInstance() {
//        if(instance == null) {
//            instance = new OrderRepository();
//        }
//        return instance;
//    }
}
