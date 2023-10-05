package com.example.shiftclock.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shiftclock.model.Employee;
import com.example.shiftclock.model.EmployeeFactory;
import com.example.shiftclock.model.EmployeeRequest;
import com.example.shiftclock.model.Shift;
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
	
	@PostMapping("/start-shift/{id}")
	public ResponseEntity<String> startShift(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = foundEmployee.getLastShift();
		if (lastShift != null && lastShift.shiftStart != null && lastShift.shiftEnd == null) {
			return ResponseEntity.ok("Shift already started");
		}
		employeeService.startShift(foundEmployee);
		return ResponseEntity.ok("Shift started");
	}
	
	@PostMapping("/stop-shift/{id}")
	public ResponseEntity<Shift> stopShift(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = employeeService.stopShift(foundEmployee);
		return ResponseEntity.ok(lastShift);
	}
}
