package org.usfirst.frc.team1683.sensors;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1683.simulation.SimIterativeRobot;

public class Gyro {
	private double prevHeading = 0;
	private double simHeading = 0;
	private double rate = 0;
	
	private static Map<Integer, Gyro> gyros = new HashMap<>();
	
	private double angleDiff(double a, double b) {
		double diff = a - b;
		diff %= 360;
		if (diff > 180)
			diff -= 360;
		else if (diff < -180)
			diff += 360;
		return diff;
	}
	
	public Gyro(int channel) {
		if (gyros.containsKey(channel)) {
			throw new IllegalAccessError("Gyro on channel " + channel + " already initialized");
		}
		gyros.put(channel, this);
	}

	public double getAngle() {
		return simHeading;
	}
	
	public void update(SimIterativeRobot robot) {
		double angle = Math.toDegrees(-robot.getDifferenceVector().getAngle());
		rate = angleDiff(angle, prevHeading);
		prevHeading = angle;
		simHeading += rate;
	}
	
	public double getRate() {
		return rate * SimIterativeRobot.FRAMES_PER_SECOND;
	}
	
	public void reset() {
		simHeading = 0;
	}
	
	public static Iterable<Gyro> getGyros() {
		return gyros.values();
	}
}
