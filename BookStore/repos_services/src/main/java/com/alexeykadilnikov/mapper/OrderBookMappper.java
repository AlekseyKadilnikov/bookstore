package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.OrderBookDto;
import com.alexeykadilnikov.entity.OrderBook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderBookMappper implements IMapper<OrderBook, OrderBookDto> {

    private final ModelMapper mapper;

    @Autowired
    public OrderBookMappper(ModelMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public OrderBook toEntity(OrderBookDto dto) {
        return null;
    }

    @Override
    public OrderBookDto toDto(OrderBook entity) {
        return null;
    }
}
