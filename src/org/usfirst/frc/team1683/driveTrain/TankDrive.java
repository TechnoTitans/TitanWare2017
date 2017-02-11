package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.sensors.Gyro;

public class TankDrive implements DriveTrain {

	private MotorGroup left;
	private MotorGroup right;
	private Gyro gyro;

	// Will probably use to correct drift
	@SuppressWarnings("unused")
	private final double kp = 0.6;

	public TankDrive(MotorGroup left, MotorGroup right) {
		this.left = left;
		this.right = right;
	}

	public TankDrive(MotorGroup left, MotorGroup right, Gyro gyro) {
		this.left = left;
		this.right = right;
		this.gyro = gyro;
		// this.gyro.reset();
	}
	/**
	 * Sets the speed.
	 */
	@Override
	public void set(double speed) {
		left.set(speed);
		right.set(speed);
	}

	public void setLeft(double speed) {
		left.set(speed);
	}

	public void setRight(double speed) {
		right.set(speed);
	}

	/**
	 * Sets speed of each motor group
	 * 
	 * @param leftSpeed
	 *            -- speed of left motor group
	 * @param rightSpeed
	 *            -- speed of right motor group
	 */
	public void set(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	/**
	 * Turns in place
	 * 
	 * @param right
	 *            -- True if should turn right (clockwise), false if left
	 * @param speed
	 *            -- Speed
	 */
	public void turnInPlace(boolean right, double speed) {
		if (right) {
			set(speed, -speed);
		} else {
			set(-speed, speed);
		}
	}

	/**
	 * Stop the drive train.
	 */
	@Override
	public void stop() {
		left.enableBrakeMode(true);
		right.enableBrakeMode(true);
		left.stop();
		right.stop();
	}

	/**
	 * Stop without braking.
	 */
	@Override
	public void coast() {
		left.enableBrakeMode(false);
		right.enableBrakeMode(false);
		left.stop();
		right.stop();
	}

	/**
	 * Start driving.
	 */
	@Override
	public void driveMode(double leftSpeed, double rightSpeed) {
		left.enableBrakeMode(false);
		right.enableBrakeMode(false);

		// double lSpeed =
		// -DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		// double rSpeed =
		// -DriverStation.rightStick.getRawAxis(DriverStation.YAxis);

		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	@Override
	public Encoder getLeftEncoder() {
		return left.getEncoder();
	}

	@Override
	public Encoder getRightEncoder() {
		return right.getEncoder();
	}

	@Override
	public void resetEncoders() {
		left.getEncoder().reset();
		right.getEncoder().reset();
	}

	@Override
	public MotorGroup getLeftGroup() {
		return left;
	}

	@Override
	public MotorGroup getRightGroup() {
		return right;
	}

	// public void enableAntiDrift() {
	/// left.enableAntiDrift(antiDrift);
	// right.enableAntiDrift(antiDrift);
	// }

	// public void disableAntiDrift() {
	// left.disableAntiDrift();
	// right.disableAntiDrift();
	// }

	// public boolean isAntiDriftEnabled() {
	// return left.isAntiDriftEnabled() && right.isAntiDriftEnabled();
	// }

	public Gyro getGyro() {
		return gyro;
	}

	public boolean hasGyro() {
		return !(gyro == null);
	}
}
