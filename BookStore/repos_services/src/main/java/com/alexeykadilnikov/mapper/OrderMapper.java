package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.dto.OrderBookDto;
import com.alexeykadilnikov.dto.OrderBookKeyDto;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.OrderBook;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class OrderMapper implements IMapper<Order, OrderDto> {

    private final ModelMapper mapper;

    @Autowired
    public OrderMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Order toEntity(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    @Override
    public OrderDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Order.class, OrderDto.class)
                .addMappings(m -> m.skip(OrderDto::setOrderStatus)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(OrderDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setStatus))
                .setPostConverter(toEntityConverter());
    }

    public Converter<Order, OrderDto> toDtoConverter() {
        return mappingContext -> {
            Order source = mappingContext.getSource();
            OrderDto destination = mappingContext.getDestination();
            mapSpecificFields(source, destination);
            return mappingContext.getDestination();
        };
    }

    public Converter<OrderDto, Order> toEntityConverter() {
        return mappingContext -> {
            OrderDto source = mappingContext.getSource();
            Order destination = mappingContext.getDestination();
            mapSpecificFields(source, destination);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(Order source, OrderDto destination) {
        destination.setOrderStatus(source.getStatus().name());
    }

    private void mapSpecificFields(OrderDto source, Order destination) {
        destination.setStatus(OrderStatus.valueOf(source.getOrderStatus()));
    }
}
