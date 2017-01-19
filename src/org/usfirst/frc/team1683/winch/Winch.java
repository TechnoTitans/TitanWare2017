package org.usfirst.frc.team1683.winch;

import org.usfirst.frc.team1683.sensors.LimitSwitch;

import edu.wpi.first.wpilibj.TalonSRX;

public class Winch {

	TalonSRX winchMotor;
	LimitSwitch limitSwitch;
	public static final double liftSpeed = 0;

	public Winch(int channel, LimitSwitch limitSwitch) {
		this.winchMotor = new TalonSRX(channel);
		this.limitSwitch = limitSwitch;
	}
	
	//turn the winch
	public void turnWinch() {
		winchMotor.set(liftSpeed);
		if (limitSwitch.isPressed()) {
			winchMotor.set(0);
		}
	}
}
