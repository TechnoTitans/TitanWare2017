package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;
public interface DriveTrain {

	void moveDistance(double distance) throws EncoderNotFoundException;

	public void moveDistance(double distance, double speed) throws EncoderNotFoundException;

	public void turn(double degrees);

	public void set(double speed);

	public void stop();

	public void coast();

	public void resetEncoders();

	public Encoder getLeftEncoder();

	public Encoder getRightEncoder();

	public MotorGroup getLeftGroup();

	public MotorGroup getRightGroup();

	public void driveMode(double leftSpeed, double rightSpeed);
}
