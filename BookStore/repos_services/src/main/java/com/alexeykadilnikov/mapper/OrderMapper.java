package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.OrderBook;
import com.alexeykadilnikov.entity.OrderBookKey;
import com.alexeykadilnikov.repository.IBookRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OrderMapper implements IMapper<Order, OrderDto> {

    private final ModelMapper mapper;
    private final IBookRepository bookRepository;

    @Autowired
    public OrderMapper(ModelMapper mapper, IBookRepository bookRepository) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;
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
        Set<OrderBook> orderBooks = source.getOrderBooks();
        Map<Long, Integer> books = new HashMap<>();
        for(OrderBook orderBook : orderBooks) {
            books.put(orderBook.getBook().getId(), orderBook.getBookCount());
        }
        destination.setBooks(books);
    }

    private void mapSpecificFields(OrderDto source, Order destination) {
        destination.setStatus(OrderStatus.valueOf(source.getOrderStatus()));
        Map<Long, Integer> books = source.getBooks();
        Set<OrderBook> orderBooks = new HashSet<>();
        for(Map.Entry<Long, Integer> entry : books.entrySet()) {
            OrderBook orderBook = new OrderBook();
            OrderBookKey orderBookKey = new OrderBookKey();
            orderBook.setId(orderBookKey);
            Book book = bookRepository.getById(entry.getKey());
            orderBook.setBook(book);
            orderBook.setOrder(destination);
            orderBook.setBookCount(entry.getValue());
            orderBooks.add(orderBook);
        }
        destination.setOrderBooks(orderBooks);
    }
}
