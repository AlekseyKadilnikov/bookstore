package com.alexeykadilnikov.repository;


import com.alexeykadilnikov.entity.User;


public class UserRepository implements IRepository<User, Long>{
    private User user;

    public UserRepository() {
    }

    @Override
    public User[] findAll() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public void save(User user) {
        this.user = user;
    }

    @Override
    public void delete(User user) {

    }

    public User getUser() {
        return user;
    }
}
