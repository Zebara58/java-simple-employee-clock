package com.example.shiftclock;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.example.shiftclock.model.EmployeeFactory;
import com.example.shiftclock.model.EmployeeRequest;

public class EmployeeFactoryTest {
	@Test
	public void testCreateEmployeeFromRequestWhenNullId() throws Exception {
		var request = new EmployeeRequest();
		var employee = EmployeeFactory.createEmployeeFromRequest(request);
		assertEquals(36, employee.getId().length());
	}
	
	@Test
	public void testCreateEmployeeFromRequestWhenIdInRequest() throws Exception {
		var request = new EmployeeRequest();
		request.Id = UUID.randomUUID();
		var employee = EmployeeFactory.createEmployeeFromRequest(request);
		assertEquals(request.Id.toString(), employee.getId());
	}
}
