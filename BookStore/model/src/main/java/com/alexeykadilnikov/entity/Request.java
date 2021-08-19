package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Request extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4499720100063137694L;
    private static long idCount = 0;
    private final String name;
    private int count = 1;
    private RequestStatus status;
    private final Set<Long> ordersId = new HashSet<>();
    private Set<Long> booksId = new HashSet<>();

    public Request(String name, Set<Long> booksId) {
        super(idCount++);
        this.name = name;
        this.booksId.addAll(booksId);
        this.status = RequestStatus.COMMON;
    }

    public Request(String name, long bookId, Set<Long> ordersId, RequestStatus status) {
        super(idCount++);
        this.name = name;
        this.booksId.add(bookId);
        this.ordersId.addAll(ordersId);
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", count=" + count +
                "}\n";
    }

    public Set<Long> getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Set<Long> ordersId) {
        this.ordersId.addAll(ordersId);
    }

    public Set<Long> getBooksId() {
        return booksId;
    }

    public void setBooksId(Set<Long> booksId) {
        this.booksId = booksId;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
