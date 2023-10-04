package com.example.shiftclock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shiftclock.repository.IEmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
	@Autowired
	private IEmployeeRepository employeeRepository;
}
