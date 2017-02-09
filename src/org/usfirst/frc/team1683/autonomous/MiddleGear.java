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
	private final Timer timer;
	private DriveTrainMover driveTrainMover;
	public MiddleGear(TankDrive tankDrive) {
		super(tankDrive);
		timer = new Timer();
		driveTrainMover = new DriveTrainMover(tankDrive, distance, speed);
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.DRIVE_FORWARD;
				timer.start();
				break;
			case DRIVE_FORWARD:
				driveTrainMover.runIteration();
				SmartDashboard.sendData("encoder average distance", driveTrainMover.getAverageDistanceLeft());
				if (driveTrainMover.areAnyFinished()) {
					timer.reset();
					nextState = State.SCORE;
				}
				break;
			case SCORE:
				/*
				 * piston.extend();
				 */
				if (timer.get() > 3) nextState = State.END_CASE;
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