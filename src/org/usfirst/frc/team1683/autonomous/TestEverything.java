package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * Test everything works
 * 
 * @author Yi Liu
 *
 */

public class TestEverything extends Autonomous {
	Timer timer;
	Winch winch;
	DriveTrainMover mover;
	LimitSwitch limitSwitch;
	DriveTrainTurner turner;

	public TestEverything(TankDrive tankDrive) {
		super(tankDrive);
		timer = new Timer();
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		limitSwitch = new LimitSwitch(HWR.LIMIT_SWITCH);
		presentState = State.INIT_CASE;
	}

	/**
	 * 
	 * 1. Runs left side until limitswitch pressed
	 * 
	 * 2. Runs right side until limitswitch pressed
	 * 
	 * 3. Moves forward 30 inches based on encoders
	 * 
	 * 4. Turns winch for 3 seconds
	 * 
	 * 5. Turns robot 30 degrees clockwise
	 * 
	 */
	@Override
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				presentState = State.TEST_LEFT;
				break;
			case TEST_LEFT:
				tankDrive.setLeft(0.4);
				if (limitSwitch.isPressed()) {
					tankDrive.stop();
					presentState = State.TEST_RIGHT;
				}
				break;
			case TEST_RIGHT:
				tankDrive.setRight(0.4);
				if (limitSwitch.isPressed()) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, 30, 0.3);
					presentState = State.TEST_DRIVE;
				}
				break;
			case TEST_DRIVE:
				mover.runIteration();
				if (mover.areAllFinished()) {
					tankDrive.stop();
					timer.start();
					presentState = State.TEST_WINCH;
				}
				break;
			case TEST_WINCH:
				winch.turnOn();
				if (timer.get() > 3) {
					winch.stop();
					turner = new DriveTrainTurner(tankDrive, -30, 0.3);
					presentState = State.TEST_GYRO;
				}
				break;
			case TEST_GYRO:
				turner.run();
				if (turner.isDone()) {
					tankDrive.stop();
					presentState = State.END_CASE;
				}
			case END_CASE:
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}
