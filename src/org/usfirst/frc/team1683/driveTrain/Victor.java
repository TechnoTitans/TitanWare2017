package org.usfirst.frc.team1683.driveTrain;

public class Victor extends edu.wpi.first.wpilibj.Victor {

	public Victor(int channel) {
		super(channel);
	}

	public Victor(int channel, boolean inverted) {
		super(channel);
		super.setInverted(inverted);
	}
}
