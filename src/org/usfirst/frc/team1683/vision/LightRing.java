package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

public class LightRing {
	TalonSRX light1;
	TalonSRX light2;
	TalonSRX light3;
	final double MAX_BRIGHTNESS = 0.7;

	public LightRing(TalonSRX light1, TalonSRX light2, TalonSRX light3) {
		this.light1 = light1;
		this.light2 = light2;
		this.light3 = light3;
	}

	public void turnOn() {
		light1.set(MAX_BRIGHTNESS);
		light2.set(MAX_BRIGHTNESS);
		light3.set(MAX_BRIGHTNESS);
	}

	public void turnOff() {
		light1.set(0);
		light2.set(0);
		light3.set(0);
	}
}
