package com.jondelatorre.employee.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jondelatorre.employee.dto.EmployeeDto;
import com.jondelatorre.employee.error.AlreadyDeletedException;
import com.jondelatorre.employee.error.NotFoundException;
import com.jondelatorre.employee.mapper.Mapper;
import com.jondelatorre.employee.model.Employee;
import com.jondelatorre.employee.repository.EmployeeRepository;

@RestController
@RequestMapping(value = EmployeeController.EMPLOYEE_REQUEST_MAPPING)
public class EmployeeController {

    private static final String ENTITY_FIELD = "id";

    private static final String ENTITY_NAME = "Employee";
    public static final String EMPLOYEE_REQUEST_MAPPING = "/employee";

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private Mapper<EmployeeDto, Employee> employeeMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeeDto createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toModel(employeeDto);
        employee.setStatus(true);
        employee.setCreated(LocalDateTime.now());
        employee = employeeRepository.insert(employee);

        logger.debug("Created employee: {}", employee);
        return employeeMapper.toDto(employee);
    }

    @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public EmployeeDto retrieveEmployee(@PathVariable Long employeeId) {
        return employeeMapper.toDto(employeeRepository.findByIdAndStatusIsTrue(employeeId)
                .orElseThrow(() -> new NotFoundException(ENTITY_NAME, ENTITY_FIELD,
                        employeeId.toString())));
    }

    @PutMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public EmployeeDto updateEmployee(@Valid @RequestBody EmployeeDto employeeDto,
            @PathVariable Long employeeId) {
        Employee employee = employeeMapper.toModel(employeeDto);
        if (!employeeRepository.existsById(employeeId)) {
            throw new NotFoundException(ENTITY_NAME, ENTITY_FIELD, employeeId.toString());
        }
        employee.setId(employeeId);
        employee.setStatus(true);
        employee = employeeRepository.save(employee);

        logger.debug("Updated employee: {}", employee);
        return employeeMapper.toDto(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException(ENTITY_NAME, ENTITY_FIELD, employeeId.toString()));

        if (!employee.getStatus()) {
            throw new AlreadyDeletedException(ENTITY_NAME, employeeId.toString());
        }

        employee.setStatus(false);
        employeeRepository.save(employee);

        logger.debug("Soft deleted employee: {}", employee);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<EmployeeDto> retrieveAllEmployees() {
        return employeeRepository.findByStatusIsTrue().stream()
                .map(entity -> employeeMapper.toDto(entity)).collect(Collectors.toList());
    }

}
