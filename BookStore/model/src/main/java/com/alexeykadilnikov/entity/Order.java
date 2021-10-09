package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "order_t")
public class Order extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5713427368399553474L;
    @Column(name = "total_price")
    private int totalPrice;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_code")
    private OrderStatus status;
    @Column(name = "exec_date")
    private LocalDateTime executionDate;
    @Column(name = "init_date")
    private LocalDateTime initDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public Order(int totalPrice, OrderStatus status, LocalDateTime initDate, User user) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.initDate = initDate;
        this.user = user;
    }



    @Override
    public String toString() {
        return "Order{" +
                "totalPrice=" + totalPrice +
                ", status=" + status +
                ", executionDate=" + executionDate +
                ", initDate=" + initDate +
                ", user=" + user +
                ", orderBooks=" + orderBooks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return totalPrice == order.totalPrice && status == order.status &&
                initDate.equals(order.initDate) && user.equals(order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, status, executionDate, initDate, user);
    }

    @PreRemove
    public void removeThisOrderFromUser() {
        user.getOrders().remove(this);
    }
}
