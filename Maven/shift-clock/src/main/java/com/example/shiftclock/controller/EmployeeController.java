package com.example.shiftclock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shiftclock.model.Employee;
import com.example.shiftclock.model.EmployeeFactory;
import com.example.shiftclock.model.EmployeeRequest;
import com.example.shiftclock.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/add-employee")
	public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeRequest request) {
		var employee = EmployeeFactory.createEmployeeFromRequest(request);
		employeeService.addEmployee(employee);
		return ResponseEntity.ok(employee);
	}
}
