package org.usfirst.frc.team1683.autonomous;

public class PathPoint {
	private double x, y;
	public PathPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getAngle() {
		return Math.toDegrees(Math.atan2(y, x));
	}
	public double getDistance() {
		return Math.hypot(x, y);
	}
}
