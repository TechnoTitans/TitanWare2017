package org.usfirst.frc.team1683.autonomous;

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

	private final double speed = 0.2;
	private boolean right;
	private Timer timer;
	private Path path;
	private PathPoint[] pathPoints = { new PathPoint(0, 73), new PathPoint(-55 * 0.01, 37 * 0.01, true), };

	/**
	 * Places a gear when not starting in the middle
	 * 
	 * @param tankDrive
	 * @param right
	 *            True if on the right side, false if on the left side
	 */
	public EdgeGear(TankDrive tankDrive, boolean right, PiVisionReader piReader) {
		super(tankDrive);
		this.right = right;
		this.piReader = piReader;

		timer = new Timer();
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
				// driveTrainMover = new DriveTrainMover(tankDrive, distance,
				// speed);
				path = new Path(tankDrive, pathPoints, speed);
				int ind = 0;
				for (PathPoint point : pathPoints) {
					SmartDashboard.sendData("point values " + ind, point.toString());
					++ind;
				}
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone()) {
					tankDrive.stop();
					nextState = State.FIND_TARGET;
					gearScore = new GearScore(tankDrive, 0.3, piReader, 1.7, 0.0001, 0, "edge");
				}
				break;
			case FIND_TARGET:
				gearScore.enable();
				gearScore.run();
				break;
			case END_CASE:
				tankDrive.stop();
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("timer", timer.get());
		SmartDashboard.sendData("Auto State", presentState.toString());
		presentState = nextState;
	}
}