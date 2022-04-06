package com.example.company.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.company.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}