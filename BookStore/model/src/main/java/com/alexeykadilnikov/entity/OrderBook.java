package com.alexeykadilnikov.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "OrderBook{" +
                "orderId=" + order.getId() +
                ", bookId=" + book.getId() +
                ", bookCount=" + bookCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        return bookCount == orderBook.bookCount && Objects.equals(id, orderBook.id) && Objects.equals(order, orderBook.order) && Objects.equals(book, orderBook.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, book, bookCount);
    }

    @PreRemove
    public void removeThisOrderBookFromOrderAndBook() {
        order.getOrderBooks().remove(this);
        book.getOrderBooks().remove(this);
    }
}
