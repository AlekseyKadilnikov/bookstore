package service;

import model.Book;
import model.Request;
import model.User;
import repository.RequestRepository;

public class RequestService implements IRequestService {
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void createRequest(Book book, User user) {
        if(book.isAvailable()) {
            System.out.println("Request couldn't be created: book is available");
            return;
        }
        requestRepository.save(new Request(book, user));
        System.out.println("Request id = " + requestRepository.getRequest().getId() + " created");
    }

    @Override
    public String showRequest() {
        return requestRepository.getRequest().toString();
    }

    @Override
    public void cancelRequest() {
        System.out.println("Request id = " + requestRepository.getRequest().getId() + " closed");
    }
}
