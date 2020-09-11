package com.hsbc.usermanager.api.responses;

import com.hsbc.usermanager.data.entity.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeleteEmployeeApiResponse {

    @ApiModelProperty(position = 1, notes = "Employee unique id")
    private Integer id;
    @ApiModelProperty(position = 2, notes = "Confirmation of deletion")
    private Boolean deleted;

    public DeleteEmployeeApiResponse() {
    }

    public DeleteEmployeeApiResponse(Employee e) {
        setId(e.getId());
        setDeleted(e.getDeleted());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
