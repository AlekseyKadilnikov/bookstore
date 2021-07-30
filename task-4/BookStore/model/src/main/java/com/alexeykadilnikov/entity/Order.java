package com.alexeykadilnikov.entity;

import java.util.Arrays;
import java.util.Date;

public class Order extends BaseEntity {
    private static long ID_COUNT = 0;
    private Book[] books;
    private User user;
    private int price = 0;
    private OrderStatus status;
    private Date executionDate;

    public Order(Book[] books, User user) {
        super(ID_COUNT++);
        this.books = books;
        this.user = user;
        status = OrderStatus.New;
        calculatePrice();
    }

    @Override
    public String toString() {
        return "Order{" +
                "books=" + Arrays.toString(books) +
                ", user=" + user +
                ", status=" + status +
                '}';
    }

    public Book[] getBooks() {
        return books;
    }

    public void setBook(Book book) {
        Book[] newBooks = new Book[books.length + 1];
        newBooks[books.length] = book;
        books = newBooks;

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
