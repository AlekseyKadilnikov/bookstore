package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IOrderDAO;
import com.alexeykadilnikov.dto.OrderBookDto;
import com.alexeykadilnikov.dto.OrderBookKeyDto;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.OrderBook;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class OrderBookMapper implements IMapper<OrderBook, OrderBookDto> {

    private final ModelMapper mapper;
    private final IOrderDAO orderDAO;
    private final IBookDAO bookDAO;

    @Autowired
    public OrderBookMapper(ModelMapper mapper, IOrderDAO orderDAO, IBookDAO bookDAO) {
        this.mapper = mapper;
        this.orderDAO = orderDAO;
        this.bookDAO = bookDAO;
    }

    @Override
    public OrderBook toEntity(OrderBookDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderBook.class);
    }

    @Override
    public OrderBookDto toDto(OrderBook entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderBookDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(OrderBook.class, OrderBookDto.class)
                .addMappings(m -> m.skip(OrderBookDto::setId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(OrderBookDto.class, OrderBook.class)
                .addMappings(m -> m.skip(OrderBook::setBook))
                .addMappings(m -> m.skip(OrderBook::setOrder))
                .setPostConverter(toEntityConverter());
    }

    public Converter<OrderBook, OrderBookDto> toDtoConverter() {
        return mappingContext -> {
            OrderBook source = mappingContext.getSource();
            OrderBookDto destination = mappingContext.getDestination();
            mapSpecificFields(source, destination);
            return mappingContext.getDestination();
        };
    }

    public Converter<OrderBookDto, OrderBook> toEntityConverter() {
        return mappingContext -> {
            OrderBookDto source = mappingContext.getSource();
            OrderBook destination = mappingContext.getDestination();
            mapSpecificFields(source, destination);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(OrderBook source, OrderBookDto destination) {
        OrderBookKeyDto orderBookKeyDto = new OrderBookKeyDto();
        orderBookKeyDto.setBookId(source.getBook().getId());
        orderBookKeyDto.setOrderId(source.getOrder().getId());
        destination.setId(orderBookKeyDto);
    }

    private void mapSpecificFields(OrderBookDto source, OrderBook destination) {
        destination.setBook(bookDAO.getById(source.getId().getBookId()));
        destination.setOrder(orderDAO.getById(source.getId().getOrderId()));
    }
}
