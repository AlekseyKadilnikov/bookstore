package com.alexeykadilnikov.mapper;

import com.alexeykadilnikov.dto.RequestDto;
import com.alexeykadilnikov.entity.Request;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RequestMapper implements IMapper<Request, RequestDto> {

    private final ModelMapper mapper;

    @Autowired
    public RequestMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Request toEntity(RequestDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Request.class);
    }

    @Override
    public RequestDto toDto(Request entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, RequestDto.class);
    }
}
