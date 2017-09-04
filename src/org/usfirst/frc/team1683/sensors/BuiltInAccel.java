package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.robot.InputFilter;

public class BuiltInAccel {
	public static final double MAX_FLAT_ANGLE = 3.0;
	public static final double FILTER_K = 0.5;
	public static final double THRESHOLD = 0.4;

	private InputFilter filter;

	public BuiltInAccel() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public double getX() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public double getY() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public double getZ() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Axis: 0 for x, 1 for y, 2 for z
	 */
	public boolean isOverThreshold(int axis){
		throw new UnsupportedOperationException("Not implemented");
	}

	public boolean isFlat() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public double getAngleXZ() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public double getAngleYZ() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
