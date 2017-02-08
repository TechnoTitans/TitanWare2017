package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.sensors.Gyro;

public class TankDrive implements DriveTrain {

	private MotorGroup left;
	private MotorGroup right;
	private Gyro gyro;
	private Thread thread;

	// Will probably use to correct drift
	@SuppressWarnings("unused")
	private final double kp = 0.6;

	private class RobotTurner implements Runnable {

		private double speed;
		private double angle;

		public RobotTurner(double angle, double speed) {
			this.angle = angle;
			if (angle < 0) {
				this.speed = -speed;
			} else {
				this.speed = speed;
			}
		}

		@Override
		public void run() {
			double initialHeading = gyro.getAngle();

			while (Math.abs(gyro.getAngle() - initialHeading) < Math.abs(angle)) {
				// TODO: make sure these directions are right
				SmartDashboard.sendData("Gyro Angle2", gyro.getAngle());
				left.set(-speed);
				right.set(speed);
			}
			left.stop();
			right.stop();
		}
	}

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
	 * @param distance
	 *            Distance to move in inches at mid speed
	 * @throws EncoderNotFoundException
	 *             Encoder not found.
	 */
	@Override
	public void moveDistance(double distance) throws EncoderNotFoundException {
		moveDistance(distance, Motor.MID_SPEED);
	}

	/**
	 * @param distance
	 *            Distance to move in inches.
	 * @param speed
	 *            Speed from 0 to 1.
	 * @throws EncoderNotFoundException
	 *             Encoder not found.
	 */
	@Override
	public void moveDistance(double distance, double speed) throws EncoderNotFoundException {
		//gyro.reset(); TODO
		left.moveDistance(distance, speed);
		right.moveDistance(distance, speed);
	}

	public boolean hasMoveDistanceFinished() {
		return left.hasMoveDistanceFinished() && right.hasMoveDistanceFinished();
	}
	/**
	 * @param degrees
	 *            How much to turn the robot.
	 */
	@Override
	public void turn(double degrees) throws GyroNotFoundException {
		turn(degrees, Motor.LOW_SPEED);
	}

	public void turn(double degrees, double speed) throws GyroNotFoundException {
		if (hasGyro()) {
			if (thread == null || thread.getState().equals(Thread.State.TERMINATED)) {
				thread = new Thread(new RobotTurner(degrees, speed));
			}
			if (thread.getState().equals(Thread.State.NEW)) {
				thread.start();
			}
		} else {
			throw new GyroNotFoundException();
		}
	}

	/**
	 * Sets the speed.
	 */
	@Override
	public void set(double speed) {
		left.set(speed);
		right.set(speed);
	};

	/**
	 * Sets speed of each motor group
	 * @param leftSpeed -- speed of left motor group
	 * @param rightSpeed -- speed of right motor group
	 */
	public void set(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}
	
	/**
	 * Turns in place
	 * @param right -- True if should turn right, false if left
	 * @param speed -- Speed
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

		//double lSpeed = -DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		//double rSpeed = -DriverStation.rightStick.getRawAxis(DriverStation.YAxis);

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
