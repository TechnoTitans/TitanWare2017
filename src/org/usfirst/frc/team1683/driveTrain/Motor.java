package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;

import edu.wpi.first.wpilibj.PIDOutput;

public interface Motor extends PIDOutput {

	public static final double MAX_SPEED = 1.0;
	public static final double MID_SPEED = 0.5;
	public static final double LOW_SPEED = 0.25;

	public void moveDistance(double distance, Boolean left) throws EncoderNotFoundException;

	public void moveDistance(double distance, double speed, Boolean left) throws EncoderNotFoundException;

	// public void moveDegrees();
	public void set(double speed);

	public double get();

	public void stop();

	public boolean hasEncoder();

	public Encoder getEncoder();

	// public void setBrakeMode(boolean enable);

	public int getChannel();

	public boolean isReversed();
}
