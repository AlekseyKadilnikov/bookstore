package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.User;

public class UserRepository implements IRepository<User, Long>{
    private User[] users = new User[0];

    public UserRepository() {
    }

    @Override
    public User[] findAll() {
        return users;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public void save(User newUser) {
        User[] newUsers = new User[users.length + 1];
        System.arraycopy(users, 0, newUsers, 0, users.length);
        newUsers[users.length] = newUser;
        users = newUsers;
    }

    @Override
    public void delete(User user) {

    }

    public User getByIndex(int index) {
        return users[index];
    }
}
