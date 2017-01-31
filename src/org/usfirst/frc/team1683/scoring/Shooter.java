package org.usfirst.frc.team1683.scoring;

import edu.wpi.first.wpilibj.TalonSRX;

public class Shooter {
	
	TalonSRX shooterWheel;
	public Shooter(int channel) {
		this.shooterWheel = new TalonSRX(channel);
	}
	
	//public double getSpeed() {
		// TODO until encoders are done
	//}
	//TODO PID LOOP
	public void setSpeed(double rotationSpeed){
		shooterWheel.set(rotationSpeed);
	}
}
