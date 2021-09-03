package com.alexeykadilnikov.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1624659229198950104L;
    @Column(name = "name")
    private String username;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
