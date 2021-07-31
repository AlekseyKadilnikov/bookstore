package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;

import java.util.Date;

public class Request extends BaseEntity {
    private static long ID_COUNT = 0;
    private Book book;
    private User user;
    private int amount = 1;
    private final Date date = new Date();
    private RequestStatus status = RequestStatus.OPENED;

    public Request(Book book, User user) {
        super(ID_COUNT++);
        this.book = book;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Request{" +
                "book=" + book +
                ", user=" + user +
                ", date=" + date +
                ", status=" + status +
                '}';
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount() {
        amount++;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
