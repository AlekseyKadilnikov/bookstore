package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Request extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4499720100063137694L;
    private String name;
    private int count;
    private RequestStatus status;
    private final Set<Long> ordersId = new HashSet<>();
    private Set<Long> booksId = new HashSet<>();

    public Request() {
    }

    public Request(String name, Set<Long> booksId) {
        this.name = name;
        this.booksId.addAll(booksId);
        this.status = RequestStatus.COMMON;
    }

    public Request(String name, Set<Long> booksId, Set<Long> ordersId, RequestStatus status) {
        this.name = name;
        this.booksId.addAll(booksId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return count == request.count && name.equals(request.name) && status == request.status
                && ordersId.equals(request.ordersId) && booksId.equals(request.booksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count, status, ordersId, booksId);
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

    public void setName(String name) {
        this.name = name;
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
