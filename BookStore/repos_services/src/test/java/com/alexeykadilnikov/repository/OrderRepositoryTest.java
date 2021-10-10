package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.Role;
import com.alexeykadilnikov.Status;
import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class OrderRepositoryTest {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;


    private static Order order1;
    private static Order order2;
    private static Order order3;
    private static Order order4;
    private static Order order5;

    private static User user;

    @BeforeAll
    static void init() {

        user = new User();
        user.setUsername("name");
        user.setEmail("email");
        user.setPassword("1111");
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);

        order1 = new Order();
        order1.setStatus(OrderStatus.NEW);
        order1.setTotalPrice(600);
        order1.setInitDate(LocalDateTime.now());
        order1.setUser(user);

        order2 = new Order();
        order2.setStatus(OrderStatus.SUCCESS);
        order2.setTotalPrice(700);
        order2.setInitDate(LocalDateTime.now());
        order2.setExecutionDate(LocalDateTime.of(2021, 7, 9, 8, 10));
        order2.setUser(user);

        order3 = new Order();
        order3.setStatus(OrderStatus.SUCCESS);
        order3.setTotalPrice(800);
        order3.setInitDate(LocalDateTime.now());
        order3.setExecutionDate(LocalDateTime.of(2021, 8, 9, 8, 10));
        order3.setUser(user);

        order4 = new Order();
        order4.setStatus(OrderStatus.SUCCESS);
        order4.setTotalPrice(900);
        order4.setInitDate(LocalDateTime.now());
        order4.setExecutionDate(LocalDateTime.of(2021, 9, 9, 8, 10));
        order4.setUser(user);

        order5 = new Order();
        order5.setStatus(OrderStatus.CANCELED);
        order5.setTotalPrice(1000);
        order5.setInitDate(LocalDateTime.now());
        order5.setUser(user);

        MySql.container.start();
    }

    @Test
    void saveOrder_and_getOrderById() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        User savedUser = userRepository.save(user);
        order1.setUser(savedUser);
        Order savedOrder = orderRepository.save(order1);

        Optional<Order> returnedById = orderRepository.findById(savedOrder.getId());

        Assertions.assertTrue(returnedById.isPresent());
        Assertions.assertEquals(savedOrder.getId(), returnedById.get().getId());
        Assertions.assertEquals(savedUser.getId(), returnedById.get().getUser().getId());
        Assertions.assertNotNull(returnedById.get().getOrderBooks());
    }

    @Test
    void saveOrder_and_deleteOrder() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(user);

        Order saved = orderRepository.save(order1);
        orderRepository.deleteById(saved.getId());

        int size = orderRepository.findAll().size();

        Assertions.assertEquals(0, size);
    }

    @Test
    void saveOrder_and_findAllOrders() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        User savedUser = userRepository.save(user);
        order1.setUser(savedUser);
        orderRepository.save(order1);
        order2.setUser(savedUser);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();

        Assertions.assertEquals(2, orders.size());
    }

    @Test
    void save_and_updateOrder() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        User savedUser = userRepository.save(user);
        order1.setUser(savedUser);

        Order returned = orderRepository.save(order1);

        Assertions.assertNotNull(returned);

        long idBefore = returned.getId();

        returned.setTotalPrice(601);
        returned = orderRepository.save(returned);

        Assertions.assertNotNull(returned);

        long idAfter = returned.getId();

        Assertions.assertEquals(idBefore, idAfter);
        Assertions.assertEquals(601, returned.getTotalPrice());
    }
}
