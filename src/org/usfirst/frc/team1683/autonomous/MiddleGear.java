package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.Timer;

/**
 * middle gear scoring
 * 
 * @author Yi Liu
 *
 */
public class MiddleGear extends Autonomous {
	public final double distance = 96; // guessing distance (inches)
	private final double pixelFromCenter = 10; // pixel (guessing)
	private final double turnSpeed = 3; // degrees
	private final double distanceFromGoal = 3; // degrees
	public final double speed = 5;
	private final Timer timer;
	public MiddleGear(TankDrive tankDrive) {
		super(tankDrive);
		timer = new Timer();
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.DRIVE_FORWARD;
				timer.start();
				break;
			case DRIVE_FORWARD:
				tankDrive.moveDistance(distance);
				nextState = State.DRIVE_FORWARD_WAITING;
				break;
			case DRIVE_FORWARD_WAITING:
				if (tankDrive.hasMoveDistanceFinished() || timer.get() > 1000) {
					nextState = State.SCORE;
					timer.stop();
				}
				break;
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
		SmartDashboard.sendData("Middle gear state", presentState.toString());
		presentState = nextState;
	}
}