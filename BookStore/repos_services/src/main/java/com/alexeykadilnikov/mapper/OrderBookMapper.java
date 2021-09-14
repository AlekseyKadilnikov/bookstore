package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.OrderBookDto;
import com.alexeykadilnikov.entity.OrderBook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderBookMapper implements IMapper<OrderBook, OrderBookDto> {

    private final ModelMapper mapper;

    @Autowired
    public OrderBookMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public OrderBook toEntity(OrderBookDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderBook.class);
    }

    @Override
    public OrderBookDto toDto(OrderBook entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderBookDto.class);
    }
}
