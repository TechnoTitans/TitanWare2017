package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.simulation.SimIterativeRobot;

public class Gyro {
	private double simHeading = 0;
	
	public Gyro(int channel) {
	}

	public double getAngle() {
		SimIterativeRobot robot = SimIterativeRobot.get();
		return robot.getDifferenceVector().getAngle();
	}

	public void reset() {
		simHeading = 0;
	}
}
