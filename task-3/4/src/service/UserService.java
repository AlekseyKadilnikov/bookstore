package service;

import model.Book;
import model.User;

public class UserService implements ICustomerService {
    private User user;

    public UserService(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void createOrder(Book[] books) {

    }

    @Override
    public void cancelOrder(int id) {

    }

    @Override
    public void createRequest(Book book) {

    }

    @Override
    public void cancelRequest(Book book) {

    }
}
