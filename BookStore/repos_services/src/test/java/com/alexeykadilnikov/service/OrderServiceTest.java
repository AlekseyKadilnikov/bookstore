package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.mapper.OrderMapper;
import com.alexeykadilnikov.repository.IOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceTest {
    private IOrderService orderService;

    @MockBean
    private IOrderRepository orderRepository;

    long orderId = 1L;

    @BeforeAll
    void init(){

        OrderMapper orderMapper = new OrderMapper(new ModelMapper());

        orderService = new OrderService(orderRepository, orderMapper);
    }

    @Test
    void shouldFetchOneOrderById() {
        OrderBook orderBook = new OrderBook();
        orderBook.setBook(new Book());
        orderBook.setOrder(new Order());
        User user = new User();
        Order order = new Order(Set.of(orderBook), user);
        order.setId(orderId);
        order.setTotalPrice(1000);
        order.setStatus(OrderStatus.NEW);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        OrderDto orderDto = orderService.getById(orderId);

        Assertions.assertEquals(orderId, orderDto.getId());
    }
}
