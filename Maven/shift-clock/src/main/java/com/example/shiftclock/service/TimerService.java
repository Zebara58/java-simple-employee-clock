package com.example.shiftclock.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Service;

import com.example.shiftclock.model.ShiftTrack;

@Service
public class TimerService {
	private final Map<String, ShiftTrack> timers = new HashMap<>();
	
	public void start(String id) {
		Timer timer = new Timer();
		var timerTask = new TimerTask() {
			@Override
            public void run() {
                System.out.println("Shift ended for ID: " + id);
            }
		};
		timer.schedule(timerTask, 0);
		var shiftTrack = new ShiftTrack(timer, System.currentTimeMillis());
		
		timers.put(id, shiftTrack);
	}
	
	// Returns the elapsed time for the timer in Milliseconds
	public long stop(String id) {
		var shiftTrack = timers.get(id);
		long elapsedTime = 0;
		if (shiftTrack != null && shiftTrack.timer != null) {
			shiftTrack.timer.cancel();
			long endTime = System.currentTimeMillis();
		    elapsedTime = endTime - shiftTrack.startTime;
		}
		return elapsedTime;
	}
}
