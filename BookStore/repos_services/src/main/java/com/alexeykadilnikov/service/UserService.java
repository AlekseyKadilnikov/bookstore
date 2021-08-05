package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.UserRepository;

public class UserService implements IUserService {
    private static UserService instance;

    private final UserRepository userRepository;

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(String username) {
        userRepository.save(new User(username));
    }

    public static UserService getInstance() {
        if(instance == null) {
            instance = new UserService(UserRepository.getInstance());
        }
        return instance;
    }

    public User getByIndex(int index) {
        return userRepository.getByIndex(index);
    }
}
