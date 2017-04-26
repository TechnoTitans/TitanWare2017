package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.constants.Constants;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class AdvancedFollowPath {
	private TankDrive drive;
	private PathPoint[] pathPoints = { new PathPoint(0, 0, false), new PathPoint(0, 20, false),
			new PathPoint(3, 30, false), new PathPoint(30, 40, false), new PathPoint(35, 60, false) };

	private PathPoint point1, point2, point3;
	private DriveTrainMover mover;
	private int index = 0;
	private boolean isPaint = true;

	public AdvancedFollowPath(TankDrive drive) {
		this.drive = drive;

		point1 = pathPoints[0];
		point2 = pathPoints[1];
		point3 = pathPoints[2];

		mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
				curveDirection() ? 0.3 : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * 0.3,
				curveDirection() ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * 0.3 : 0.3);
	}

	public void run() {
		if (isPaint) {

		} else {
			SmartDashboard.sendData(index + " CalDistance1", calDistTravel()[0], true);
			SmartDashboard.sendData(index + " CalDistance2", calDistTravel()[1], true);
			SmartDashboard.sendData(index + " Speed1",
					curveDirection() ? 0.3 : calDistTravel()[0] / calDistTravel()[1] * 0.3, true);
			SmartDashboard.sendData(index + " Speed2",
					curveDirection() ? calDistTravel()[1] / calDistTravel()[0] * 0.3 : 0.3, true);
			SmartDashboard.sendData(index + " CurveDirection", curveDirection(), true);
			SmartDashboard.sendData(index + " Radius", calRadius(), true);
			SmartDashboard.sendData(index + " Angle", PathPoint.getAngleTwoPoints(point1, point3, calRadius()), true);
			if (index < pathPoints.length - 2) {
				SmartDashboard.sendData("Stopped", false, true);
				mover.runIteration();
				if (mover.areAnyFinished()) {
					drive.coast();
					index++;
					if (index < pathPoints.length - 2) {
						point1 = pathPoints[index];
						point2 = pathPoints[index + 1];
						point3 = pathPoints[index + 2];
						mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
								curveDirection() ? 0.3 : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * 0.3,
								curveDirection() ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * 0.3 : 0.3);
					}
				}
			} else {
				SmartDashboard.sendData("Stopped", true, true);
			}
		}
	}

	private double[] calDistTravel() {
		double distance1 = (calRadius() + (curveDirection() ? 1 : -1) * Constants.ROBOT_WIDTH / 2)
				* PathPoint.getAngleTwoPoints(point1, point2, calRadius());
		double distance2 = (calRadius() + (curveDirection() ? -1 : 1) * Constants.ROBOT_WIDTH / 2)
				* PathPoint.getAngleTwoPoints(point1, point2, calRadius());
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
