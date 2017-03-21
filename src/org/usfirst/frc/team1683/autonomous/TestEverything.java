package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.scoring.Winch;

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

	public TestEverything(TankDrive tankDrive) {
		super(tankDrive);
		timer = new Timer();
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		presentState = State.INIT_CASE;
	}

	@Override
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				presentState = State.TEST_LEFT;
				timer.start();
				break;
			case TEST_LEFT:
				tankDrive.setLeft(0.4);
				if (timer.get() > 2) {
					tankDrive.stop();
					presentState = State.TEST_RIGHT;
				}
				break;
			case TEST_RIGHT:
				if (timer.get() > 3)
					tankDrive.setRight(0.4);
				if (timer.get() > 5) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, 30, 0.3);
					presentState = State.TEST_DRIVE;
				}
				break;
			case TEST_DRIVE:
				if (timer.get() > 6)
					mover.runIteration();
				if (mover.areAllFinished()) {
					tankDrive.stop();
					timer.reset();
					timer.start();
					presentState = State.TEST_WINCH;
				}
				break;
			case TEST_WINCH:
				if (timer.get() > 1)
					winch.turnOn();
				if (timer.get() > 3) {
					winch.stop();
					presentState = State.END_CASE;
				}
				break;
			case END_CASE:
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}
