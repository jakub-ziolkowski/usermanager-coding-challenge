package com.hsbc.usermanager;

import com.hsbc.usermanager.api.UserManagementRestApi;
import com.hsbc.usermanager.data.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserManagerApplicationTest {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private UserManagementRestApi api;

    @Test
    void contextLoads() {
        assertNotNull(repository);
        assertNotNull(api);
    }

}