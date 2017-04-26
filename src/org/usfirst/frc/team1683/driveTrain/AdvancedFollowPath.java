package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.constants.Constants;

public class AdvancedFollowPath {
	private TankDrive drive;
	private PathPoint[] pathPoints = { new PathPoint(0, 0, false), new PathPoint(0, 20, false),
			new PathPoint(20, 20, false) };

	private PathPoint point1, point2, point3;
	private DriveTrainMover mover;

	public AdvancedFollowPath(TankDrive drive) {
		this.drive = drive;

		point1 = pathPoints[0];
		point2 = pathPoints[1];
		point3 = pathPoints[2];

		mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
				curveDirection() ? 0.3 : calDistTravel()[1] / calDistTravel()[0] * 0.3,
				curveDirection() ? calDistTravel()[0] / calDistTravel()[1] * 0.3 : 0.3);
	}

	public void run() {
		mover.runIteration();
		if (mover.areAllFinished()) {
			drive.stop();
		}
	}

	private double[] calDistTravel() {
		double distance1 = (calRadius() + (curveDirection() ? 1 : -1) * Constants.ROBOT_WIDTH / 2)
				* PathPoint.getAngleTwoPoints(point1, point3, calRadius());
		double distance2 = (calRadius() + (curveDirection() ? -1 : 1) * Constants.ROBOT_WIDTH / 2)
				* PathPoint.getAngleTwoPoints(point1, point3, calRadius());
		return new double[] { distance1, distance2 };
	}

	// return true if right and false if left
	private boolean curveDirection() {
		double slope = (point3.getY() - point1.getY()) / (point3.getX() - point1.getX());
		double offset = point2.getY() - (point1.getY() + slope * (point2.getX() - point1.getX()));
		return 0 < offset * (slope > 0 ? 1 : -1);
	}

	private double calRadius() {
		double side1 = PathPoint.getDistance(point1, point2);
		double side2 = PathPoint.getDistance(point2, point3);
		double side3 = PathPoint.getDistance(point3, point1);

		double semiPeri = (side1 + side2 + side3) / 2;
		double area = Math.sqrt(semiPeri * (semiPeri - side1) * (semiPeri - side2) * (semiPeri - side3));

		double radius = side1 * side2 * side3 / (area * 4);
		return radius;
	}
}
