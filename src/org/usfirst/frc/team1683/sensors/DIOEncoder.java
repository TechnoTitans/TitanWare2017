package org.usfirst.frc.team1683.sensors;

public class DIOEncoder extends edu.wpi.first.wpilibj.Encoder implements Encoder {

	// Pulses per inch(?) from encoder
	//private double wheelDistancePerPulse;

	public DIOEncoder(int aChannel, int bChannel, boolean reverseDirection, double wheelDistancePerPulse) {
		super(aChannel, bChannel, reverseDirection);
		//this.wheelDistancePerPulse = wheelDistancePerPulse;
		super.setDistancePerPulse(wheelDistancePerPulse);
	}

	/**
	 * @return distance in inches
	 */
	@Override
	public double getDistance() {
		return super.getDistance();
	}

	/**
	 * @return speed in inches/sec
	 */
	@Override
	public double getSpeed() {
		return super.getRate();
	}

	@Override
	public void reset() {
		super.reset();
	}
}
