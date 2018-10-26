package com.jondelatorre.employee.mapper;

public interface Mapper<D, E> {

    public E toEntity(D dto);

    public D toDto(E entity);

}
