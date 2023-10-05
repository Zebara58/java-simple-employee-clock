package com.example.shiftclock.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee {
	@Id
	private String id;
	private List<Shift> shifts;
	
	public Employee() {
		this.shifts = new ArrayList<>();
	}
	
    public String getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id.toString();
    }
    
    public void startShift() {
    	var newShift = new Shift();
    	newShift.shiftStart = new Date();
    	shifts.add(newShift);
    }
    
    public void stopShift(long elapsedTime) {
    	var lastShift = getLastShift();
    	if (lastShift == null) return;
    	lastShift.shiftEnd = new Date(lastShift.shiftStart.getTime() + elapsedTime);
    }
    
    public Shift getLastShift() {
    	if (shifts.size() == 0) return null;
    	return shifts.get(shifts.size()-1);
    }
}
