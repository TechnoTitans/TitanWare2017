package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * Middle gear scoring
 * 
 * @author Yi Liu
 *
 */

public class MiddleGear extends Autonomous {

	private static final double DEFAULT_DISTANCE = 112;
	private Timer timer;
	private Timer timer2;
	private Timer timer3;
	private Timer waitTimer;

	private boolean shakeRight = true;
	PiVisionReader piReader;
	DriveTrainMover mover;

	public MiddleGear(TankDrive tankDrive, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;
		presentState = State.INIT_CASE;
	}

	/**
	 * 
	 * 1. Moves forward a fixed distance
	 * 
	 * 2. Backs up a small distance ~2 inches
	 * 
	 * 3. Shake
	 * 
	 */
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				timer2 = new Timer();
				timer3 = new Timer();
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
					nextState = State.SHAKE;
					waitTimer.start();
					timer2.start();
					timer3.start();
				}
				break;
			case SHAKE:
				if (timer2.get() > 3) {
					nextState = State.END_CASE;
					tankDrive.stop();
				} else {
					tankDrive.turnInPlace(shakeRight, 0.15);
					if (timer3.get() > 0.18) {
						shakeRight = !shakeRight;
						timer3.reset();
					}
				}
				break;
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