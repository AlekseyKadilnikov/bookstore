package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.OrderBook;
import com.alexeykadilnikov.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface IOrderService {
    void createOrder(List<Long> booksId, User user);
    String showOrder(long id);
    void cancelOrder(long id);
    void setStatus(long index, OrderStatus status);
    void completeOrder(long id);
    List<Order> getAll();

    void saveOrder(Order order);

    void saveAll(List<Order> orderList);

    void checkBookAvailable(Set<OrderBook> orderBooks);

    Order getByIndex(long id);

    Order getById(long id);

    int calculatePrice(Order order);

    List<Order> sort(List<Order> orders, Comparator<Order> comparator);

    List<Order> sendSqlQuery(String hql);
}
