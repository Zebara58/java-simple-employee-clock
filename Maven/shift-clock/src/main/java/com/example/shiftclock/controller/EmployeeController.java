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
		var lastShift = foundEmployee.getLastShift();
		if (lastShift == null || 
				(lastShift.breakStart != null && lastShift.breakEnd == null) || 
				(lastShift.lunchStart !=null && lastShift.lunchEnd == null)) {
			return ResponseEntity.badRequest().build();
		}
		lastShift = employeeService.stopShift(foundEmployee);
		return ResponseEntity.ok(lastShift);
	}
	
	@PostMapping("/start-lunch/{id}")
	public ResponseEntity<String> startLunch(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = foundEmployee.getLastShift();
		if (shiftNotStarted(lastShift)) {
			return ResponseEntity.ok("Shift not started");
		}
		if (lastShift.lunchEnd != null) {
			return ResponseEntity.ok("Lunch already ended");
		}
		if (lastShift.lunchStart != null) {
			return ResponseEntity.ok("Lunch already started");
		}
		employeeService.startLunch(foundEmployee);
		return ResponseEntity.ok("Lunch started");
	}
	
	@PostMapping("/stop-lunch/{id}")
	public ResponseEntity<Shift> stopLunch(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = foundEmployee.getLastShift();
		if (lastShift == null) {
			return ResponseEntity.badRequest().build();
		}
		lastShift = employeeService.stopLunch(foundEmployee);
		return ResponseEntity.ok(lastShift);
	}
	
	@PostMapping("/start-break/{id}")
	public ResponseEntity<String> startBreak(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = foundEmployee.getLastShift();
		if (shiftNotStarted(lastShift)) {
			return ResponseEntity.ok("Shift not started");
		}
		if (lastShift.breakEnd != null) {
			return ResponseEntity.ok("Break already ended");
		}
		if (lastShift.breakStart != null) {
			return ResponseEntity.ok("Break already started");
		}
		employeeService.startBreak(foundEmployee);
		return ResponseEntity.ok("Break started");
	}
	
	@PostMapping("/stop-break/{id}")
	public ResponseEntity<Shift> stopBreak(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.notFound().build();
		}
		var foundEmployee = employeeService.getEmployee(id.toString());
		if (foundEmployee == null) {
			return ResponseEntity.notFound().build();
		}
		var lastShift = foundEmployee.getLastShift();
		if (lastShift == null) {
			return ResponseEntity.badRequest().build();
		}
		lastShift = employeeService.stopBreak(foundEmployee);
		return ResponseEntity.ok(lastShift);
	}
	
	private boolean shiftNotStarted(Shift lastShift) {
		return lastShift == null || lastShift.shiftStart == null || lastShift.shiftEnd != null;
	}
}
