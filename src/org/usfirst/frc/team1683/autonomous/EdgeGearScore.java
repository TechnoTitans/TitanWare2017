package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
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
public class EdgeGearScore extends Autonomous {
	public final double distance = 96; // guessing distance (inches)
	public final double pixelFromCenter = 10; // pixel (guessing)
	public final double turnSpeed = 3; // degrees
	public final double distanceFromGoal = 3; // degrees
	public final double speed = 0.7;
	private boolean right;
	private PiVisionReader vision;
	private Timer timer;
	private MiddleGear moveForward;
	private DriveTrainMover driveTrainMover;
	private Path path;
	private PathPoint[] pathPoints = {
		new PathPoint(0, 96),
		new PathPoint(48, 48, false),
		new PathPoint(0, 0, false)
	};
	/**
	 * Places a gear when not starting in the middle
	 * @param tankDrive
	 * @param right True if on the right side, false if on the left side
	 */
	public EdgeGearScore(TankDrive tankDrive, boolean right) {
		super(tankDrive);
		vision = new PiVisionReader();
		this.right = right;
		timer = new Timer();
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
				//driveTrainMover = new DriveTrainMover(tankDrive, distance, speed);
				path = new Path(tankDrive, pathPoints, 0.5);
				nextState = State.DRIVE_PATH;
				break;
			case DRIVE_PATH:
				path.run();
				if (path.isDone()) {
					nextState = State.END_CASE;
				}
				break;
			case DRIVE_FORWARD:
				driveTrainMover.runIteration();
				SmartDashboard.sendData("encoder average distance", driveTrainMover.getAverageDistanceLeft());
				if (driveTrainMover.areAnyFinished()) {
					nextState = State.REALIGN;
					timer.reset();
				}
				break;
			case REALIGN:
				/*
				 * while(vision.isNull ||
				 * vision.distanceFromCenter<pixelFromCenter){
				 * tankDrive.turn(turnSpeed); }
				 */
				tankDrive.turnInPlace(!right, 0.2);
				if (vision.getDistanceTarget() < pixelFromCenter || timer.get() > 6) {
					nextState = State.APPROACH_GOAL;
					tankDrive.set(0);
					moveForward = new MiddleGear(tankDrive, 20);
				}
				break;
			case APPROACH_GOAL:
				moveForward.run();
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