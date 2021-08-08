package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository implements IRepository<User, Long>{
    private static UserRepository instance;

    private List<User> users = new ArrayList<>();

    {
        users.add(new User("admin"));
        users.add(new User("testUser"));
    }

    private UserRepository() {
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User getById(Long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(User newUser) {
        users.add(newUser);
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    public User getByIndex(int index) {
        return users.get(index);
    }

    public static UserRepository getInstance() {
        if(instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
