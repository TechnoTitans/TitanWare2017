package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;
import org.usfirst.frc.team1683.driveTrain.TankDrive;

/**
 * Passes the line
 * 
 * @author Yi Liu
 *
 */
public class PassLine extends Autonomous {
	public final double distance = 300; // inches
	public final double backup = -250;
	DriveTrainMover mover;
	DriveTrainTurner turner;
	boolean turnRight;

	public PassLine(TankDrive tankDrive, boolean turnRight) {
		super(tankDrive);
		this.turnRight = turnRight;
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				mover = new DriveTrainMover(tankDrive, distance, 0.5);
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, backup, 0.5);
					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					turner = new DriveTrainTurner(tankDrive, (turnRight ? -1 : 1) * 56, 0.2);
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				tankDrive.stop();
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}
