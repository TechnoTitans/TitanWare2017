package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;


/*
 * 
 * Author: Yi Liu
 * 
 */
public class Agitator implements ScoringMotor {

	TalonSRX intakeMotor;
	public final double AGITATOR_SPEED = 0.3;

	public Agitator(int channel) {
		this.intakeMotor = new TalonSRX(channel, true);
	}

	// turn intake
	public void setSpeed(double speed) {
		intakeMotor.set(speed);
	}

	@Override
	public void turnOn() {
		intakeMotor.set(-AGITATOR_SPEED);
		SmartDashboard.sendData("intake", "turning");
	}

	@Override
	public void stop() {
		intakeMotor.set(0);
		SmartDashboard.sendData("intake2", "stop");
	}
}
