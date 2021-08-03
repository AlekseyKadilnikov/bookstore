package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.OrderStatus;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Order extends BaseEntity {
    private static long idCount = 0;
    private Book[] books;
    private User user;
    private int price = 0;
    private OrderStatus status;
    private Date executionDate;
    private final Date initDate = new Date();

    public Order(Book[] books, User user) {
        super(idCount++);
        this.books = books;
        this.user = user;
        status = OrderStatus.NEW;
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

    public Date getInitDate() {
        return initDate;
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
