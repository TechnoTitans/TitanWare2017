package org.usfirst.frc.team1683.sensors;

/**
 * 
 * Analog acceleromter
 * 
 * Not used. Might be used later to determine if we are at the peg.
 *
 */
public class AnalogAccel extends edu.wpi.first.wpilibj.AnalogAccelerometer {
	public static final double SENSITIVITY = 3.3 / 6;
	public static final double ZERO_G_IN_VOLTS = 1.65;

	public AnalogAccel(int channel) {
		super(channel);
		super.setSensitivity(SENSITIVITY);
		super.setZero(ZERO_G_IN_VOLTS);
	}

	@Override
	public double getAcceleration() {
		return super.getAcceleration();
	}
}
