package com.alexeykadilnikov.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderBookKey implements Serializable {
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "book_id")
    private Long bookId;

    public OrderBookKey() {
    }

    public OrderBookKey(Long orderId, Long bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookKey that = (OrderBookKey) o;
        return orderId.equals(that.orderId) && bookId.equals(that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookId);
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
}
