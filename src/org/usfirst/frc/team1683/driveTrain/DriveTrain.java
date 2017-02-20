package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.sensors.Gyro;
public interface DriveTrain {

	public void set(double speed);

	/**
	 * @param leftSpeed speed of left motor group
	 * @param rightSpeed speed of right motor group
	 */
	public void set(double left, double right);

	public void stop();
	
	public void coast();

	public void resetEncoders();

	public Encoder getLeftEncoder();

	public Encoder getRightEncoder();

	public MotorGroup getLeftGroup();

	public MotorGroup getRightGroup();

	public void driveMode(double leftSpeed, double rightSpeed);

	public Gyro getGyro();

	/**
	 * @param right True if should turn right (clockwise), false if left
	 * @param speed Speed of motors
	 */
	public void turnInPlace(boolean right, double speed);
}
