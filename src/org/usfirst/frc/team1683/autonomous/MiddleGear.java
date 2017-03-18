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
	private static final double DEFAULT_DISTANCE = 112;
	private final double pixelFromCenter = 10; // pixel (guessing)
	private final double turnSpeed = 3;
	private final double distanceFromGoal = 3; // degrees
	private final double speed = 0.5;
	private Timer timer;
	private Timer timer2;
	private Timer waitTimer;
	private DriveTrainMover driveTrainMover;

	private boolean shakeRight = true;
	GearScore gearScore;
	PiVisionReader piReader;
	DriveTrainMover mover;

	public MiddleGear(TankDrive tankDrive, PiVisionReader piReader) {
		this(tankDrive, DEFAULT_DISTANCE);
		this.piReader = piReader;
		gearScore = new GearScore(tankDrive, 0.3, piReader, 1.7, 0.0001, 0, "edge");
		presentState = State.INIT_CASE;
	}

	public MiddleGear(TankDrive tankDrive, double distance) {
		super(tankDrive);
		this.distance = distance;

	}

	public void run() {// TODO feedback
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				timer2 = new Timer();
				waitTimer = new Timer();

				nextState = State.DRIVE_FORWARD;
				mover = new DriveTrainMover(tankDrive, DEFAULT_DISTANCE, 0.3);
				break;
			case DRIVE_FORWARD:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					waitTimer.start();
					SmartDashboard.sendData("waitTimer", waitTimer.get(), false);
					nextState = State.WAIT;
				}
				break;
			case WAIT:
				tankDrive.stop();
				if (waitTimer.get() > 0.1) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, -2, 0.3);
					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.END_CASE;
					waitTimer.start();
				}
				break;
//			case SHAKE:
//				if (timer2.get() > 3) {
//					nextState = State.END_CASE;
//					tankDrive.stop();
//				} else {
//					tankDrive.turnInPlace(shakeRight, 0.15);
//					if (timer.get() > 0.18) {
//						shakeRight = !shakeRight;
//						timer.reset();
//					}
//				}
//				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Auto State", presentState.toString(), true);
		SmartDashboard.sendData("Auto Timer", timer.get(), true);
		presentState = nextState;
	}
}