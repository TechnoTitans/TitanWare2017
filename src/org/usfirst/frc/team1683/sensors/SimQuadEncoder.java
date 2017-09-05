package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.simulation.SimTalon;

/**
 * Encoder class. Used to measure how far the robot traveled
 */

public class SimQuadEncoder implements Encoder {

	private SimTalon talonSRX;
	private double wheelRadius;

	public SimQuadEncoder(SimTalon talonSRX, double wheelRadius) {
		this.talonSRX = talonSRX;
		this.wheelRadius = wheelRadius;
	}

	/**
	 * The total distance that the motor has traveled Multiplies rotations by
	 * 2*pi*r where r = wheel radius
	 * 
	 * @return total distance
	 */
	@Override
	public double getDistance() {
		return talonSRX.getPosition() * 2 * Math.PI * wheelRadius;
	}

	/**
	 * Just calls talonSRX.getSpeed()
	 * 
	 * @return The speed of the talon in RPM
	 */
	@Override
	public double getSpeed() {
		return talonSRX.getSpeed();
	}

	@Override
	public void reset() {
		talonSRX.setPosition(0);
	}

	public SimTalon getTalon() {
		return talonSRX;
	}
}