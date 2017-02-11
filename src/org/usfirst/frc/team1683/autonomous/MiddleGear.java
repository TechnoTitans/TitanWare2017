package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.MotorMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.Timer;

/**
 * middle gear scoring
 * 
 * @author Yi Liu
 *
 */

@SuppressWarnings("unused")
public class MiddleGear extends Autonomous {

	public final double distance = 96; // guessing distance (inches)
	private final double pixelFromCenter = 10; // pixel (guessing)
	private final double turnSpeed = 3; // degrees
	private final double distanceFromGoal = 3; // degrees
	public final double speed = 5;
	private Timer timer;
	private DriveTrainMover driveTrainMover;
	public MiddleGear(TankDrive tankDrive) {
		super(tankDrive);
	}
	
	private void driveForward() {
		driveTrainMover.runIteration();
		SmartDashboard.sendData("encoder average distance", driveTrainMover.getAverageDistanceLeft());
		if (driveTrainMover.areAnyFinished()) {
			timer.start();
			nextState = State.SCORE;
			tankDrive.set(0);
		}
	}

	private void score() {
		/*
		 * piston.extend();
		 */
		SmartDashboard.sendData("piston", true);
		SmartDashboard.sendData("elapsed time", timer.get());
		if (timer.get() > 10) nextState = State.END_CASE;
	}
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				driveTrainMover = new DriveTrainMover(tankDrive, distance, speed);
				SmartDashboard.sendData("ranInit", Math.random());
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				driveForward();
				break;
			case SCORE:
				score();
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
			break;
		}
		SmartDashboard.sendData("Middle gear state", presentState.toString());
		presentState = nextState;
	}
}