package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Shooter implements ScoringMotor {

	TalonSRX shooterWheel;

	public final double speed = 0.6715;

	public Shooter(int channel) {
		this.shooterWheel = new TalonSRX(channel, false);
	}

	// public double getSpeed() {
	// TODO until encoders are done
	// }
	// TODO PID LOOP
	public void setSpeed(double rotationSpeed) {
		shooterWheel.set(rotationSpeed);
	}

	public void turnOn() {
		shooterWheel.set(speed);
	}

	public void stop() {
		shooterWheel.set(0);
	}

	public double getSpeed() {
		return shooterWheel.getEncoder().getDistance();
	}
}
