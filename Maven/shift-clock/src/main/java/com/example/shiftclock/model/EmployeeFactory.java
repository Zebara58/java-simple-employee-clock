package com.example.shiftclock.model;

import java.util.UUID;

public class EmployeeFactory {
	public static Employee createEmployeeFromRequest(EmployeeRequest request) {
		var employee = new Employee();
		if (request.Id == null) {
			employee.setId(UUID.randomUUID());
		}
		else {
			employee.setId(request.Id);
		}
		return employee;
	}
}
