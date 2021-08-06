package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;

public class Request extends BaseEntity {
    private static long idCount = 0;
    private final String name;
    private int count = 1;
    private RequestStatus status;

    public Request(String name, RequestStatus status) {
        super(idCount++);
        this.name = name;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", count=" + count +
                "}\n";
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
