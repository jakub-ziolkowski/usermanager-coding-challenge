package com.hsbc.usermanager.api.responses;

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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getSalary() {
        return salary;
    }
}
