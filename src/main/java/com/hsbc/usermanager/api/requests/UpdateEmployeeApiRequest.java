package com.hsbc.usermanager.api.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UpdateEmployeeApiRequest {

    @ApiModelProperty(position = 1, notes = "Employee first name", required = true, example = "First name")
    private String name;

    @ApiModelProperty(position = 2, notes = "Employee last name", required = true, example = "Last name")
    private String surname;

    @ApiModelProperty(position = 3, notes = "Employee grade", required = true, example = "1")
    private Integer grade;

    @ApiModelProperty(position = 4, notes = "Employee salary", required = true, example = "1337")
    private Integer salary;

    public UpdateEmployeeApiRequest() {
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
