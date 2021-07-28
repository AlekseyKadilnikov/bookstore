package service;

import model.Book;
import model.OrderStatus;
import model.User;

public interface IOrderService {
    void createOrder(Book book, User user);
    String showOrder();
    void cancelOrder();
    void setStatus(OrderStatus status);
    void completeOrder();
}
