package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

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
	public final double speed = 0.2;
	private boolean right;
	private PiVisionReader vision;
	private Timer timer;
	private DriveTrainMover driveTrainMover;
	/**
	 * Places a gear when not starting in the middle
	 * @param tankDrive
	 * @param right -- True if on the right side, false if on the left side
	 */
	public EdgeGearScore(TankDrive tankDrive, boolean right) {
		super(tankDrive);
		vision = new PiVisionReader();
		this.right = right;
		timer = new Timer();
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer.start();
				driveTrainMover = new DriveTrainMover(tankDrive, distance, speed);
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				driveTrainMover.runIteration();
				SmartDashboard.sendData("encoder average distance", driveTrainMover.getAverageDistanceLeft());
				if (driveTrainMover.areAnyFinished()) {
					nextState = State.REALIGN;
				}
				break;
			case DRIVE_FORWARD_WAITING:
				if (tankDrive.hasMoveDistanceFinished() || timer.get() > 3) {
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
				if (vision.getDistanceTarget() < pixelFromCenter || timer.get() > 6) {
					nextState = State.APPROACH_GOAL;
					tankDrive.set(0);
				}
				break;
			case APPROACH_GOAL:
				/*
				 * while(vision.getDistance < distanceFromGoal){
				 * tankDrive.set(speed); }
				 */
				tankDrive.stop();
				if (timer.get() > 10) nextState = State.SCORE;
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
		SmartDashboard.sendData("timer", timer.get());
		SmartDashboard.sendData("Edge gear state", presentState.toString());
		presentState = nextState;
	}
}