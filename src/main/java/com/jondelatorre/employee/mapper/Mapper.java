package com.jondelatorre.employee.mapper;

public interface Mapper<D, M> {

    public M toModel(D dto);

    public D toDto(M entity);

}
