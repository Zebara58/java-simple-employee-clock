package com.example.shiftclock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shiftclock.controller.EmployeeController;
import com.example.shiftclock.model.Employee;
import com.example.shiftclock.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	public void testAddEmployee() throws Exception {
		mockMvc.perform(post("/employees/add-employee").content("{}").contentType("application/json"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isString());
	}
	
	@Test
	public void testAddEmployeeAlreadyExists() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.setId(employeeId);
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/add-employee").content("{\"Id\": \""+employeeId.toString()+"\"}").contentType("application/json"))
		.andExpect(status().isBadRequest());
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
	public void testGetEmployeeLastShiftWhenNoId() throws Exception {
		mockMvc.perform(get("/employees/get-employee-last-shift/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetEmployeeLastShiftWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(get("/employees/get-employee-last-shift/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetEmployeeLastShiftWhenLastShiftNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.setId(employeeId);
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(get("/employees/get-employee-last-shift/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetEmployeeLastShiftWhenEmployeeFound() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.setId(employeeId);
		employee.startShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(get("/employees/get-employee-last-shift/"+employeeId).contentType("application/json")).andExpect(status().isOk()).andExpect(jsonPath("$.shiftStart").isString());
	}
	
	@Test
	public void testGetEmployeeAllShiftsWhenNoId() throws Exception {
		mockMvc.perform(get("/employees/get-employee-all-shifts/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetEmployeeAllShiftsWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(get("/employees/get-employee-all-shifts/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetEmployeeAllShiftsWhenEmployeeFound() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.setId(employeeId);
		employee.startShift();
		employee.stopShift(100);
		employee.startShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(get("/employees/get-employee-all-shifts/"+employeeId).contentType("application/json")).andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].shiftStart").isString())
		.andExpect(jsonPath("$[0].shiftEnd").isString())
		.andExpect(jsonPath("$[1].shiftStart").isString());
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
	public void testStopShiftWhenShiftMissing() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/stop-shift/"+employeeId).contentType("application/json"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testStopShiftWhenShiftHasLunchNotFinished() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startLunch();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/stop-shift/"+employeeId).contentType("application/json"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testStopShiftWhenShiftHasBreakNotFinished() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startBreak();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/stop-shift/"+employeeId).contentType("application/json"))
		.andExpect(status().isBadRequest());
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
	
	@Test
	public void testStopLunchWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/stop-lunch/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopLunchWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/stop-lunch/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopLunchWhenShiftMissing() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/stop-lunch/"+employeeId).contentType("application/json"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testStopLunchWhenShiftStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startLunch();
		employee.stopLunch(10);
		var shift = employee.getLastShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		when(employeeService.stopLunch(employee)).thenReturn(shift);
		mockMvc.perform(post("/employees/stop-lunch/"+employeeId).contentType("application/json"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.shiftStart").isString())
        .andExpect(jsonPath("$.lunchStart").isString())
        .andExpect(jsonPath("$.lunchEnd").isString());
	}
	
	@Test
	public void testStopBreakWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/stop-break/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopBreakWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/stop-break/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStopBreakWhenShiftMissing() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/stop-break/"+employeeId).contentType("application/json"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testStopBreakWhenShiftStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startBreak();
		employee.stopBreak(10);
		var shift = employee.getLastShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		when(employeeService.stopBreak(employee)).thenReturn(shift);
		mockMvc.perform(post("/employees/stop-break/"+employeeId).contentType("application/json"))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.shiftStart").isString())
        .andExpect(jsonPath("$.breakStart").isString())
        .andExpect(jsonPath("$.breakEnd").isString());
	}
	
	@Test
	public void testStartLunchWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/start-lunch/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartBreakWhenNoId() throws Exception {
		mockMvc.perform(post("/employees/start-break/")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartLunchWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/start-lunch/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartBreakWhenEmployeeNotFound() throws Exception {
		var employeeId = UUID.randomUUID();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(null);
		mockMvc.perform(post("/employees/start-break/"+employeeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testStartLunchWhenShiftAlreadyStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-lunch/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartBreakWhenShiftAlreadyStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-break/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartLunchWhenLunchAlreadyStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startLunch();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-lunch/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartBreakWhenBreakAlreadyStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startBreak();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-break/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartLunchWhenLunchAlreadyEnded() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startLunch();
		employee.stopLunch(100);
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-lunch/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartBreakWhenBreakAlreadyEnded() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		employee.startShift();
		employee.startBreak();
		employee.stopBreak(100);
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-break/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartLunchWhenShiftNotStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-lunch/"+employeeId)).andExpect(status().isOk());
	}
	
	@Test
	public void testStartBreakWhenShiftNotStarted() throws Exception {
		var employeeId = UUID.randomUUID();
		var employee = new Employee();
		when(employeeService.getEmployee(employeeId.toString())).thenReturn(employee);
		mockMvc.perform(post("/employees/start-break/"+employeeId)).andExpect(status().isOk());
	}
}
