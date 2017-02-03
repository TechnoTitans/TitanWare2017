package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;

/**
 * Shoots at target
 * 
 * @author Yi Liu
 *
 */
public class PassLine extends Autonomous {
	public final double distance = 20000000; // inches

	public PassLine(TankDrive tankDrive) {
		super(tankDrive);
	}

	public void run() {
		switch(presentState){
			case INIT_CASE:
				tankDrive.moveDistance(distance, 0.1);
				break;
			case STOP:
				tankDrive.stop();
				break;
		}
		presentState = nextState;
	}
}
