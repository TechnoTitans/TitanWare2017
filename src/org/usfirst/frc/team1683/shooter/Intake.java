package org.usfirst.frc.team1683.shooter;

import edu.wpi.first.wpilibj.TalonSRX;
/*
 * 
 * Author: Yi Liu
 * 
 */
public class Intake {

	TalonSRX intakeMotor;

	public static final double speed = 0; //TODO

	public Intake(int channel) {
		this.intakeMotor = new TalonSRX(channel);
	}

	// turn intake
	public void turnIntake() {
		intakeMotor.set(speed);
	}
}
