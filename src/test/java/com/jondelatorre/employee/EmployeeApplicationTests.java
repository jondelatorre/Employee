package com.jondelatorre.employee;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.util.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jondelatorre.employee.config.Config;
import com.jondelatorre.employee.config.MongoDbConfig;
import com.jondelatorre.employee.controller.EmployeeController;
import com.jondelatorre.employee.mapper.EmployeeDtoToEmployeeMapper;
import com.jondelatorre.employee.model.Employee;
import com.jondelatorre.employee.repository.EmployeeRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.jondelatorre.employee.controller.EmployeeController.EMPLOYEE_REQUEST_MAPPING;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@Import({MongoDbConfig.class, Config.class,EmployeeDtoToEmployeeMapper.class})
public class EmployeeApplicationTests {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private Employee saveEmployee(Long id, String name, String middleInitial, String lastName, 
            LocalDate birthDate, LocalDate employmentDate) {
        return saveEmployee(id,name,middleInitial,lastName,birthDate,employmentDate,true);
    }
    
    private Employee saveEmployee(Long id, String name, String middleInitial, String lastName, 
            LocalDate birthDate, LocalDate employmentDate, Boolean status) {
        final Employee e = new Employee();
        e.setId(id);
        e.setCreated(LocalDateTime.now());
        e.setFirstName(name);
        e.setMiddleNameInitial(middleInitial);
        e.setLastName(lastName);
        e.setStatus(status);
        e.setBirthdate(birthDate);
        e.setEmploymentDate(employmentDate);
        employeeRepository.save(e);
        return e;
    }
    
    @Test
    public void postEmployeeNoData() throws Exception {
        this.mvc.perform(post(EMPLOYEE_REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.path", is("/employee")))
        .andExpect(jsonPath("$.message", startsWith("Required request body is missing")));
    }
    
    @Test
    public void postEmployeeEmptyData() throws Exception {
        this.mvc.perform(post(EMPLOYEE_REQUEST_MAPPING).content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.status", is(400)))
        .andExpect(jsonPath("$.path", is("/employee")))
        .andExpect(jsonPath("$.message", startsWith("Validation failed for argument")));
    }
    
    @Test
    public void postEmployeeMinDataTest() throws Exception {
        String json = Files.contentOf(Paths.get("src","test","resources","employee-tests", "min-data.json")
                .toFile(), StandardCharsets.UTF_8);
        this.mvc.perform(post(EMPLOYEE_REQUEST_MAPPING).content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Teenage")))
        .andExpect(jsonPath("$.lastName", is("Ninja")))
        .andExpect(jsonPath("$.dateOfBirth", is("1987-05-07")))
        .andExpect(jsonPath("$.dateOfEmployment", is("2005-04-07")))
        .andExpect(jsonPath("$.status", is(true)));
        employeeRepository.deleteAll();
    }
    
    @Test
    public void postEmployeeExtremeDataTest() throws Exception {
        Long id = 1312323123123l;
        String firstName = "!\\\"#$%&\\\\¨´*/()'}";
        String lastName = "ñ?&/%<>";
        String dateOfBirth = "0001-01-01";
        String dateOfEmployment = "9999-12-31";
        String json = "{"
                + "\"id\":"+id+","
                + "\"firstName\":\""+firstName+"\","
                + "\"middleInitial\":\"\\\"\","
                + "\"lastName\":\""+lastName+"\","
                + "\"dateOfBirth\":\""+dateOfBirth+"\","
                + "\"dateOfEmployment\":\""+dateOfEmployment+"\""
                +"}";
        this.mvc.perform(post(EMPLOYEE_REQUEST_MAPPING).content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(id)))
        .andExpect(jsonPath("$.firstName", is("!\"#$%&\\¨´*/()'}")))
        .andExpect(jsonPath("$.middleInitial", is("\"")))
        .andExpect(jsonPath("$.lastName", is(lastName)))
        .andExpect(jsonPath("$.dateOfBirth", is(dateOfBirth)))
        .andExpect(jsonPath("$.dateOfEmployment", is(dateOfEmployment)))
        .andExpect(jsonPath("$.status", is(true)));
        employeeRepository.deleteAll();
    }
    
    @Test
    public void postEmployeeDuplicatedIdTest() throws Exception {
        saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03));
        String json = Files.contentOf(Paths.get("src","test","resources","employee-tests", "min-data.json")
                .toFile(), StandardCharsets.UTF_8);
        this.mvc.perform(post(EMPLOYEE_REQUEST_MAPPING).content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(409)))
        .andExpect(jsonPath("$.path", is("/employee")))
        .andExpect(jsonPath("$.message", startsWith("The chosen id already exists in the database. Please chose another one.")));
        employeeRepository.deleteAll();
    }
    
