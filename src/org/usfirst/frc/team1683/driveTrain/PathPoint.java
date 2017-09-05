package org.usfirst.frc.team1683.driveTrain;

/**
 * A point on a path
 * 
 * Contains useful data such as the angle and distance
 */
public class PathPoint {
	private double x, y, angle;
	private boolean isRelative;

	/**
	 * Creates a point on a path
	 * 
	 * @param x
	 *            The x coordinate, in inches
	 * @param y
	 *            The y coordinate, in inches Note: isRelative defaults to true
	 */
	public PathPoint(double x, double y) {
		this(x, y, true);
	}

	/**
	 * Creates a point on a path
	 * 
	 * @param x
	 *            The x coordinate, in inches
	 * @param y
	 *            The y coordinate, in inches
	 * @param isRelative
	 *            True if the point should be considered relative to the
	 *            previous point (or the origin if it is the first point) in the
	 *            path, false if it should be considered absolutely on the
	 *            plane.
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
		for (int i = path.length - 1; i > 0; --i) {
			if (!path[i].isRelative()) {
				path[i].setRelativeTo(path[i - 1]);
			}
		}
	}

	/**
	 * 
	 * @return The angle this point is on, in degrees, where 0 degrees is
	 *         horizontal and moving counterclockwise
	 * 
	 */
	public double getAngle() {
		return (x == 0 && y == 0) ? angle : Math.toDegrees(Math.atan2(y, x));
	}

	public double getDistance() {
		return Math.hypot(x, y);
	}

	public static double getDistance(PathPoint point1, PathPoint point2) {
		return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
	}

	public static double getAngleTwoPoints(PathPoint currPoint, PathPoint nextPoint, double radius) {
		if (getDistance(currPoint, nextPoint) >= (2 * Math.abs(radius)))
			return Math.PI;
		return 2 * Math.asin(getDistance(currPoint, nextPoint) / (2 * Math.abs(radius)));
	}

	public static double atan2(PathPoint center, PathPoint nexPoint) {
		double angle = Math.atan2(nexPoint.getY() - center.getY(), nexPoint.getX() - center.getX());
		if (angle > 0)
			return angle;
		return 2 * Math.PI + angle;
	}

	public boolean isRelative() {
		return isRelative;
	}

	public String toString() {
		return x + ", " + y + ", " + getAngle();
	}

	void setRelativeTo(PathPoint other) {
		x -= other.x;
		y -= other.y;
		isRelative = true;
	}

	public PathPoint subtract(PathPoint other) {
		return new PathPoint(x - other.x, y - other.y, true);
	}
	
	/**
	 * Creates a path point that has coordinates (0, 0) (aka the robot doesn't move)
	 * but has a specified angle
	 * This is useful when you want the robot to turn at the end of the path
	 * but not move any
	 * @param angle
	 *         The angle to turn in degrees.
	 *         Note: 0 does NOT mean no turn, 0 means align with the horizontal x-axis
	 * @return
	 *         A path point at the origin with the specified angle
	 */
	public static PathPoint fromAngle(double angle) {
		PathPoint p = new PathPoint(0, 0);
		angle %= 360;
		if (angle > 180)
			angle -= 360;
		p.angle = angle;
		return p;
	}
	
	/**
	 * Creates a path point with coordinates (0, 0) but pointed towards the point (x, y)
	 * @param x
	 * @param y
	 * @return
	 *         A path point pointed at the origin pointed at (x, y)
	 * @see PathPoint#fromAngle(double angle)
	 */
	public static PathPoint inDirectionOf(double x, double y) {
		return fromAngle(Math.toDegrees(Math.atan2(y, x)));
	}
}
