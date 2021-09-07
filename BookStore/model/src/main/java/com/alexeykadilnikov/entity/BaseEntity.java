package com.alexeykadilnikov.entity;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 4402678274096518315L;
    private long id;

    protected BaseEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
