package com.alexeykadilnikov.dto;

import com.alexeykadilnikov.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends BaseEntityDto {
    private int totalPrice;
    private OrderStatus orderStatus;
    private UserDto user;
}
