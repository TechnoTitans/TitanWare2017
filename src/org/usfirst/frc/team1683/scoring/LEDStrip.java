package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.Victor;

/*
 * @Author Yi
 * 
 */
public class LEDStrip {
	Victor ledStrip;
	final double BRIGHTNESS = 0.8;

	public LEDStrip(int channel) {
		this.ledStrip = new Victor(channel);
	}

	public void turnOn() {
		ledStrip.set(BRIGHTNESS);
	}
}
