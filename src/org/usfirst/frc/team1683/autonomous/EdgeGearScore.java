package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.autonomous.Autonomous.State;
import org.usfirst.frc.team1683.driveTrain.TankDrive;

/**
 * Shoots at target
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

	public EdgeGearScore(TankDrive tankDrive) {
		super(tankDrive);
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				tankDrive.moveDistance(distance);
				nextState = State.REALIGN;
				break;
			case REALIGN:
				/*
				 * while(vision.isNull ||
				 * vision.distanceFromCenter<pixelFromCenter){
				 * tankDrive.turn(turnSpeed); }
				 */
				nextState = State.APPROACH_GOAL;
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