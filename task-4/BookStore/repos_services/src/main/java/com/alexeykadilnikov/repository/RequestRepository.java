package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Request;

public class RequestRepository implements IRepository<Request, Long>{
    private Request[] requests;

    public RequestRepository() {
    }

    @Override
    public Request[] findAll() {
        return requests;
    }

    @Override
    public Request getById(Long id) {
        return null;
    }

    @Override
    public void save(Request request) {
        Request[] newRequests = new Request[requests.length + 1];
        newRequests[requests.length] = request;
        requests = newRequests;
    }

    @Override
    public void delete(Request req) {

    }

    public Request getByIndex(int index) {
        return requests[index];
    }
}
