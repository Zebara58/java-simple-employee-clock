package com.example.shiftclock.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee {
	@Id
	private String id;
	private Date shiftStart;
	private Date shiftEnd;
	private Date breakStart;
	private Date breakEnd;
	private Date startLunch;
	private Date endLunch;
}
