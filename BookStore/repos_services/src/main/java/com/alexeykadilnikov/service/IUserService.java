package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.User;

import java.util.List;

public interface IUserService {
    int addUser(String username);

    void addUser(User user);

    List<User> getAll();

    void saveAll(List<User> userList);

    User getById(long id);

    User getByName(String name);
}
