package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

/**
 * 
 * @author Pran
 *
 */
public class DriveTrainTurner {
	private DriveTrain driveTrain;
	private double initialHeading;
	private Gyro gyro;
	private double speed;
	private double angle;
	private boolean done = false;
	private final double ANGLE_TOLERANCE = 2;
	/**
	 * Creates a DriveTrainTurner
	 * @param driveTrain -- the drive train
	 * @param angle -- The angle, if above 180 or below -180, will be adjusted to be in that range (will not do multiple revolutions); positive indicates counter-clockwise
	 * @param speed -- Speed between 0 and 1 normally, if negative will take a longer route to angle (will twist the opposite way)
	 */
	public DriveTrainTurner(DriveTrain driveTrain, double angle, double speed) {
		// positive angle = counter clockwise, negative = clockwise
		this.driveTrain = driveTrain;
		gyro = driveTrain.getGyro();
		gyro.reset();
		initialHeading = gyro.getAngle();
		angle = normalizeAngle(angle);
		this.angle = angle;
		this.speed = speed;
	}
	
	/**
	 * Takes an angle and returns the angle between -180 and 180 that is equivalent to it
	 * @param angle
	 * @return An equivalent angle between -180 and 180
	 */
	public double normalizeAngle(double angle) {
		angle %= 360;
		if (angle < -180) angle += 360;
		if (angle > 180) angle -= 360;
		return angle;
	}
	
	/**
	 * Turns in place as long as the heading is less than the angle (within ANGLE_TOLERANCE)
	 */
	public void run() {
		double heading = angleDiff(gyro.getAngle(), initialHeading);
		SmartDashboard.sendData("angle", angle);
		SmartDashboard.sendData("heading_turner", heading);
		SmartDashboard.sendData("drive train turner heading", Math.abs(heading - angle));
		if (!done && Math.abs(heading) <= Math.abs(angle) - ANGLE_TOLERANCE) {
			driveTrain.turnInPlace(angle < 0, speed);
		} else {
			done = true;
		}
	}
	public double angleLeft() {
		return Math.abs(angle) - Math.abs(angleDiff(gyro.getAngle(), initialHeading));
	}
	/**
	 * 
	 * @return Whether it is done turning
	 */
	public boolean isDone() {
		return done;
	}
	private double angleDiff(double a, double b) {
		return normalizeAngle(a - b);
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
