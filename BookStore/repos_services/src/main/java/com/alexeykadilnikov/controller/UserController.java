package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.service.IUserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@NoArgsConstructor
@RequestMapping("users")
public class UserController {
    private IUserService userService;
    private JmsTemplate jmsTemplate;

    @Autowired
    public UserController(IUserService userService, JmsTemplate jmsTemplate) {
        this.userService = userService;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping()
    public UserDto save(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.save(userDto);
        jmsTemplate.convertAndSend("user", "saved: id = " + newUserDto.getId());
        return newUserDto;
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('users:read')")
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
