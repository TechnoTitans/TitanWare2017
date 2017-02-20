package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;

public class SquareAuto extends Autonomous {
	private PathPoint[] points = {
			new PathPoint(0, 50),
			new PathPoint(50, 0),
			new PathPoint(0, -50),
			new PathPoint(-50, 0)
	};
	private Path path;
	public SquareAuto(TankDrive tankDrive) {
		super(tankDrive);
		path = new Path(tankDrive, points, 0.3);
	}
	public void run() {
		if (!path.isDone()) {
			path.run();
		}
	}
}
