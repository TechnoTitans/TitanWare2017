package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;


/*
 * 
 * Author: Yi Liu
 * 
 */
public class Intake implements ScoringMotor {

	TalonSRX intakeMotor;
	public final double INTAKE_SPEED = 0.8;

	public Intake(int channel) {
		this.intakeMotor = new TalonSRX(channel, true);
	}

	// turn intake
	public void setSpeed(double speed) {
		intakeMotor.set(speed);
	}

	@Override
	public void turnOn() {
		intakeMotor.set(0.9);
		SmartDashboard.sendData("intake", "turning");
	}

	@Override
	public void stop() {
		intakeMotor.set(0);
		SmartDashboard.sendData("intake2", "stop");
	}
}
