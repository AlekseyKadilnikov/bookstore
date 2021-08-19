package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.annotation.Singleton;
import com.alexeykadilnikov.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class UserRepository implements IUserRepository {
    private List<User> users = new ArrayList<>();

    {
        users.add(new User("admin"));
        users.add(new User("testUser"));
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

    @Override
    public void saveAll(List<User> all) {
        users = all;
    }
}
