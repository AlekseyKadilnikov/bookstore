package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.service.*;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('orders:write')")
    public OrderDto save(@RequestBody OrderDto orderDto) {
        return orderService.save(orderDto);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('orders:read')")
    public List<OrderDto> getAll() {
        return orderService.getAll();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('orders:read')")
    public OrderDto getById(@PathVariable("id") long id) {
        return orderService.getById(id);
    }

    @PatchMapping("{id}/status")
    @PreAuthorize("hasAuthority('orders:update')")
    public OrderDto setStatus(@PathVariable("id") int orderId,
                              @RequestParam("status") int statusCode) {
        return orderService.setStatus(orderId, statusCode);
    }

    @GetMapping("sort/status")
    @PreAuthorize("hasAuthority('orders:read')")
    public List<OrderDto> sortByStatus(@RequestParam("status") int statusCode) {
        return orderService.sortByStatus(statusCode);
    }

    @GetMapping("sort/{sortBy}")
    @PreAuthorize("hasAuthority('orders:read')")
    public List<OrderDto> sortBy(@PathVariable("sortBy") String sortBy,
                                 @RequestParam("mode") int mode,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate) {
        if(startDate == null || endDate == null)
            return orderService.sortBy(sortBy, mode);
        else {
            return orderService.sortForPeriod(sortBy, mode, startDate, endDate);
        }
    }

    @GetMapping("countComplete")
    @PreAuthorize("hasAuthority('orders:read')")
    public int getCountOfCompleteOrdersForPeriod(@RequestParam(value = "startDate") String startDate,
                                                  @RequestParam(value = "endDate") String endDate) {
        return orderService.getCountOfCompleteOrdersForPeriod(startDate, endDate);
    }

    @GetMapping("earnedMoney")
    @PreAuthorize("hasAuthority('orders:read')")
    public int getEarnedMoneyForPeriod(@RequestParam(value = "startDate") String startDate,
                                        @RequestParam(value = "endDate") String endDate) {
        return orderService.getEarnedMoneyForPeriod(startDate, endDate);
    }
}
