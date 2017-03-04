package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;

/**
 * Passes the line
 * 
 * @author Yi Liu
 *
 */
public class PassLine extends Autonomous {
	public final double distance = 300; // inches
	DriveTrainMover mover;

	public PassLine(TankDrive tankDrive) {
		super(tankDrive);
	}

	public void run() {
		switch(presentState){
			case INIT_CASE:
				mover = new DriveTrainMover(tankDrive, distance, 0.5);
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.STOP;
				}
			case STOP:
				tankDrive.set(0);
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}
