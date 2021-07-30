package com.alexeykadilnikov.repository;


import com.alexeykadilnikov.entity.Order;


public class OrderRepository implements IRepository<Order, Long> {
    private Order[] orders;

    public OrderRepository() {
    }

    @Override
    public Order[] findAll() {
        return orders;
    }

    @Override
    public Order getById(Long id) {
        return null;
    }

    @Override
    public void save(Order order) {
        Order[] newOrder = new Order[orders.length + 1];
        newOrder[orders.length] = order;
        orders = newOrder;
    }

    @Override
    public void delete(Order order) {

    }

    public Order getByIndex(int index) {
        return orders[index];
    }
}
