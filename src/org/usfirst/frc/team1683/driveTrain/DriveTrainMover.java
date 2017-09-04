package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1683.motor.Motor;
import org.usfirst.frc.team1683.motor.MotorGroup;
import org.usfirst.frc.team1683.motor.MotorMover;

/**
 * Mover for robot to move for a set distance in a line
 */
public class DriveTrainMover {
	private List<MotorMover> motorMovers = new ArrayList<MotorMover>();
	private double speed, distance;

	/**
	 * Creates a drive train mover, which moves every motor in a certain distance at a certain speed with anti-drift
	 * @param driveTrain
	 *            The drive train
	 * @param distance
	 *            The distance to move (can be negative for backward)
	 * @param speed
	 *            The speed (between 0 and 1)
	 */
	public DriveTrainMover(DriveTrain driveTrain, double distance, double speed) {
		this.distance = distance;
		this.speed = speed;
		MotorGroup left = driveTrain.getLeftGroup(), right = driveTrain.getRightGroup();
		addMotorGroup(left);
		addMotorGroup(right);
	}
	
	/**
	 * Creates a drive train mover that handles left and right motor groups separately
	 * Anti-drift is (obviously) disabled because the robot will not move straight.
	 * @param driveTrain
	 *            The drive train
	 * @param leftDistance
	 *            Distance to move the left motor group (can be negative)
	 * @param rightDistance
	 *            Distance to move the right motor group (can be negative)
	 * @param leftSpeed
	 *            Speed to move the left motor group
	 * @param rightSpeed
	 *            Speed to move the right motor group
	 */
	public DriveTrainMover(DriveTrain driveTrain, double leftDistance, double rightDistance, double leftSpeed, double rightSpeed) {
		MotorGroup left = driveTrain.getLeftGroup(), right = driveTrain.getRightGroup();
		addMotorGroup(left, leftDistance, leftSpeed);
		addMotorGroup(right, rightDistance, rightSpeed);
	}

	private void addMotorGroup(MotorGroup group) {
		for (Motor m : group) {
			motorMovers.add(new MotorMover(m, distance, speed, group.getEncoder(), group.getAntiDrift()));
		}
	}
	
	private void addMotorGroup(MotorGroup group, double moveDistance, double moveSpeed) {
		for (Motor m : group) {
			motorMovers.add(new MotorMover(m, moveDistance, moveSpeed, group.getEncoder()));
		}
	}

	/**
	 * Runs an iteration of all the motor movers
	 */
	public void runIteration() {
		for (MotorMover motorMover : motorMovers) {
			motorMover.runIteration();
		}
	}

	/**
	 * Tests if all motor movers have distance more than zero Equivalent to
	 * getAverageDistanceLeft() == 0
	 * 
	 * @return True if all motor movers are finished, false otherwise
	 */
	public boolean areAllFinished() {
		for (MotorMover motorMover : motorMovers) {
			if (motorMover.distanceLeft() > 0)
				return false;
		}
		return true;
	}

	/**
	 * @return True if any (even one) motors are finished, false otherwise
	 */
	public boolean areAnyFinished() {
		for (MotorMover motorMover : motorMovers) {
			if (motorMover.distanceLeft() <= 0)
				return true;
		}
		return false;
	}

	/**
	 * Note: motor mover distance left is capped at 0, so if it has finished (is
	 * negative), it doesn't cancel out something that has not finished
	 * 
	 * @return The average distance that all motor movers have traveled
	 */
	public double getAverageDistanceLeft() {
		double total = 0;
		for (MotorMover motorMover : motorMovers) {
			total += Math.max(0, motorMover.distanceLeft());
		}
		return total / motorMovers.size();
	}
}