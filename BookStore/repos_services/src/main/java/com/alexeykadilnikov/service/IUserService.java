package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;

import java.util.List;

public interface IUserService {

    UserDto addUser(UserDto user);

    List<UserDto> getAll();

    UserDto getById(long id);

    UserDto getByName(String name);

    UserDto save(UserDto user);
}
