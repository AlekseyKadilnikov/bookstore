package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @InjectBean
    private IUserRepository userRepository;

    @Override
    public int addUser(String username) {
        for(User user : userRepository.findAll()) {
            if(user.getUsername().equals(username)) {
                return 1;
            }
        }
        User user = new User(username);
        userRepository.save(user);
        return 0;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveAll(List<User> userList) {
        userRepository.saveAll(userList);
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByName(String name) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(name))
                .findAny()
                .orElse(null);
    }
}
