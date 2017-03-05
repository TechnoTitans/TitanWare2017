package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.autonomous.Autonomous.State;
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
public class VisionMiddle extends Autonomous {

	//private final double distance;
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

	public VisionMiddle(TankDrive tankDrive, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;
		gearScore = new GearScore(tankDrive, 0.3, piReader, 1.7, 0.0001, 0, "edge");
		presentState = State.INIT_CASE;
	}
	public void run() {// TODO feedback
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				timer.start();
				
				nextState = State.SCORE;
				break;
			case SCORE:
				gearScore.enable();
				gearScore.run();
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Middle vision state", presentState.toString());
		SmartDashboard.sendData("middle vision timer", timer.get());
		presentState = nextState;
	}
}