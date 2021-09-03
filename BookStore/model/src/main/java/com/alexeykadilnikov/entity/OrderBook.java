package com.alexeykadilnikov.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_book")
@IdClass(OrderBookPK.class)
public class OrderBook implements Serializable {
    @Id
    @Column(name = "order_id")
    private Long orderId;
    @Id
    @Column(name = "book_id")
    private Long bookId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "book_count")
    private int bookCount;

    public OrderBook() {
    }

    public OrderBook(Order order, Book book, int bookCount) {
        this.order = order;
        this.book = book;
        this.bookCount = bookCount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int count) {
        this.bookCount = count;
    }
}
