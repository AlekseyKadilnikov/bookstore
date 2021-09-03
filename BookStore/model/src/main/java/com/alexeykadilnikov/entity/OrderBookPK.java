package com.alexeykadilnikov.entity;


import java.io.Serializable;
import java.util.Objects;

public class OrderBookPK implements Serializable {
    private Long orderId;
    private Long bookId;

    public OrderBookPK() {
    }

    public OrderBookPK(Long orderId, Long bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookPK that = (OrderBookPK) o;
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
