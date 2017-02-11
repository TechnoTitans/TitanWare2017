package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.sensors.LimitSwitch;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Winch implements ScoringMotor{

	TalonSRX winchMotor;
	LimitSwitch limitSwitch;
	public static final double LIFT_SPEED = 0.6;//TODO

	public Winch(int channel){//, LimitSwitch limitSwitch) {
		this.winchMotor = new TalonSRX(channel, true);
		//this.limitSwitch = limitSwitch;
	}
	
	//turn the winch
	public void turnOn() {
		winchMotor.set(LIFT_SPEED);
	}
	public void stop(){
		winchMotor.brake();
	}
}
