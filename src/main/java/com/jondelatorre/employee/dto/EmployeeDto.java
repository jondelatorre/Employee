package com.jondelatorre.employee.dto;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {

    @NotNull
    @Positive(message = "id cannot be negative")
    @Max(value = Long.MAX_VALUE, message = "id cannot be bigger than " + Long.MAX_VALUE)
    private Long id;

    @NotNull
    @NotEmpty(message = "firstName cannot be empty")
    private String firstName;

    @Size(max = 1, min = 1, message = "middleInitial shoul be a single letter")
    private String middleInitial;

    @NotNull
    @NotEmpty(message = "lastName cannot be empty")
    private String lastName;

    @NotNull
    @Past(message = "dateOfBirth should be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfEmployment;

    @AssertTrue(
            message = "status can only be true. If you wish to delete an employee, please call DELETE /employee/{employeeId}")
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateOfBirth, dateOfEmployment, firstName, id, lastName, middleInitial,
                status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmployeeDto other = (EmployeeDto) obj;
        return Objects.equals(dateOfBirth, other.dateOfBirth)
                && Objects.equals(dateOfEmployment, other.dateOfEmployment)
                && Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(middleInitial, other.middleInitial)
                && Objects.equals(status, other.status);
    }

    @Override
    public String toString() {
        return "EmployeeDto [id=" + id + ", firstName=" + firstName + ", middleInitial="
                + middleInitial + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
                + ", dateOfEmployment=" + dateOfEmployment + ", status=" + status + "]";
    }



}
