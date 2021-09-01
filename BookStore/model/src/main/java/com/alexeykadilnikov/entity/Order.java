package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.OrderStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Order extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5713427368399553474L;
    private static long idCount = 0;
    private List<Book> books;
    private Long userId;
    private int totalPrice = 0;
    private OrderStatus status;
    private LocalDate executionDate;
    private LocalDate initDate;

    public Order() {
        super(idCount);
        initDate = LocalDate.now();
        executionDate = null;
    }

    public Order(List<Book> books, Long userId) {
        super(idCount++);
        this.books = books;
        this.userId = userId;
        status = OrderStatus.NEW;
        initDate = LocalDate.now();
        executionDate = null;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id = " + getId() +
                ", books=\n" + books.toString() +
                ", userId=" + userId +
                ", status=" + status +
                ", executionDate=" + executionDate +
                ", total price=" + totalPrice +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return totalPrice == order.totalPrice && books.equals(order.books)
                && userId.equals(order.userId) && status == order.status
                && executionDate.equals(order.executionDate) && initDate.equals(order.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(books, userId, totalPrice, status, executionDate, initDate);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBook(Book book) {
        books.add(book);
        totalPrice += book.getPrice();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getExecutionDate() {
        if(executionDate == null)
            return null;
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }
}
