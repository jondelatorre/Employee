package com.jondelatorre.employee.mapper;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jondelatorre.employee.dto.EmployeeDto;
import com.jondelatorre.employee.entity.Employee;

@Component
public class EmployeeDtoToEmployeeMapper implements Mapper<EmployeeDto, Employee> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDtoToEmployeeMapper.class);

    @NotNull
    @Override
    public Employee toEntity(final EmployeeDto dto) {
        final Employee entity = new Employee();
        logger.debug("About to map dto: {}", dto);
        BeanUtils.copyProperties(dto, entity);
        entity.setMiddleNameInitial(dto.getMiddleInitial());
        entity.setBirthdate(dto.getDateOfBirth());
        entity.setEmploymentDate(dto.getDateOfEmployment());

        logger.debug("Mapped to entity: {}", entity);
        return entity;
    }

    @NotNull
    @Override
    public EmployeeDto toDto(final Employee entity) {
        final EmployeeDto dto = new EmployeeDto();
        logger.debug("About to map entity: {}", entity);
        BeanUtils.copyProperties(entity, dto);
        dto.setMiddleInitial(entity.getMiddleNameInitial());
        dto.setDateOfBirth(entity.getBirthdate());
        dto.setDateOfEmployment(entity.getEmploymentDate());

        logger.debug("Mapped to dto: {}", dto);
        return dto;
    }

}
