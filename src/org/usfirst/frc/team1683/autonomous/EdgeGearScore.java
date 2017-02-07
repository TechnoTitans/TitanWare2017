package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.vision.PiVisionReader;

/**
 * Edge gear scoring
 * 
 * @author Yi Liu
 *
 */
public class EdgeGearScore extends Autonomous {
	public final double distance = 96; // guessing distance (inches)
	public final double pixelFromCenter = 10; // pixel (guessing)
	public final double turnSpeed = 3; // degrees
	public final double distanceFromGoal = 3; // degrees
	public final double speed = 5;
	private boolean right;
	private PiVisionReader vision;
	/**
	 * Places a gear when not starting in the middle
	 * @param tankDrive
	 * @param right -- True if on the right side, false if on the left side
	 */
	public EdgeGearScore(TankDrive tankDrive, boolean right) {
		super(tankDrive);
		vision = new PiVisionReader();
		this.right = right;
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				tankDrive.moveDistance(distance);
				nextState = State.DRIVE_FORWARD_WAITING;
				break;
			case DRIVE_FORWARD_WAITING:
				if (tankDrive.hasMoveDistanceFinished()) {
					nextState = State.REALIGN;
				}
				break;
			case REALIGN:
				/*
				 * while(vision.isNull ||
				 * vision.distanceFromCenter<pixelFromCenter){
				 * tankDrive.turn(turnSpeed); }
				 */
				tankDrive.turnInPlace(!right, 0.2);
				if (vision.distanceFromCenter() < pixelFromCenter) {
					nextState = State.APPROACH_GOAL;
				}
				break;
			case APPROACH_GOAL:
				/*
				 * while(vision.getDistance<distanceFromGoal){
				 * tankDrive.set(speed); }
				 */
				tankDrive.stop();
				nextState = State.SCORE;
			case SCORE:
				/*
				 * piston.extend();
				 */
				nextState = State.END_CASE;
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}