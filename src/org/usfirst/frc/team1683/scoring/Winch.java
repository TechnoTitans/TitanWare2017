package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.sensors.LimitSwitch;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Winch implements ScoringMotor{

	TalonSRX winchMotor1;
	TalonSRX winchMotor2;
	
	LimitSwitch limitSwitch;
	public static final double LIFT_SPEED = 0.3;//TODO

	public Winch(int channel1, int channel2){//, LimitSwitch limitSwitch) {
		this.winchMotor1 = new TalonSRX(channel1, true);
		this.winchMotor2 = new TalonSRX(channel2, true);
		//this.limitSwitch = limitSwitch;
	}
	
	//turn the winch
	public void turnOn() {
		winchMotor1.set(LIFT_SPEED);
		winchMotor2.set(LIFT_SPEED);
	}
	public void stop(){
		winchMotor1.brake();
		winchMotor2.brake();
	}
}
