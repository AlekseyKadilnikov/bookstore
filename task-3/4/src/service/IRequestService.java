package service;

import model.Book;
import model.User;

public interface IRequestService {
    void createRequest(Book book, User user);
    String showRequest();
    void cancelRequest();
}
