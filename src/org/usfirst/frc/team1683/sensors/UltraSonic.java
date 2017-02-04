package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class UltraSonic extends edu.wpi.first.wpilibj.Ultrasonic implements Sensor {

	public UltraSonic(DigitalOutput pingChannel, DigitalInput echoChannel) {
		super(pingChannel, echoChannel);
	}
	public double getDistance() {
		return super.getRangeInches();
	}

	@Override
	public double getRaw() {
		return getDistance();
	}
}
