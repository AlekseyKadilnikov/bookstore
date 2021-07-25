package service;

import model.Book;

public interface ICustomerService {
    void createOrder(Book[] books);
    void cancelOrder(int id);
    void createRequest(Book book);
    void cancelRequest(Book book);
}
