package com.example.shiftclock.model;

import java.util.Date;
import java.util.UUID;

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
	
    public String getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id.toString();
    }
}
