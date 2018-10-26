package com.jondelatorre.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jondelatorre.employee.entity.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, Long> {

    Optional<Employee> findByIdAndStatusIsTrue(Long id);

    List<Employee> findByStatusIsTrue();

}
