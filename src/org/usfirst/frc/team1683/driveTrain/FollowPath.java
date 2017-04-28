package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;

import org.usfirst.frc.team1683.constants.Constants;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class FollowPath {
	private TankDrive drive;
	private ArrayList<PathPoint> pathPoints = new ArrayList<PathPoint>();
	private PathPoint point1, point2, point3;
	private DriveTrainMover mover;
	private int index = 0;

	public FollowPath(TankDrive drive) {
		this.drive = drive;
		pathPoints.add(new PathPoint(146.0, 72.0));
		pathPoints.add(new PathPoint(152.0, 92.0));
		pathPoints.add(new PathPoint(165.0, 110.0));
		pathPoints.add(new PathPoint(180.0, 126.0));
		pathPoints.add(new PathPoint(195.0, 142.0));
		pathPoints.add(new PathPoint(214.0, 157.0));
		pathPoints.add(new PathPoint(235.0, 174.0));
		pathPoints.add(new PathPoint(246.0, 189.0));
		pathPoints.add(new PathPoint(251.0, 209.0));
		pathPoints.add(new PathPoint(255.0, 230.0));
		pathPoints.add(new PathPoint(245.0, 253.0));
		pathPoints.add(new PathPoint(229.0, 270.0));
		pathPoints.add(new PathPoint(204.0, 289.0));

		point1 = pathPoints.get(0);
		point2 = pathPoints.get(1);
		point3 = pathPoints.get(2);
		mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
				curveDirection() ? 0.3 : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * 0.3,
				curveDirection() ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * 0.3 : 0.3);
	}

	public void run() {
		SmartDashboard.sendData("CurrentlyRunning", index, true);
		SmartDashboard.sendData(index + " CalDistance1", calDistTravel()[0], true);
		SmartDashboard.sendData(index + " CalDistance2", calDistTravel()[1], true);
		SmartDashboard.sendData(index + " Speed1",
				curveDirection() ? 0.3 : calDistTravel()[0] / calDistTravel()[1] * 0.3, true);
		SmartDashboard.sendData(index + " Speed2",
				curveDirection() ? calDistTravel()[1] / calDistTravel()[0] * 0.3 : 0.3, true);
		SmartDashboard.sendData(index + " CurveDirection", curveDirection(), true);
		SmartDashboard.sendData(index + " Radius", calRadius(), true);
		SmartDashboard.sendData(index + " Angle", PathPoint.getAngleTwoPoints(point1, point2, calRadius()), true);
		if (index < pathPoints.size() - 2) {
			mover.runIteration();
			if (mover.areAnyFinished()) {
				drive.coast();
				index++;
				if (index < pathPoints.size() - 2) {
					point1 = pathPoints.get(index);
					point2 = pathPoints.get(index + 1);
					point3 = pathPoints.get(index + 2);
					mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
							curveDirection() ? 0.3 : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * 0.3,
							curveDirection() ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * 0.3 : 0.3);
				}
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

		if(area <= 0.6){
			return 9999;
		}
		double radius = side1 * side2 * side3 / (area * 4);
		return radius;
	}
}
