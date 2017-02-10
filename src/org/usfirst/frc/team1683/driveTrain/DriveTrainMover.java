package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Pran
 *
 */
public class DriveTrainMover {
	private List<MotorMover> motorMovers = new ArrayList<MotorMover>();
	private double distance, speed;
	public DriveTrainMover(DriveTrain driveTrain, double distance, double speed) {
		this.distance = distance;
		this.speed = speed;
		MotorGroup left = driveTrain.getLeftGroup(), right = driveTrain.getRightGroup();
		addMotorGroup(left);
		addMotorGroup(right);
	}
	
	private void addMotorGroup(MotorGroup group) {
		for (Motor m : group) {
			motorMovers.add(new MotorMover(m, distance, speed, group.getEncoder(), group.getAntiDrift()));
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
	 * Tests if all motor movers have distance more than zero
	 * Equivalent to getAverageDistanceLeft() == 0
	 * @return True if all motor movers are finished, false otherwise
	 */
	public boolean areAllFinished() {
		for (MotorMover motorMover : motorMovers) {
			if (motorMover.distanceLeft() > 0) return false;
		}
		return true;
	}
	
	/**
	 * @return True if any (even one) motors are finished, false otherwise
	 */
	public boolean areAnyFinished() {
		for (MotorMover motorMover : motorMovers) {
			if (motorMover.distanceLeft() <= 0) return true;
		}
		return false;
	}
	
	/**
	 * Note: motor mover distance left is capped at 0, so if it has finished (is negative), it doesn't cancel out something that has not finished
	 * @return The average distance that 
	 */
	public double getAverageDistanceLeft() {
		double total = 0;
		for (MotorMover motorMover : motorMovers) {
			total += Math.max(0, motorMover.distanceLeft());
		}
		return total / motorMovers.size();
	}
}
