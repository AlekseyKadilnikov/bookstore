package model;

import status.OrderStatus;

public class Order {
    private int id;
    private Book[] books;
    private User user;
    private OrderStatus status;

    public Order(Book[] books, User user) {
        this.books = books;
        this.user = user;
        status = OrderStatus.New;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book[] getBooks() {
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
