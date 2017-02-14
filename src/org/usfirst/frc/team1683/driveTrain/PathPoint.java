package org.usfirst.frc.team1683.driveTrain;

/**
 * A point on a path
 * Contains useful data such as the angle and distance
 * @author Pran
 *
 */
public class PathPoint {
	private double x, y;
	private boolean isRelative;
	/**
	 * Creates a point on a path
	 * @param x The x coordinate, in inches
	 * @param y The y coordinate, in inches
	 * Note: isRelative defaults to true
	 */
	public PathPoint(double x, double y) {
		this(x, y, true);
	}
	/**
	 * Creates a point on a path
	 * @param x The x coordinate, in inches
	 * @param y The y coordinate, in inches
	 * @param isRelative True if the point should be considered relative to the previous point (or the origin if it is the first point) in the path,
	 * 						false if it should be considered absolutely on the plane.
	 */
	public PathPoint(double x, double y, boolean isRelative) {
		this.x = x;
		this.y = y;
		this.isRelative = isRelative;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	static void convertAbsoluteToRelative(PathPoint[] path) {
		for (int i = 1; i < path.length; ++i) {
			if (!path[i].isRelative()) {
				path[i].setRelativeTo(path[i - 1]);
			}
		}
	}
	/**
	 * 
	 * @return The angle this point is on, in degrees, where 0 degrees is horizontal and moving counterclockwise
	 */
	public double getAngle() {
		return Math.toDegrees(Math.atan2(y, x));
	}
	public double getDistance() {
		return Math.hypot(x, y);
	}
	public boolean isRelative() {
		return isRelative;
	}
	public String toString() {
		return x + ", " + y;
	}
	void setRelativeTo(PathPoint other) {
		x -= other.x;
		y -= other.y;
		isRelative = true;
	}
}
