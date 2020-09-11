package com.hsbc.usermanager.api.responses;

import com.hsbc.usermanager.data.entity.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UpdateEmployeeApiResponse {

    @ApiModelProperty(position = 1, notes = "Employee unique id")
    private Integer id;

    @ApiModelProperty(position = 2, notes = "Employee first name")
    private String name;

    @ApiModelProperty(position = 3, notes = "Employee last name")
    private String surname;

    @ApiModelProperty(position = 4, notes = "Employee grade")
    private Integer grade;

    @ApiModelProperty(position = 5, notes = "Employee salary")
    private Integer salary;

    public UpdateEmployeeApiResponse() {
    }

    public UpdateEmployeeApiResponse(Employee e) {
        super();
        setId(e.getId());
        setName(e.getName());
        setSurname(e.getSurname());
        setGrade(e.getGrade());
        setSalary(e.getSalary());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
