package com.hsbc.usermanager.api;

import com.hsbc.usermanager.api.requests.CreateEmployeeApiRequest;
import com.hsbc.usermanager.api.requests.UpdateEmployeeApiRequest;
import com.hsbc.usermanager.api.responses.CreateEmployeeApiResponse;
import com.hsbc.usermanager.api.responses.GetEmployeeApiResponse;
import com.hsbc.usermanager.data.entity.Employee;
import com.hsbc.usermanager.data.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserManagementRestApiTest {

    @Autowired
    private UserManagementRestApi api;

    @Autowired
    private EmployeeRepository repository;

    private CreateEmployeeApiRequest getCreateEmployeeApiRequest(String name, String surname, int grade, int salary) {
        CreateEmployeeApiRequest request = new CreateEmployeeApiRequest();
        request.setName(name);
        request.setSurname(surname);
        request.setGrade(grade);
        request.setSalary(salary);
        return request;
    }

    private UpdateEmployeeApiRequest getUpdateEmployeeApiRequest(String name, String surname, int grade, int salary) {
        UpdateEmployeeApiRequest request = new UpdateEmployeeApiRequest();
        request.setName(name);
        request.setSurname(surname);
        request.setGrade(grade);
        request.setSalary(salary);
        return request;
    }

    @Test
    void test_Employee_Data_Should_Be_Stored_And_Id_Assigned() {
        // when
        CreateEmployeeApiResponse response = api.createEmployee(getCreateEmployeeApiRequest("John", "Doe", 3, 12000));

        // then
        assertNotNull(response.getId());

        Employee employee = repository.findById(response.getId()).get();

        assertNotNull(employee);
        assertEquals(employee.getName(), "John");
        assertEquals(employee.getSurname(), "Doe");
        assertEquals(employee.getGrade(), 3);
        assertEquals(employee.getSalary(), 12000);
    }

    @Test
    void test_Employee_Data_Should_NOT_Be_Stored_Due_To_Malformed_Request_Payload_And_Exception_Should_Be_Thrown() {
        //when then
        Exception e = assertThrows(DataIntegrityViolationException.class, () ->
                api.createEmployee(getCreateEmployeeApiRequest(null, "Doe", 0, 12000))
        );
        // checking exception message
        String expectedMessage = "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));

    }

    @Test
    void test_Employee_Data_Should_Be_Updated() {
        // when
        CreateEmployeeApiResponse response = api.createEmployee(getCreateEmployeeApiRequest("John", "Doe", 3, 12000));
        assertNotNull(response.getId());

        api.updateEmployee(getUpdateEmployeeApiRequest("Johnny", "Does", 4, 15000), response.getId().toString());

        // then
        GetEmployeeApiResponse updatedEmployee = api.getEmployee(response.getId().toString());
        assertEquals(updatedEmployee.getName(), "Johnny");
        assertEquals(updatedEmployee.getSurname(), "Does");
        assertEquals(updatedEmployee.getGrade(), 4);
        assertEquals(updatedEmployee.getSalary(), 15000);
    }

    @Test
    void test_Employee_Should_Be_Deleted() {
        // when
        CreateEmployeeApiResponse response = api.createEmployee(getCreateEmployeeApiRequest("John", "Doe", 3, 12000));
        assertNotNull(response.getId());

        api.deleteEmployee(String.valueOf(response.getId()));

        Exception e = assertThrows(ResponseStatusException.class, () ->
                api.getEmployee(String.valueOf(response.getId()))
        );

        String expectedMessage = "404 NOT_FOUND \"Unable to find User with id:";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void test_Users_Should_Be_Found() {
        api.createEmployee(getCreateEmployeeApiRequest("John", "Doe", 3, 12000));
        api.createEmployee(getCreateEmployeeApiRequest("Kate", "Redmond", 2, 10000));
        api.createEmployee(getCreateEmployeeApiRequest("Bill", "Gates", 3, 5000));
        api.createEmployee(getCreateEmployeeApiRequest("Linus", "Torvalds", 4, 15000));
        api.createEmployee(getCreateEmployeeApiRequest("Mike", "Shepard", 0, 2000));

        // when
        List<GetEmployeeApiResponse> response = api.searchEmployees("name:John");
        //then

        GetEmployeeApiResponse john = response.get(0);
        assertEquals(john.getName(), "John");

        // when
        response = api.searchEmployees("grade>2");
        // then
        assertEquals(response.size(), 4);

        // when
        response = api.searchEmployees("salary>5000,name:Linus");
        GetEmployeeApiResponse linus = response.get(0);

        // then
        assertEquals(linus.getName(), "Linus");

        // when
        response = api.searchEmployees("grade>2, salary<10000");
        assertEquals(response.size(), 1);

    }
}
