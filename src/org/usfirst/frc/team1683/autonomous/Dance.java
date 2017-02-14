package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;

public class Dance extends Autonomous {
	Path path;
	PathPoint[] pathPoints = {
			new PathPoint(20, 20),
			new PathPoint(0, -20),
			new PathPoint(-20, 0)
	};
	public Dance(TankDrive tankDrive) {
		super(tankDrive);
	}

	@Override
	public void run() {
		switch (presentState) {
		case INIT_CASE:
			path = new Path(tankDrive, pathPoints, 0.1);
			nextState = State.DRIVE_FORWARD;
			break;
		case DRIVE_FORWARD:
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
		presentState = nextState;
	}
}
