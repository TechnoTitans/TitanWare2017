package org.usfirst.frc.team1683.scoring;

import edu.wpi.first.wpilibj.TalonSRX;
/*
 * 
 * Author: Yi Liu
 * 
 */
public class Intake implements ScoringMotor{

	TalonSRX intakeMotor;
	public final double INTAKE_SPEED = 0.8;
	public Intake(int channel) {
		this.intakeMotor = new TalonSRX(channel);
	}

	// turn intake
	public void setSpeed(double speed) {
		intakeMotor.set(speed);
	}

	@Override
	public void turnOn() {
		intakeMotor.set(INTAKE_SPEED);
		
	}

	@Override
	public void stop() {
		intakeMotor.set(0);
	}
}
