package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

/**
 * Edge gear scoring
 * 
 *
 */
public class EdgeGear extends Autonomous {
	private final double speed = 0.3;
	private boolean right;
	private PiVisionReader vision;
	private Timer timer;
	private Path path;
	private PathPoint[] pathPoints = { new PathPoint(0, 73), new PathPoint(-55 * 0.9, 37 * 0.9, true), };

	/**
	 * Places a gear when not starting in the middle
	 * 
	 * @param tankDrive
	 * @param right
	 *            True if on the right side, false if on the left side
	 */
	public EdgeGear(TankDrive tankDrive, boolean right) {
		super(tankDrive);
		vision = new PiVisionReader();
		this.right = right;
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
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				tankDrive.stop();
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