package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.User;

import java.util.Comparator;
import java.util.List;

public interface IOrderService {
    void createOrder(List<Book> books, User user);
    String showOrder(long id);
    void cancelOrder(long id);
    void setStatus(long index, OrderStatus status);
    void completeOrder(long id);
    List<Order> getAll();

    void saveOrder(Order order);

    void saveAll(List<Order> orderList);

    void checkBookAvailable(List<Book> books, long orderId);

    Order getByIndex(long id);

    Order getById(long id);

    int calculatePrice(Order order);

    List<Order> sort(List<Order> orders, Comparator<Order> comparator);
}
