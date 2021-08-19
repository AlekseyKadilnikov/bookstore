package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Request;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class RequestRepository implements IRequestRepository {
    private List<Request> requests = new ArrayList<>();

    @Override
    public List<Request> findAll() {
        return requests;
    }

    @Override
    public Request getById(Long id) {
        return requests.stream()
                .filter(request -> request.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(Request request) {
        requests.add(request);
    }

    @Override
    public void delete(Request request) {
        requests.remove(request);
    }

    @Override
    public void saveAll(List<Request> all) {
        requests = all;
    }
}
