package com.alexeykadilnikov.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_book")
public class OrderBook implements Serializable {
    @EmbeddedId
    private OrderBookKey id = new OrderBookKey();
    @ManyToOne(optional=false)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", insertable=false, updatable=false)
    private Order order;
    @ManyToOne(optional=false)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable=false, updatable=false)
    private Book book;
    @Column(name = "book_count")
    private int bookCount;

    public void setId(OrderBookKey id) {
        this.id = id;
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
