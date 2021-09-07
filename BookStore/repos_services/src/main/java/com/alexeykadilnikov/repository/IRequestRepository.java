package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Request;

public interface IRequestRepository extends IRepository<Request, Long> {
    void update(Request request);
}
