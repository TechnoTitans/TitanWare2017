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
	private DriveTrainMover driveTrainMover;

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
				timer.start();
				piReader.update();
				if (piReader.getConfidence() == 0.0 || 5==5) {
					nextState = State.NON_VISION_AIDED;
					mover = new DriveTrainMover(tankDrive, DEFAULT_DISTANCE, 0.4);
				}
				break;
			case NON_VISION_AIDED:
				mover.runIteration();
				SmartDashboard.sendData("distance left non vision aided middle gear", mover.getAverageDistanceLeft());
				if (mover.areAnyFinished() || timer.get() > 4) {
					tankDrive.stop();
					nextState = State.END_CASE;
				}
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Middle gear state", presentState.toString());
		SmartDashboard.sendData("middle gear timer", timer.get());
		presentState = nextState;
	}
}