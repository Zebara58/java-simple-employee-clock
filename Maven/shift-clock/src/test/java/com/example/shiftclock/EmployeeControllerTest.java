package com.example.shiftclock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shiftclock.controller.EmployeeController;
import com.example.shiftclock.model.Employee;
import com.example.shiftclock.model.EmployeeRequest;
import com.example.shiftclock.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	public void testAddEmployee() throws Exception {
		mockMvc.perform(post("/employees/add-employee").content("{}").contentType("application/json"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isString());
	}
	
	@Test
	public void testStartShiftWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/start-shift/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartShiftWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/start-shift/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartShiftWhenShiftAlreadyStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-shift/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartShiftWhenShiftNotStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-shift/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStopShiftWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/stop-shift/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopShiftWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/stop-shift/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopShiftWhenShiftStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.stopShift(10);
		var shift = employee.getLastShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		when(employeeService.stopShift(employee)).thenReturn(shift);
		mockMvc.perform(post("/employees/stop-shift/"+employeeId).contentType("application/json"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.shiftStart").isString())
        .andExpect(jsonPath("$.shiftEnd").isString());
	}
}
