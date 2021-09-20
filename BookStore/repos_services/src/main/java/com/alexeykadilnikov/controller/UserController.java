package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("users")
public class UserController {
    private final IUserService userService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public UserController(IUserService userService, JmsTemplate jmsTemplate) {
        this.userService = userService;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping()
    public UserDto save(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
