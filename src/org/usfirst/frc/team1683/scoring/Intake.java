package org.usfirst.frc.team1683.scoring;

import edu.wpi.first.wpilibj.TalonSRX;
/*
 * 
 * Author: Yi Liu
 * 
 */
public class Intake {

	TalonSRX intakeMotor;

	public Intake(int channel) {
		this.intakeMotor = new TalonSRX(channel);
	}

	// turn intake
	public void setIntakeSpeed(double speed) {
		intakeMotor.set(speed);
	}
}
