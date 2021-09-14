package com.alexeykadilnikov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookDto {
    private OrderBookKeyDto id;
    private OrderDto order;
    private BookDto book;
    private int bookCount;
}
