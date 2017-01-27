package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;

/**
 * Shoots at target
 * 
 * @author Yi Liu
 *
 */
public class PassLine extends Autonomous {
	public final double distance = 2; // inches

	public PassLine(TankDrive tankDrive) {
		super(tankDrive);
	}

	public void run() {
		tankDrive.moveDistance(distance, 0.3);
	}
}
