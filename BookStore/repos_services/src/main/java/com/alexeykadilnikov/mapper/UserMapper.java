package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper implements IMapper<User, UserDto> {

    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User toEntity(UserDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, User.class);
    }

    @Override
    public UserDto toDto(User entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, UserDto.class);
    }
}
