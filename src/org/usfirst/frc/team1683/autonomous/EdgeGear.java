package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

/**
 * Edge gear scoring
 *
 */
public class EdgeGear extends Autonomous {
	GearScore gearScore;
	PiVisionReader piReader;

	private Path path;
	private DriveTrainMover mover;

	private Timer timer;
	private Timer waitTimer;

	private PathPoint[] pathPoint1 = { new PathPoint(0, 73), new PathPoint(-55 * 0.0001, 37 * 0.0001, true), };
	private PathPoint[] pathPoints;
	// private PathPoint[] pathPointsLeft = { new PathPoint(0, -12), new
	// PathPoint(-90, 37, true), };
	// private PathPoint[] pathPointsRight = { new PathPoint(0, -12), new
	// PathPoint(90, 0, true), };

	public EdgeGear(TankDrive tankDrive, boolean right, boolean wide, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;

		timer = new Timer();
		waitTimer = new Timer();
		
		if(!wide)
			pathPoints = pathPoint1;
		
		if (right) {
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
				path = new Path(tankDrive, pathPoints, 0.2);
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone() || timer.get() > 6) {
					tankDrive.stop();
					nextState = State.APPROACH_GOAL;
					gearScore = new GearScore(tankDrive, 0.2, piReader, 0.74, 0.0, 0, "edge");
				}
				break;
			case APPROACH_GOAL:
				gearScore.run();
				gearScore.enable();
				if (gearScore.isDone()) {
					waitTimer.start();
					gearScore.disable();
					tankDrive.stop();
					nextState = State.WAIT;
				}
				break;
			case WAIT:
				tankDrive.stop();
				if (waitTimer.get() > 0.1) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, -1, 0.3);
					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.END_CASE;
				}
				break;
			case HEAD_TO_LOADING:
				path.run();
				if (path.isDone()) {
					tankDrive.stop();
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Auto Timer", timer.get(), true);
		SmartDashboard.sendData("Auto State", presentState.toString(), true);
		presentState = nextState;
	}
}