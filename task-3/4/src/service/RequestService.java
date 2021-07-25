package service;

import repository.RequestRepository;

public class RequestService implements IRequestService {
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }
}
