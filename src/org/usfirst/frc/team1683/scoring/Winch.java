package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.sensors.LimitSwitch;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Winch {

	TalonSRX winchMotor;
	LimitSwitch limitSwitch;
	public static final double liftSpeed = 0;//TODO

	public Winch(int channel){//, LimitSwitch limitSwitch) {
		this.winchMotor = new TalonSRX(channel, true);
		//this.limitSwitch = limitSwitch;
	}
	
	//turn the winch
	public void turnWinch() {
		winchMotor.set(liftSpeed);
	}
	public void stop(){
		winchMotor.brake();
	}
}
