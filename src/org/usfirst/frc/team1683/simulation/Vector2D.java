package org.usfirst.frc.team1683.simulation;

public class Vector2D {
	public double x, y;
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector2D fromAngle(double magnitude, double angle) {
		return new Vector2D(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	public void add(double a, double b) {
		x += a;
		y += b;
	}
	public void add(Vector2D other) {
		add(other.x, other.y);
	}
	public void subtract(double a, double b) {
		x -= a;
		y -= b;
	}
	public void subtract(Vector2D other) {
		subtract(other.x, other.y);
	}
	public Vector2D getPerpendicularUnitVector() {
		// returns vector 90 degrees counter clockwise
		Vector2D ret = new Vector2D(-y, x);
		ret.normalize();
		return ret;
	}
	public void normalize() {
		double mag = magnitude();
		multiply(1 / mag);
	}
	public double magnitude() {
		return Math.hypot(x, y);
	}
	void multiply(double k) {
		x *= k;
		y *= k;
	}
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	public double getAngle() {
		return Math.atan2(y, x);
	}
	public void rotateAbout(double theta, Vector2D center) {
		subtract(center);
		rotate(theta);
		add(center);
	}
	public void rotate(double theta) {
		double cosTheta = Math.cos(theta), sinTheta = Math.sin(theta);
		x = cosTheta * x - sinTheta * y;
		y = sinTheta * x + cosTheta * y;
	}
	
	public String toString() {
		return x + ", " + y;
	}
}
