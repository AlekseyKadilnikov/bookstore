package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Role;
import com.alexeykadilnikov.Status;
import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

    @BeforeAll
    static void init() {
        MySql.container.start();
    }

    @Test
    void saveUser_and_getUserById() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("name");
        user.setEmail("email");
        user.setPassword("1111");
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);

        User returnedUser = userRepository.save(user);

        Optional<User> returned = userRepository.findById(returnedUser.getId());

        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(user.getId(), returned.get().getId());
    }

    @Test
    void saveUser_and_deleteUser() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("name");
        user.setEmail("email");
        user.setPassword("1111");
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);

        userRepository.save(user);
        userRepository.delete(user);

        int size = userRepository.findAll().size();

        Assertions.assertEquals(0, size);
    }

    @Test
    void saveUser_and_findAllUsers() {
        userRepository.deleteAll();

        User userOne = new User("email", "user", "1111", Role.USER, Status.ACTIVE);
        User userTwo = new User("email", "user", "1111", Role.USER, Status.ACTIVE);
        userRepository.save(userOne);
        userRepository.save(userTwo);

        List<User> users = userRepository.findAll();

        Assertions.assertEquals(2, users.size());
    }

    @Test
    void save_and_updateUser() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("name");
        user.setEmail("email");
        user.setPassword("1111");
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);

        User returned = userRepository.save(user);

        Assertions.assertNotNull(returned);

        long idBefore = returned.getId();

        user.setUsername("renamed");
        returned = userRepository.save(user);

        Assertions.assertNotNull(returned);

        long idAfter = returned.getId();
        String nameAfter = returned.getUsername();

        Assertions.assertEquals(idBefore, idAfter);
        Assertions.assertEquals(user.getUsername(), nameAfter);
    }
}
