package org.usfirst.frc.team1683.driveTrain;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.usfirst.frc.team1683.constants.Constants;
import org.usfirst.frc.team1683.driverStation.Draw;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class FollowPath {
	private TankDrive drive;
	private ArrayList<PathPoint> pathPoints;
	private PathPoint point1, point2, point3;
	private DriveTrainMover mover;
	private int index = 0;

	private boolean isPaint = true;
	private Draw draw = new Draw();
	private JFrame frame;

	public FollowPath(TankDrive drive) {
		this.drive = drive;

		point1 = pathPoints.get(0);
		point2 = pathPoints.get(1);
		point3 = pathPoints.get(2);

		mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
				curveDirection() ? 0.3 : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * 0.3,
				curveDirection() ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * 0.3 : 0.3);

		frame = new JFrame("Draw Robot Path");
		frame.getContentPane().add(draw, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isPaint = false;
			}
		});
	}

	public void run() {
		if (!isPaint) {
			if (pathPoints == null)
				pathPoints = draw.getArray();
			SmartDashboard.sendData(index + " CalDistance1", calDistTravel()[0], true);
			SmartDashboard.sendData(index + " CalDistance2", calDistTravel()[1], true);
			SmartDashboard.sendData(index + " Speed1",
					curveDirection() ? 0.3 : calDistTravel()[0] / calDistTravel()[1] * 0.3, true);
			SmartDashboard.sendData(index + " Speed2",
					curveDirection() ? calDistTravel()[1] / calDistTravel()[0] * 0.3 : 0.3, true);
			SmartDashboard.sendData(index + " CurveDirection", curveDirection(), true);
			SmartDashboard.sendData(index + " Radius", calRadius(), true);
			SmartDashboard.sendData(index + " Angle", PathPoint.getAngleTwoPoints(point1, point3, calRadius()), true);
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

		double radius = side1 * side2 * side3 / (area * 4);
		return radius;
	}
}
