package com.jondelatorre.employee.mapper;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jondelatorre.employee.dto.EmployeeDto;
import com.jondelatorre.employee.model.Employee;

@Component
public class EmployeeDtoToEmployeeMapper implements Mapper<EmployeeDto, Employee> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDtoToEmployeeMapper.class);

    @NotNull
    @Override
    public Employee toModel(final EmployeeDto dto) {
        final Employee model = new Employee();
        logger.debug("About to map dto: {}", dto);
        BeanUtils.copyProperties(dto, model);
        model.setMiddleNameInitial(dto.getMiddleInitial());
        model.setBirthdate(dto.getDateOfBirth());
        model.setEmploymentDate(dto.getDateOfEmployment());

        logger.debug("Mapped to model: {}", model);
        return model;
    }

    @NotNull
    @Override
    public EmployeeDto toDto(final Employee model) {
        final EmployeeDto dto = new EmployeeDto();
        logger.debug("About to map model: {}", model);
        BeanUtils.copyProperties(model, dto);
        dto.setMiddleInitial(model.getMiddleNameInitial());
        dto.setDateOfBirth(model.getBirthdate());
        dto.setDateOfEmployment(model.getEmploymentDate());

        logger.debug("Mapped to dto: {}", dto);
        return dto;
    }

}
