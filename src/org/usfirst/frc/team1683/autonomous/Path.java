package org.usfirst.frc.team1683.autonomous;


import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;

public class Path {
	private PathPoint[] path;
	private double currentHeading = 90;
	private DriveTrainMover mover;
	private DriveTrainTurner turner;
	private DriveTrain driveTrain;
	private boolean isTurning = true;
	private int pathIndex = 0;
	private double speed;
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed) {
		this(driveTrain, path, speed, 0);
	}
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed, double currentHeading) {
		this.driveTrain = driveTrain;
		this.path = path;
		this.currentHeading = currentHeading;
		if (path.length > 0) {
			mover = new DriveTrainMover(driveTrain, path[0].getDistance(), speed);
			turner = new DriveTrainTurner(driveTrain, path[0].getAngle() - currentHeading, speed);
		}
		this.speed = speed;
	}
	public boolean isDone() {
		return pathIndex >= path.length;
	}
	public void run() {
		if (isDone()) {
			return;
		}
		if (isTurning) {
			turner.run();
			if (turner.isDone()) {
				isTurning = false;
				currentHeading = path[pathIndex].getAngle();
			}
		} else {
			mover.runIteration();
			if (mover.areAnyFinished()) {
				pathIndex++;
				if (!isDone()) {
					mover = new DriveTrainMover(driveTrain, path[pathIndex].getDistance(), speed);
					turner = new DriveTrainTurner(driveTrain, path[pathIndex].getAngle() - currentHeading, speed);
					isTurning = true;
				}
			}
		}
	}
}
