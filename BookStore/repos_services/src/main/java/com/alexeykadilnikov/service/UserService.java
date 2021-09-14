package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.dao.IUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final IUserDAO userDAO;

    @Autowired
    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public int addUser(String username) {
        for(User user : userDAO.findAll()) {
            if(user.getUsername().equals(username)) {
                return 1;
            }
        }
        User user = new User(username);
        userDAO.save(user);
        return 0;
    }

    public void addUser(User user) {
        userDAO.save(user);
    }

    public List<User> getAll() {
        return userDAO.findAll();
    }

    public void saveAll(List<User> userList) {
        for(User user : userList) {
            userDAO.save(user);
        }
    }

    public User getById(long id) {
        return userDAO.getById(id);
    }

    public User getByName(String name) {
        return userDAO.findAll().stream()
                .filter(u -> u.getUsername().equals(name))
                .findAny()
                .orElse(null);
    }
}
