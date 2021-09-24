package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.mapper.UserMapper;
import com.alexeykadilnikov.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository,
                       UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> sendSqlQuery(String hql) {
        return userRepository.findAll();
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for(User user : users) {
            usersDto.add(userMapper.toDto(user));
        }
        return usersDto;
    }

    public UserDto getById(long id) {
        User user = userRepository.getById(id);
        if(user == null) {
            throw new NullPointerException("User with id = " + id + " not found");
        }
        return userMapper.toDto(user);
    }

    public UserDto save(UserDto userDto) {
//        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//        User user = userMapper.toEntity(userDto);
//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.getById(1L));
//        user.setRoles(roles);
//        user = userRepository.save(user);
//        userDto = userMapper.toDto(user);
        return userDto;
    }
}
