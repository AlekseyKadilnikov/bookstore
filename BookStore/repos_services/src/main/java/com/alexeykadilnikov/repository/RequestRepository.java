package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository implements IRequestRepository {
//    private static RequestRepository instance;

    private List<Request> requests = new ArrayList<>();

//    public static RequestRepository getInstance() {
//        if(instance == null) {
//            instance = new RequestRepository();
//        }
//        return instance;
//    }

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
