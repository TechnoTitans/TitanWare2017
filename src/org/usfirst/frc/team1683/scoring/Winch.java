package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Winch implements ScoringMotor {

	TalonSRX winchMotor1;
	TalonSRX winchMotor2;

	private double liftSpeed = 0.6;// TODO

	public Winch(int channel1, int channel2) {
		this.winchMotor1 = new TalonSRX(channel1, true);
		this.winchMotor2 = new TalonSRX(channel2, false);
	}

	// Takes positive double
	public void setSpeed(double speed) {
		if (speed > 1)
			speed = 1;
		if (speed < 0)
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
