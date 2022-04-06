package com.example.payroll;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.* ;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {

  private final EmployeeRepository repository;
  
  private final EmployeeModelAssembler assembler;

  EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {

    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/employees")
  ResponseEntity<?> getAll() {

      List<EntityModel<Employee>> employees = repository.findAll().stream()		
          .map(assembler::toModel) // .map(employee -> assembler.toModel(employee))
          .collect(Collectors.toList());
      
      CollectionModel<EntityModel<Employee>> entityModel = 
    		  CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());

	  return ResponseEntity.ok(entityModel); // Status: 200 OK 
  }

  @GetMapping("/employees/{id}")
  ResponseEntity<?> getById(@PathVariable Long id) {

	  Employee employee = repository.findById(id) //
	      .orElseThrow(() -> new EmployeeNotFoundException(id));

	  EntityModel<Employee> entityModel = assembler.toModel(employee);
	
	  return ResponseEntity.ok(entityModel); // Status: 200 OK 
  }
  
  @PostMapping("/employees") 
  ResponseEntity<?> create(@RequestBody Employee newEmployee) {
	  EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

	  return ResponseEntity //
	      .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Status: 201 Created 
	      .body(entityModel);
  }

  @PutMapping("/employees/{id}")
  ResponseEntity<?> update(@RequestBody Employee newEmployee, @PathVariable Long id) {
    
	  Employee updatedEmployee = repository.findById(id) //
		      .map(employee -> {
		        employee.setName(newEmployee.getName());
		        employee.setRole(newEmployee.getRole());
		        return repository.save(employee);
		      }) //
		      .orElseThrow(() -> new EmployeeNotFoundException(id));

	  EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

	  return ResponseEntity //
	      .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // Status: 201 Created 
	      .body(entityModel);
  }

  @DeleteMapping("/employees/{id}")
  ResponseEntity<?> delete(@PathVariable Long id) {

	  Employee employee = repository.findById(id) //
	      .orElseThrow(() -> new EmployeeNotFoundException(id));
	  
      repository.deleteById(id);
    
      return ResponseEntity.ok().build();
  }
}