package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
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

public class TestEverything extends Autonomous{
	Timer compTimer;
	Timer timer;
	Winch winch;
	DriveTrainMover mover;
	LimitSwitch limitSwitch;
	DriveTrainTurner turner;

	public TestEverything(TankDrive tankDrive, LimitSwitch limitSwitch) {
		super(tankDrive);
		
		compTimer = new Timer();
		timer = new Timer();
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		
		this.limitSwitch = limitSwitch;
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
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.TEST_LEFT;
				compTimer.start();
				break;
			case TEST_LEFT:
				tankDrive.setLeft(0.4);
				if (compTimer.get() > 3) {
					tankDrive.stop();
					nextState = State.TEST_RIGHT;
					timer.start();
				}
				break;
			case TEST_RIGHT:
				tankDrive.setRight(0.4);
				if (compTimer.get() > 6) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, 100, 0.3);
					nextState = State.TEST_DRIVE;
				}
				break;
			case TEST_DRIVE:
				mover.runIteration();
				if (mover.areAllFinished()) {
					tankDrive.stop();
					timer.reset();
					timer.start();
					nextState = State.TEST_WINCH;
				}
				break;
			case TEST_WINCH:
				winch.turnOn();
				if (timer.get() > 3) {
					winch.stop();
					turner = new DriveTrainTurner(tankDrive, -30, 0.3);
					nextState = State.TEST_GYRO;
				}
				break;
			case TEST_GYRO:
				turner.run();
				if (turner.isDone()) {
					tankDrive.stop();
					nextState = State.END_CASE;
				}
			case END_CASE:
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Auto Timer", compTimer.get(), true);
		SmartDashboard.sendData("Auto State", presentState.toString(), true);
		presentState = nextState;
	}
}
