package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;

import java.util.List;

public interface IUserService {

    List<UserDto> getAll();

    UserDto getById(long id);

    UserDto save(UserDto user);

    UserDto update(UserDto user);
}