	@Test
	public void getEmployeeByIdTest() throws Exception {
	    Employee emp = saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03));
	    this.mvc.perform(get(EMPLOYEE_REQUEST_MAPPING+"/1").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is(emp.getFirstName())))
        .andExpect(jsonPath("$.middleInitial", is(emp.getMiddleNameInitial())))
        .andExpect(jsonPath("$.lastName", is(emp.getLastName())))
        .andExpect(jsonPath("$.dateOfBirth", is("1987-04-17")))
        .andExpect(jsonPath("$.dateOfEmployment", is("2007-11-03")))
        .andExpect(jsonPath("$.status", is(true)));
	    employeeRepository.deleteAll();
	}
	
	@Test
    public void getEmployeeNotFoundTest() throws Exception {
        this.mvc.perform(get(EMPLOYEE_REQUEST_MAPPING+"/9999").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.path", is("/employee/9999")))
        .andExpect(jsonPath("$.message", startsWith("Employee with id = 9999 not found in the database")));
    }
	
	@Test
    public void getAllEmployeesByIdTest() throws Exception {
        saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03));
        saveEmployee(2l,"Juan","A","Perez",LocalDate.of(1950, 9, 23),LocalDate.of(1975, 12, 21));
        this.mvc.perform(get(EMPLOYEE_REQUEST_MAPPING).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[1].id", is(2)));
        employeeRepository.deleteAll();
    }
	
	@Test
    public void putEmployeeMinDataTest() throws Exception {
	    saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03));
	    String json = Files.contentOf(Paths.get("src","test","resources","employee-tests", "min-data.json")
                .toFile(), StandardCharsets.UTF_8);
        this.mvc.perform(put(EMPLOYEE_REQUEST_MAPPING+"/1").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Teenage")))
        .andExpect(jsonPath("$.lastName", is("Ninja")))
        .andExpect(jsonPath("$.dateOfBirth", is("1987-05-07")))
        .andExpect(jsonPath("$.dateOfEmployment", is("2005-04-07")))
        .andExpect(jsonPath("$.status", is(true)));
        employeeRepository.deleteAll();
    }
	
	@Test
    public void putEmployeeNotFoundTest() throws Exception {
        String json = Files.contentOf(Paths.get("src","test","resources","employee-tests", "min-data.json")
                .toFile(), StandardCharsets.UTF_8);
        this.mvc.perform(put(EMPLOYEE_REQUEST_MAPPING+"/1").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.path", is("/employee/1")))
        .andExpect(jsonPath("$.message", startsWith("Employee with id = 1 not found in the database")));
        employeeRepository.deleteAll();
    }
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void deleteEmployeeByIdRegularTest() throws Exception {
        saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03));
        this.mvc.perform(delete(EMPLOYEE_REQUEST_MAPPING+"/1").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());
        employeeRepository.deleteAll();
    }
	
	@Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void deleteEmployeeByIdNotFoundTest() throws Exception {
        this.mvc.perform(delete(EMPLOYEE_REQUEST_MAPPING+"/1111").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.path", is("/employee/1111")))
        .andExpect(jsonPath("$.message", startsWith("Employee with id = 1111 not found in the database")));
    }
	
	@Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void deleteEmployeeByIdAlreadyDeletedTest() throws Exception {
	    saveEmployee(1l,"Jonathan","E","De la Torre",LocalDate.of(1987, 04, 17),LocalDate.of(2007, 11, 03),false);
        this.mvc.perform(delete(EMPLOYEE_REQUEST_MAPPING+"/1").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status", is(409)))
        .andExpect(jsonPath("$.path", is("/employee/1")))
        .andExpect(jsonPath("$.message", startsWith("Employee with id = 1 has already been deleted")));
        employeeRepository.deleteAll();
    }
	
	@Test
    public void deleteEmployeeByIdUnauthenticatedTest() throws Exception {
        this.mvc.perform(delete(EMPLOYEE_REQUEST_MAPPING+"/1111").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized());
    }
	
}
