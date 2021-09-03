package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "order_t")
public class Order extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5713427368399553474L;
    @Column(name = "total_price")
    private int totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    @Column(name = "exec_date")
    private LocalDateTime executionDate;
    @Column(name = "init_date")
    private LocalDateTime initDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderBook> orderBooks;

    public Order() {
        initDate = LocalDateTime.now();
        executionDate = null;
    }

    public Order(Set<OrderBook> orderBooks, User user) {
        this.orderBooks = orderBooks;
        this.user = user;
        status = OrderStatus.NEW;
        initDate = LocalDateTime.now();
        executionDate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return totalPrice == order.totalPrice && status == order.status &&
                executionDate.equals(order.executionDate) && initDate.equals(order.initDate)
                && user.equals(order.user) && Objects.equals(orderBooks, order.orderBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, status, executionDate, initDate, user, orderBooks);
    }

    public LocalDateTime getInitDate() {
        return initDate;
    }

    public Set<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public void setOrderBooks(Set<OrderBook> orderBooks) {
        this.orderBooks = orderBooks;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getExecutionDate() {
        if(executionDate == null)
            return null;
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public void setInitDate(LocalDateTime initDate) {
        this.initDate = initDate;
    }
}
