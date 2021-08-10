package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.UserRepository;

import java.util.List;

public class UserService implements IUserService {
    private static UserService instance;

    private final UserRepository userRepository;

    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int addUser(String username) {
        for(User user : userRepository.findAll()) {
            if(user.getUsername().equals(username)) {
                return 1;
            }
        }
        userRepository.save(new User(username));
        System.out.println("User successfully created.");
        return 0;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
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

    public User getById(long id) {
        return userRepository.getById(id);
    }

    public User getByName(String name) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(name))
                .findAny()
                .orElse(null);
    }
}
