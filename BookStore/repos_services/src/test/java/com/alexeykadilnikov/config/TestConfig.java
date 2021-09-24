package com.alexeykadilnikov.config;

import com.alexeykadilnikov.controller.*;
import com.alexeykadilnikov.service.IBookService;
import com.alexeykadilnikov.service.IOrderService;
import com.alexeykadilnikov.service.IRequestService;
import com.alexeykadilnikov.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {
    @Bean
    public BookController bookController() {
        return new BookController();
    }
    @Bean
    public OrderController orderController() {
        return new OrderController();
    }
    @Bean
    public RequestController requestController() {
        return new RequestController();
    }
    @Bean
    public UserController userController() {
        return new UserController();
    }
    @Bean
    public AuthenticationController authenticationController() {
        return new AuthenticationController();
    }

    @Bean
    public IBookService bookService() {
        return mock(IBookService.class);
    }

    @Bean
    public IOrderService orderService() {
        return mock(IOrderService.class);
    }

    @Bean
    public IRequestService requestService() {
        return mock(IRequestService.class);
    }

    @Bean
    public IUserService userService() {
        return mock(IUserService.class);
    }
}
