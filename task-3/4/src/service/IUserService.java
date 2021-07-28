package service;

import model.User;

public interface IUserService {
    void addUser(String username);
    User getUser();
}
