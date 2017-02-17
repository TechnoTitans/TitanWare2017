package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.robot.HWR;

public class LightRing {
	TalonSRX light;
	final double MAX_BRIGHTNESS = 0.7;

	public LightRing(int light) {
		this.light = new TalonSRX(HWR.GREEN_LIGHT_LOW, true);
	}

	public void turnOn() {
		light.set(MAX_BRIGHTNESS);
	}

	public void turnOff() {
		light.set(0);
	}
}