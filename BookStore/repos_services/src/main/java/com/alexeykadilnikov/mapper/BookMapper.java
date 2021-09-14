package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookMapper implements IMapper<Book, BookDto>{
    @Autowired
    private ModelMapper mapper;

    @Override
    public Book toEntity(BookDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Book.class);
    }

    @Override
    public BookDto toDto(Book entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, BookDto.class);
    }
}
