package com.hsbc.usermanager.api;

import com.hsbc.usermanager.api.requests.CreateEmployeeApiRequest;
import com.hsbc.usermanager.api.requests.UpdateEmployeeApiRequest;
import com.hsbc.usermanager.api.responses.CreateEmployeeApiResponse;
import com.hsbc.usermanager.api.responses.DeleteEmployeeApiResponse;
import com.hsbc.usermanager.api.responses.GetEmployeeApiResponse;
import com.hsbc.usermanager.api.responses.UpdateEmployeeApiResponse;
import com.hsbc.usermanager.data.entity.Employee;
import com.hsbc.usermanager.data.repository.EmployeeRepository;
import com.hsbc.usermanager.data.EmployeeSpecificationBuilder;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Api
public class UserManagementRestApi {

    @Autowired
    private EmployeeRepository employeeRepository;

    @ApiOperation(
            value = "Creates a new Employee",
            notes = "Unique <code>id</code> of newly created Employee will be automatically assigned",
            response = CreateEmployeeApiResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new Employee"),
            @ApiResponse(code = 400, message = "There was a problem creating a Employee - please follow exception status for details")
    })
    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    CreateEmployeeApiResponse createEmployee(
            @ApiParam(value = "Employee data to be stored", required = true)
            @RequestBody CreateEmployeeApiRequest createEmployeeApiRequest
    ) {

        Employee e = new Employee();
        e.setGrade(createEmployeeApiRequest.getGrade());
        e.setName(createEmployeeApiRequest.getName());
        e.setSurname(createEmployeeApiRequest.getSurname());
        e.setSalary(createEmployeeApiRequest.getSalary());
        e.setDeleted(false);

        Employee saved = employeeRepository.save(e);
        return new CreateEmployeeApiResponse(saved);
    }

    @ApiOperation(
            value = "Returns data of Employee of given id",
            notes = "When Employee is not found, an exception will be thrown",
            response = GetEmployeeApiResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched Employee data"),
            @ApiResponse(code = 404, message = "Employee with given id was not found"),
            @ApiResponse(code = 400, message = "There was a problem fetching an Employee data - please follow exception status for details")
    })
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    GetEmployeeApiResponse getEmployee(
            @ApiParam(value = "Employee id", required = true)
            @PathVariable String id
    ) {

        Employee employee = employeeRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id)));

        if (employee.getDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id));
        }

        return new GetEmployeeApiResponse(employee);
    }

    @ApiOperation(
            value = "Modifies data of Employee of given id",
            notes = "When Employee is not found, an exception will be thrown",
            response = GetEmployeeApiResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified Employee data"),
            @ApiResponse(code = 404, message = "Employee with given id was not found"),
            @ApiResponse(code = 400, message = "There was a problem modifying an Employee data - please follow exception status for details")
    })
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    UpdateEmployeeApiResponse updateEmployee(
            @ApiParam(value = "Employee data", required = true)
            @RequestBody UpdateEmployeeApiRequest updateEmployeeApiRequest,
            @ApiParam(value = "Employee id", required = true)
            @PathVariable String id
    ) {

        Employee employee = employeeRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id)));

        if (employee.getDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id));
        }

        employee.setName(updateEmployeeApiRequest.getName());
        employee.setSurname(updateEmployeeApiRequest.getSurname());
        employee.setGrade(updateEmployeeApiRequest.getGrade());
        employee.setSalary(updateEmployeeApiRequest.getSalary());

        employeeRepository.save(employee);
        return new UpdateEmployeeApiResponse(employee);
    }

    @ApiOperation(
            value = "Deletes data of Employee of given id",
            notes = "When Employee is not found, an exception will be thrown",
            response = DeleteEmployeeApiResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted Employee data"),
            @ApiResponse(code = 404, message = "Employee with given id was not found"),
            @ApiResponse(code = 400, message = "There was a problem deleting an Employee data - please follow exception status for details")
    })
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    DeleteEmployeeApiResponse deleteEmployee(
            @ApiParam(value = "Employee id", required = true)
            @PathVariable String id
    ) {
        Employee employee = employeeRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id)));

        if (employee.getDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find User with id: ".concat(id));
        }

        employee.setDeleted(true);
        employeeRepository.save(employee);
        return new DeleteEmployeeApiResponse(employee);
    }

    @ApiOperation(
            value = "Search data of Employee using given query (any number of arguments)",
            notes = "You can use multiple chain of [column][operator][value] queries, separated by <code>,</code><br/><br/>" +
                    "Valid [operator] values:<br/>" +
                    "<code>&lt;</code> - column is less than <code>value</code><br/>" +
                    "<code>&gt;</code> - column is greater than <code>value</code><br/>" +
                    "<code>:</code> - column contains <code>value</code><br/><br/>" +
                    "Examples:<br/>" +
                    "<code>name:test</code> - get all records containing <code>test</code> in column <code>name</code><br/>" +
                    "<code>salary&gt;100</code> - get all records where value of <code>salary</code> column is greater than <code>100</code><br/>" +
                    "<code>salary&lt;100</code> - get all records where value of <code>salary</code> column is less than <code>100</code><br/><br/>" +
                    "You can chain these queries, example:<br/>" +
                    "<code>name:test,salary&lt;100</code> - get all records containing <code>test</code> in column <code>name</code> and value of <code>salary</code> column is less than <code>100</code><br/>",
            response = GetEmployeeApiResponse.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned search results"),
            @ApiResponse(code = 400, message = "There was a problem searching an Employees data - please follow exception status for details")
    })
    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<GetEmployeeApiResponse> searchEmployees(
            @ApiParam(value = "Query", required = true)
            @PathVariable String query
    ) {
        List<GetEmployeeApiResponse> getEmployeeApiResponses = new ArrayList<>();

        EmployeeSpecificationBuilder builder = new EmployeeSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
        //Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        builder.with("deleted", ":", false);

        Specification<Employee> spec = builder.build();
        List<Employee> results = employeeRepository.findAll(spec);
        results.forEach(employee -> getEmployeeApiResponses.add(new GetEmployeeApiResponse(employee)));

        return getEmployeeApiResponses;
    }
}
