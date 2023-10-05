package com.example.shiftclock.model;

import java.util.Timer;

public class ShiftTrack {
	public Timer timer;
	public long startTime;
	
	public ShiftTrack(Timer timer, long startTime) {
		this.timer = timer;
		this.startTime = startTime;
	}
}
