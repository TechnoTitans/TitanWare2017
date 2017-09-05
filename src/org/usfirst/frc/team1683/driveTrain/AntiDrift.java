package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Gyro;

/*
 * Keeps robot moving in a line.
 * 
 * Realigns robot based on gyro
 */
public class AntiDrift {

	private double antidriftangle = 0;
	private final double KP = 0.04;
	private Gyro gyro;
	// 1 if right, -1 if left, 0 if no correction should be applied
	private int right;

	/**
	 * Creates an anti-drift object that uses the gyro to keep the robot straight
	 * Enable by calling motorGroup.enableAntiDrift(antiDrift)
	 * @param gyro The gyro object
	 * @param right 1 if it is right, -1 is left, 0 is no correction
	 */
	public AntiDrift(Gyro gyro, int right) {
		this.gyro = gyro;
		this.right = right;
	}

	/**
	 * 
	 * @param speed
	 *            The current speed of the motor
	 * @return The new speed of the motor that should be set to make the angle
	 *         of the gyro closer to zero
	 */
	public double antiDrift(double speed) {
		double error = antidriftangle - gyro.getAngle();
		double correction = KP * error;
		return limitSpeed(speed - correction * right);
	}

	public void reset() {
		gyro.reset();
		antidriftangle = gyro.getAngle(); // should be 0 but just in case
	}

	private static double limitSpeed(double speed) {
		if (speed > 1.0) {
			return 1.0;
		} else if (speed < -1.0) {
			return -1.0;
		} else {
			return speed;
		}
	}
}
