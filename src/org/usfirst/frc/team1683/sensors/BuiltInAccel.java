package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class BuiltInAccel extends BuiltInAccelerometer {
	public static final double MAX_FLAT_ANGLE = 3.0;
	public static final double FILTER_K = 0.5;

	private InputFilter filter;

	public BuiltInAccel() {
		super();
		filter = new InputFilter(FILTER_K);
	}

	@Override
	public double getX() {
		return super.getX();
	}

	@Override
	public double getY() {
		return super.getY();
	}

	@Override
	public double getZ() {
		return super.getZ();
	}

	public boolean isFlat() {
		return filter.filterInput(Math.abs(getAngleXZ())) < MAX_FLAT_ANGLE
				&& filter.filterInput(Math.abs(getAngleYZ())) < MAX_FLAT_ANGLE;
	}

	public double getAngleXZ() {
		return Math.atan2(getX(), getZ()) * 180 / Math.PI;
	}

	public double getAngleYZ() {
		return Math.atan2(getY(), getZ()) * 180 / Math.PI;
	}
}
