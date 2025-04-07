package com.eventregistration.mapper;

import java.util.List;

/**
 * Base mapper interface with common mapping methods
 * @param <D> DTO type
 * @param <E> Entity type
 */
public interface BaseMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);
}
