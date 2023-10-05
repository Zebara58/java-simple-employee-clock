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
	
	@Autowired
	private BreakTimerService breakTimerService;
	
	@Autowired
	private LunchTimerService lunchTimerService;
	
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
		timerService.start(employee.getId());
		employeeRepository.save(employee);
	}
	
	public Shift stopShift(Employee employee) {
		var lastShift = employee.getLastShift();
		if (lastShift.shiftStart == null || lastShift.shiftEnd != null) {
			return lastShift;
		}
		var elapsedTime = timerService.stop(employee.getId());
		employee.stopShift(elapsedTime);
		employeeRepository.save(employee);
		return employee.getLastShift();
	}
	
	public void startLunch(Employee employee) {
		employee.startLunch();
		lunchTimerService.start(employee.getId());
		employeeRepository.save(employee);
	}
	
	public Shift stopLunch(Employee employee) {
		var lastShift = employee.getLastShift();
		if (lastShift.lunchStart == null || lastShift.lunchEnd != null) {
			return lastShift;
		}
		var elapsedTime = lunchTimerService.stop(employee.getId());
		employee.stopLunch(elapsedTime);
		employeeRepository.save(employee);
		return employee.getLastShift();
	}
	
	public void startBreak(Employee employee) {
		employee.startBreak();
		breakTimerService.start(employee.getId());
		employeeRepository.save(employee);
	}
	
	public Shift stopBreak(Employee employee) {
		var lastShift = employee.getLastShift();
		if (lastShift.breakStart == null || lastShift.breakEnd != null) {
			return lastShift;
		}
		var elapsedTime = breakTimerService.stop(employee.getId());
		employee.stopBreak(elapsedTime);
		employeeRepository.save(employee);
		return employee.getLastShift();
	}
}
