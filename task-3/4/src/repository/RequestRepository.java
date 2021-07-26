package repository;

import model.Request;

import java.util.List;

public class RequestRepository implements IRepository<Request, Long>{
    private Request request;

    public RequestRepository() {
    }

    @Override
    public List<Request> findAll() {
        return null;
    }

    @Override
    public Request getById(Long id) {
        return null;
    }

    @Override
    public void save(Request request) {
        this.request = request;
    }

    @Override
    public void delete(Request req) {

    }

    public Request getRequest() {
        return request;
    }
}
