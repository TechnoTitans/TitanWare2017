package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class Shooter {
	
	TalonSRX shooterWheel;
	
	public final double speed = 0.8;
	public Shooter(int channel) {
		this.shooterWheel = new TalonSRX(channel, false);
	}
	
	//public double getSpeed() {
		// TODO until encoders are done
	//}
	//TODO PID LOOP
	public void setSpeed(double rotationSpeed){
		shooterWheel.set(rotationSpeed);
	}
	public void turnOn(){
		shooterWheel.set(speed);
	}
	public void turnOff(){
		shooterWheel.set(0);
	}
}
