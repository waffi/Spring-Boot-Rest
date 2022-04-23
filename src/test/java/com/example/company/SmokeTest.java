package com.example.company;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.company.controller.EmployeeController;
import com.example.company.controller.HomeController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private HomeController homeController;
    @Autowired
    private EmployeeController employeeController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(homeController).isNotNull();
        assertThat(employeeController).isNotNull();
    }
}
