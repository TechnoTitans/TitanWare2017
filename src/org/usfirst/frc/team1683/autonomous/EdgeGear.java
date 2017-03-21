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
 * 
 * Edge gear scoring autonomous (scores on the edge) Important! Be careful
 * 
 * @author Yi Liu
 *
 */
public class EdgeGear extends Autonomous {
	GearScore gearScore;
	PiVisionReader piReader;

	private Path path;
	private DriveTrainMover mover;

	private Timer timer;
	private Timer timer2;
	private Timer timer3;
	private Timer waitTimer;
	private boolean shakeRight = false;

	// Path for normal edge gear autonomous
	private PathPoint[] pathPoint1 = { new PathPoint(0, 73), new PathPoint(-55 * 0.1, 37 * 0.1, true), };
	// Path for "wide" autonomous
	private PathPoint[] pathPoint2 = { new PathPoint(0, 52), new PathPoint(-81 * 0.1, 55 * 0.1, true), };

	// Path that is ran
	// Check next comment for more info
	private PathPoint[] pathPoints;

	public EdgeGear(TankDrive tankDrive, boolean right, boolean wide, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;

		timer = new Timer();
		timer2 = new Timer();
		timer3 = new Timer();
		waitTimer = new Timer();

		// Sets pathPoint depending on if it is wide or normal
		if (!wide)
			pathPoints = pathPoint1;
		else
			pathPoints = pathPoint2;

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
				path = new Path(tankDrive, pathPoints, 0.6, 0.3);
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone() || timer.get() > 6) {
					tankDrive.stop();
					nextState = State.APPROACH_GOAL;
					gearScore = new GearScore(tankDrive, 0.2, piReader, 0.84, 0.0, 0.0, "edge");
				}
				break;
			case APPROACH_GOAL:
				gearScore.run();
				if (gearScore.isDone() || timer.get() > 12) {
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
					mover = new DriveTrainMover(tankDrive, -2, 0.3);
					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.SHAKE;
					timer2.start();
					timer3.start();
				}
				break;
			case SHAKE:
				if (timer2.get() > 3) {
					nextState = State.END_CASE;
					tankDrive.stop();
				} else {
					tankDrive.turnInPlace(shakeRight, 0.15);
					if (timer3.get() > 0.18) {
						shakeRight = !shakeRight;
						timer3.reset();
					}
				}
				break;
			// Not ready
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