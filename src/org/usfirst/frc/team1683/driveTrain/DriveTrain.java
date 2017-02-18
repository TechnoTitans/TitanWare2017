package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.sensors.Gyro;
public interface DriveTrain {

	/**
	 * Sets the speed.
	 */
	public void set(double speed);

	/**
	 * Sets speed of each motor group
	 * 
	 * @param leftSpeed speed of left motor group
	 * @param rightSpeed speed of right motor group
	 */
	public void set(double left, double right);

	/**
	 * Stop the drive train.
	 */
	public void stop();

	/**
	 * Stop without braking.
	 */
	public void coast();

	public void resetEncoders();

	public Encoder getLeftEncoder();

	public Encoder getRightEncoder();

	public MotorGroup getLeftGroup();

	public MotorGroup getRightGroup();

	/**
	 * Start driving.
	 */
	public void driveMode(double leftSpeed, double rightSpeed);

	public Gyro getGyro();

	/**
	 * Turns in place
	 * 
	 * @param right True if should turn right (clockwise), false if left
	 * @param speed Speed of motors
	 */
	public void turnInPlace(boolean right, double speed);
}
