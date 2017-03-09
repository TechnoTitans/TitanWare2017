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
	private boolean right;
	private Path path;
	private DriveTrainMover mover;

	private boolean shakeRight = true;
	private Timer timer;
	private Timer timer2;

	private final double SPEED = 0.3;

	private PathPoint[] pathPoints = { new PathPoint(0, 73), new PathPoint(-55, 37, true), };

	public EdgeGear(TankDrive tankDrive, boolean right, PiVisionReader piReader) {
		super(tankDrive);
		this.right = right;
		this.piReader = piReader;

		timer = new Timer();
		timer2 = new Timer();

		if (this.right) {
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
				path = new Path(tankDrive, pathPoints, SPEED);
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone() || timer.get() > 6) {
					tankDrive.stop();
					nextState = State.APPROACH_GOAL;
					gearScore = new GearScore(tankDrive, 0.3, piReader, 1.7, 0.0001, 0, "edge");
					timer2.start();
				}
				break;
			case APPROACH_GOAL:
				gearScore.run();
				gearScore.enable();
				if (gearScore.isDone() || timer.get() > 10) {
					gearScore.disable();
					mover = new DriveTrainMover(tankDrive, -1, 0.3);

					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.SHAKE;
					timer.start();
					timer2.start();
				}
				break;
			case SHAKE:
				if (timer.get() > 14) {
					nextState = State.END_CASE;
					tankDrive.stop();
				} else {
					tankDrive.turnInPlace(shakeRight, 0.15);
					if (timer2.get() > 0.18) {
						shakeRight = !shakeRight;
						timer2.reset();
					}
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