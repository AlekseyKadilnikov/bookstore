package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.AuthorDto;
import com.alexeykadilnikov.entity.Author;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthorMapper implements IMapper<Author, AuthorDto> {

    private final ModelMapper mapper;

    @Autowired
    public AuthorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Author toEntity(AuthorDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Author.class);
    }

    @Override
    public AuthorDto toDto(Author entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, AuthorDto.class);
    }
}
