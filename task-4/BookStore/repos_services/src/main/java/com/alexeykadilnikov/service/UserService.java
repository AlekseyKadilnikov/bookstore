package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.UserRepository;

public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(String username) {
        userRepository.save(new User(username));
    }

    public User getByIndex(int index) {
        return userRepository.getByIndex(index);
    }
}
