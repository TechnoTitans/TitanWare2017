package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

/**
 * Edge ball scoring
 * 
 * @author Yi Liu
 *
 */
public class EdgeBallScore extends Autonomous {
	public final double distance = 10; // guessing distance (inches)
	public final double pixelFromCenter = 10; // pixel (guessing)
	public final double turnSpeed = 3; // degrees
	public final double distanceFromGoal = 3; // degrees
	public final double speed = 5;
	private boolean right;
	private PiVisionReader vision;
	private Timer timer;

	/**
	 * Places a gear when not starting in the middle
	 * 
	 * @param tankDrive
	 * @param right
	 *            -- True if on the right side, false if on the left side
	 */
	public EdgeBallScore(TankDrive tankDrive, boolean right) {
		super(tankDrive);
		vision = new PiVisionReader();
		this.right = right;
		timer = new Timer();
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer.start();
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				
				nextState = State.DRIVE_FORWARD_WAITING;
				break;
			case REALIGN:
				// depends on which procedure to score: using gyro or basing
				// rotation based on offset
				/*
				 * while(vision.isNull() || vision.getOffset() <
				 * pixelFromCenter){ tankDrive.turn(turnSpeed); }
				 */
				tankDrive.turnInPlace(!right, 0.2);
				if (vision.getDistanceTarget() < pixelFromCenter || timer.get() > 3) {
					nextState = State.APPROACH_GOAL;
				}
				break;
			case APPROACH_GOAL:

				while (vision.getDistanceTarget() < distanceFromGoal) {
					tankDrive.set(speed);
				}
				//or
				/*
				 * while(!limitSwitch.isPressed){
				 * 	tankDrive.set(speed);
				 * }
				 */
				tankDrive.stop();
				if (timer.get() > 4)
					nextState = State.SCORE;
			case SCORE:
				//TODO
				//move motor to score
				nextState = State.END_CASE;
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Edge ball state", presentState.toString());
		presentState = nextState;
	}
}