package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.dao.IUserDAO;
import com.alexeykadilnikov.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final IUserDAO userDAO;
    private final UserMapper userMapper;

    @Autowired
    public UserService(IUserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    public List<User> sendSqlQuery(String hql) {
        return userDAO.findAll(hql);
    }

    public List<UserDto> getAll() {
        List<User> users = userDAO.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for(User user : users) {
            usersDto.add(userMapper.toDto(user));
        }
        return usersDto;
    }

    public UserDto getById(long id) {
        User user = userDAO.getById(id);
        if(user == null) {
            throw new NullPointerException("User with id = " + id + " not found");
        }
        return userMapper.toDto(user);
    }

    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userDAO.save(user);
        userDto = userMapper.toDto(user);
        return userDto;
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userDAO.update(userMapper.toEntity(userDto)));
    }
}
