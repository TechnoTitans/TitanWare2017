package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.simulation.SimIterativeRobot;

public class Gyro {
	private double simHeading = 0;
	
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
	}

	public double getAngle() {
		SimIterativeRobot robot = SimIterativeRobot.get();
		double angle = Math.toDegrees(robot.getDifferenceVector().getAngle());
		simHeading += angleDiff(angle, simHeading);
		return simHeading;
	}
	
	
	public void reset() {
		simHeading = 0;
	}
}
