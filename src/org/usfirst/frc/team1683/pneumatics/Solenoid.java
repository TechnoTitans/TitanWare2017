package org.usfirst.frc.team1683.pneumatics;

public class Solenoid extends edu.wpi.first.wpilibj.Solenoid {

	public Solenoid(int moduleChannel, int channel) {
		super(moduleChannel, channel);
	}

	public boolean isExtended() {
		return super.get();
	}
}
