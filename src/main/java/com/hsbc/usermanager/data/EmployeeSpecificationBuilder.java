package com.hsbc.usermanager.data;

import com.hsbc.usermanager.data.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeSpecificationBuilder {

    private final List<SearchCriteria> params;

    public EmployeeSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public EmployeeSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Employee> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(EmployeeSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}