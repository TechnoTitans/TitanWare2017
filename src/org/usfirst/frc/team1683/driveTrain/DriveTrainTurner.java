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
	/**
	 * Creates a DriveTrainTurner
	 * @param driveTrain -- the drive train
	 * @param angle -- The angle; should not be above 180 or below -180; positive indicates counter-clockwise
	 * @param speed -- Speed between 0 and 1 normally, if negative will take a longer route to angle (will twist the opposite way)
	 */
	public DriveTrainTurner(DriveTrain driveTrain, double angle, double speed) {
		// positive angle = counter clockwise, negative = clockwise
		this.driveTrain = driveTrain;
		gyro = driveTrain.getGyro();
		initialHeading = gyro.getAngle();
		angle %= 360;
		this.angle = angle;
		this.speed = speed;
	}
	
	/**
	 * Turns in place as long as the heading
	 * @return true if it is done turning
	 */
	public boolean run() {
		double heading = angleDiff(gyro.getAngle(), initialHeading);
		SmartDashboard.sendData("drive train turner heading", heading);
		if (!done && Math.abs(heading) <= Math.abs(angle)) {
			driveTrain.turnInPlace(angle < 0, speed);
			return false;
		} else {
			done = true;
			return true;
		}
	}
	/**
	 * 
	 * @return Whether it is done turning
	 */
	public boolean isDone() {
		return done;
	}
	private double angleDiff(double a, double b) {
		double diff = a - b;
		diff %= 360;
		if (diff < -180) diff += 360;
		if (diff > 180) diff -= 360;
		return diff;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
