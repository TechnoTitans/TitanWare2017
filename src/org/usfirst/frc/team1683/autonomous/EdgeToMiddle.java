package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class EdgeToMiddle extends Autonomous {
	boolean rightSide;
	final double speed = 0.4;
	Path path;
	PathPoint[] pathPoints = { new PathPoint(0, 5), // TODO
			new PathPoint(60, 0, false), // TODO
			new PathPoint(0, 50),// TODO
	};

	public EdgeToMiddle(TankDrive drive, boolean rightSide) {
		super(drive);
		this.rightSide = rightSide;

		setPath();
	}

	public void setPath() {
		if (rightSide) {
			for (int i = 0; i < pathPoints.length; ++i) {
				PathPoint p = pathPoints[i];
				pathPoints[i] = new PathPoint(-p.getX(), p.getY());
			}
		}
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer.start();
				path = new Path(tankDrive, pathPoints, speed);
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone()) {
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("timer", timer.get());
		SmartDashboard.sendData("Edge gear state", presentState.toString());
		presentState = nextState;
	}
}
