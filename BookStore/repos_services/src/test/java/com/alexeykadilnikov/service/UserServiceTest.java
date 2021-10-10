package com.alexeykadilnikov.service;

import com.alexeykadilnikov.Role;
import com.alexeykadilnikov.Status;
import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.mapper.UserMapper;
import com.alexeykadilnikov.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    private IUserService userService;

    @MockBean
    private IUserRepository userRepository;

    long userId = 1L;

    @BeforeAll
    void init(){

        UserMapper userMapper = new UserMapper(new ModelMapper());

        userService = new UserService(userRepository, userMapper);
    }

    @Test
    void shouldFetchAllBooks() {
        List<User> testUsers = new ArrayList<>();
        testUsers.add(new User("email", "name", "1111", Role.USER, Status.ACTIVE));
        testUsers.add(new User("email1", "name1", "1111", Role.USER, Status.ACTIVE));
        testUsers.add(new User("email2", "name2", "1111", Role.USER, Status.ACTIVE));

        List<UserDto> testUsersDto = new ArrayList<>();
        testUsersDto.add(new UserDto("email", "name", Role.USER.name(), Status.ACTIVE.name()));
        testUsersDto.add(new UserDto("email1", "name1", Role.USER.name(), Status.ACTIVE.name()));
        testUsersDto.add(new UserDto("email2", "name2", Role.USER.name(), Status.ACTIVE.name()));

        given(userRepository.findAll()).willReturn(testUsers);

        List<UserDto> result = userService.getAll();

        Assertions.assertIterableEquals(testUsersDto, result);
    }

    @Test
    void shouldFetchOneBookById() {
        User user = new User("email", "name", "1111", Role.USER, Status.ACTIVE);
        user.setId(userId);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserDto userDto = userService.getById(userId);

        Assertions.assertEquals(userId, userDto.getId());
    }
}
