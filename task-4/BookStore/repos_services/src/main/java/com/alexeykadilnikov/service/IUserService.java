package com.alexeykadilnikov.service;


import com.alexeykadilnikov.entity.User;

public interface IUserService {
    void addUser(String username);
    User getUser();
}
