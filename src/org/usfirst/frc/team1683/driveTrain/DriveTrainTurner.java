package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Gyro;

/**
 * Turns robot a certain number of degrees
 */
public class DriveTrainTurner {
	private DriveTrain driveTrain;
	private Gyro gyro;
	private double speed;
	private double angle;
	private boolean done = false;
	private final double ANGLE_TOLERANCE = 1;

	/**
	 * Creates a DriveTrainTurner
	 * 
	 * @param driveTrain
	 *            The drive train
	 * @param angle
	 *            The angle, positive is counterclockwise, negative is clockwise.
	 *            Can be above 360 (does multiple rotations)
	 * @param speed
	 *            Speed between 0 and 1
	 */
	public DriveTrainTurner(DriveTrain driveTrain, double angle, double speed) {
		// positive angle = counter clockwise, negative = clockwise
		this.driveTrain = driveTrain;
		gyro = driveTrain.getGyro();
		gyro.reset();
		this.angle = angle;
		this.speed = speed;
		// If the angle is close to zero, no need to turn, we are already done
		done = Math.abs(angle) < ANGLE_TOLERANCE;
	}

	/**
	 * Turns in place as long as the heading is less than the angle (within
	 * ANGLE_TOLERANCE)
	 */
	public void run() {
		double heading = gyro.getAngle();
		if (!done && Math.abs(heading) <= Math.abs(angle) - ANGLE_TOLERANCE) {
			// If angle > 0, then it should turn counterclockwise so the "right"
			// parameter should be false
			driveTrain.turnInPlace(angle < 0, speed);
		} else {
			driveTrain.set(0);
			done = true;
		}
	}

	public double angleLeft() {
		return angle - gyro.getAngle();
	}

	/**
	 * 
	 * @return Whether it is done turning
	 */
	public boolean isDone() {
		return done;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAngle() {
		return angle;
	}
}
