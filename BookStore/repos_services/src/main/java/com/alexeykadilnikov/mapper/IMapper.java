package com.alexeykadilnikov.mapper;

public interface IMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);
}
