package com.example.shiftclock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shiftclock.model.Employee;
import com.example.shiftclock.model.Shift;
import com.example.shiftclock.repository.IEmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private IEmployeeRepository employeeRepository;
	
	@Autowired
	private TimerService timerService;
	
	public Employee addEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	public boolean doesEmployeeExist(String id) {
        return employeeRepository.existsById(id);
    }
	
	public Employee getEmployee(String id) {
		return employeeRepository.findById(id).orElse(null);
	}
	
	public void startShift(Employee employee) {
		employee.startShift();
		timerService.startShift(employee.getId());
		employeeRepository.save(employee);
	}
	
	public Shift stopShift(Employee employee) {
		var lastShift = employee.getLastShift();
		if (lastShift.shiftStart == null || lastShift.shiftEnd != null) {
			return lastShift;
		}
		var elapsedTime = timerService.stopShift(employee.getId());
		employee.stopShift(elapsedTime);
		employeeRepository.save(employee);
		return employee.getLastShift();
	}
}
