package com.example.shiftclock.service;

import java.util.Date;

import com.example.shiftclock.model.Shift;

public class TimerService {
	public static long stopShift(Shift shift) {
		if (shift == null) return 0;
		return calculateElapsedTime(shift.shiftStart, shift.shiftEnd);
	}
	
	public static long stopLunch(Shift shift) {
		if (shift == null) return 0;
		return calculateElapsedTime(shift.lunchStart, shift.lunchEnd);
	}
	
	public static long stopBreak(Shift shift) {
		if (shift == null) return 0;
		return calculateElapsedTime(shift.breakStart, shift.breakEnd);
	}
	
	private static long calculateElapsedTime(Date start, Date end) {
		long elapsedTime = 0;
		if (start != null && end == null) {;
			long endTime = System.currentTimeMillis();
		    elapsedTime = endTime - start.getTime();
		}
		return elapsedTime;
	}
}
