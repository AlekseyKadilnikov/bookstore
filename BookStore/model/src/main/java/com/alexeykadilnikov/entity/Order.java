package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.OrderStatus;

import java.util.*;

public class Order extends BaseEntity {
    private static long idCount = 0;
    private List<Book> books;
    private User user;
    private int price = 0;
    private OrderStatus status;
    private Date executionDate;
    private final Date initDate = new Date();

    public Order(List<Book> books, User user) {
        super(idCount++);
        this.books = books;
        this.user = user;
        status = OrderStatus.NEW;
        calculatePrice();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id = " + getId() +
                ", books=" + books.toString() +
                ", username=" + user.getUsername() +
                ", status=" + status +
                '}';
    }

    public Date getInitDate() {
        return initDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBook(Book book) {
        books.add(book);
        price += book.getPrice();
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

    public int getPrice() {
        return price;
    }

    public Date getExecutionDate() {
        if(executionDate == null)
            return null;
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    private void calculatePrice() {
        for(Book book : books) {
            price += book.getPrice();
        }
    }
}
