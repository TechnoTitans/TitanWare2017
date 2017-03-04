package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.MotorMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

/**
 * middle gear scoring
 * 
 * @author Yi Liu
 *
 */

@SuppressWarnings("unused")
public class MiddleGear extends Autonomous {

	private final double distance;
	private static final double DEFAULT_DISTANCE = 20096;
	private final double pixelFromCenter = 10; // pixel (guessing)
	private final double turnSpeed = 3;
	private final double distanceFromGoal = 3; // degrees
	private final double speed = 0.5;
	private Timer timer;
	private DriveTrainMover driveTrainMover;

	GearScore gearScore;

	public MiddleGear(TankDrive tankDrive) {
		this(tankDrive, DEFAULT_DISTANCE);
		gearScore = new GearScore(tankDrive, 0.3, new PiVisionReader(), 1.7, 0.0001, 0, "edge");
	}

	public MiddleGear(TankDrive tankDrive, double distance) {
		super(tankDrive);
		this.distance = distance;

	}

	public void run() {// TODO feedback
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				timer.start();
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				gearScore.run();
				if (timer.get() > 10) {
					gearScore.disable();
					nextState = State.END_CASE;
				}
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