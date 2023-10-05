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
    
    public void startLunch() {
    	var lastShift = getLastShift();
    	if (validateShiftIsActiveAndNotOver(lastShift) || lastShift.lunchStart !=null) {
    		return;
    	}
    	lastShift.lunchStart = new Date();
    }
    
    public void stopLunch(long elapsedTime) {
    	var lastShift = getLastShift();
    	if (lastShift == null || lastShift.lunchStart == null) {
    		return;
    	}
    	lastShift.lunchEnd = new Date(lastShift.lunchStart.getTime() + elapsedTime);
    }
    
    public void startBreak() {
    	var lastShift = getLastShift();
    	if (validateShiftIsActiveAndNotOver(lastShift) || lastShift.breakStart !=null) {
    		return;
    	}
    	lastShift.breakStart = new Date();
    }
    
    public void stopBreak(long elapsedTime) {
    	var lastShift = getLastShift();
    	if (lastShift == null || lastShift.breakStart == null) {
    		return;
    	}
    	lastShift.breakEnd = new Date(lastShift.breakStart.getTime() + elapsedTime);
    }
    
    private boolean validateShiftIsActiveAndNotOver(Shift lastShift) {
    	return lastShift.shiftStart == null || lastShift.shiftEnd != null;
    }
}
