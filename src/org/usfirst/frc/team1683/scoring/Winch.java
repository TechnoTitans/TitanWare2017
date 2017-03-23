package org.usfirst.frc.team1683.scoring;

/**
 * 
 * Controls the winch
 * 
 * @author Yi Liu
 * 
 */
import org.usfirst.frc.team1683.sensors.LimitSwitch;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Winch implements ScoringMotor {

	TalonSRX winchMotor1;
	TalonSRX winchMotor2;

	LimitSwitch limitSwitch;
	private double liftSpeed = 0.6;// TODO

	public Winch(int channel1, int channel2) {// , LimitSwitch limitSwitch) {
		this.winchMotor1 = new TalonSRX(channel1, false);
		this.winchMotor2 = new TalonSRX(channel2, true);
		// this.limitSwitch = limitSwitch;
	}
	
	//Takes positive double 
	public void setSpeed(double speed){
		if(speed > 1)
			speed = 1;
		if(speed < 0)
			speed = 0;
		liftSpeed = speed;
	}

	// turn the winch
	public void turnOn() {
		winchMotor1.set(liftSpeed);
		winchMotor2.set(liftSpeed);
	}

	public void turnOtherWay() {
		winchMotor1.set(-liftSpeed);
		winchMotor2.set(-liftSpeed);
	}

	public void stop() {
		winchMotor1.coast();
		winchMotor2.coast();
	}

	public TalonSRX getMotor1() {
		return winchMotor1;
	}

	public TalonSRX getMotor2() {
		return winchMotor2;
	}
}
