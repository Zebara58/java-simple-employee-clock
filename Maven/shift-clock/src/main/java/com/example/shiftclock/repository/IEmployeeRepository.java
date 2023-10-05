package com.example.shiftclock.repository;

import com.example.shiftclock.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEmployeeRepository extends MongoRepository<Employee, String> {
	boolean existsById(String id);
}