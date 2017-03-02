package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.PIDLoop;
import org.usfirst.frc.team1683.vision.PiVisionReader;

/**
 * 
 * @author Yi Liu
 *
 */
public class GearScore {
	private PiVisionReader vision;

	private double errorKP;
	private double speedKP;

	private final double CONFIDENCE_CUTOFF = 0.3;

	PIDLoop drive;
	String identifier;

	private DriveTrainMover mover;
	private DriveTrain driveTrain;

	private double speed;
	private double lastdistance;
	private boolean isRunningPID;

	public boolean isDone;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader vision, double p, double i, double d,
			String identifier) {
		this.vision = vision;
		this.speed = speed;
		this.driveTrain = driveTrain;
		this.identifier = identifier;

		isDone = false;
		isRunningPID = true;

		errorKP = 2.3;
		speedKP = 1.3;
		SmartDashboard.prefDouble("errorkp", errorKP);
		SmartDashboard.prefDouble("speedkp", speedKP);
		SmartDashboard.prefDouble("gearspeed", speed);

		drive = new PIDLoop(p, i, d, driveTrain, speed, identifier);
	}

	public void run() {
		vision.update();

		errorKP = SmartDashboard.getDouble("errorkp");
		speedKP = SmartDashboard.getDouble("speedkp");

		speed = SmartDashboard.getDouble("gearspeed");
		drive.setSpeed(speed);

		double offset = vision.getOffset(); // between -0.5 and 0.5
		double confidence = vision.getConfidence();
		double distance = vision.getDistanceTarget();
		SmartDashboard.sendData(identifier + " GearScore offset", offset);
		SmartDashboard.sendData(identifier + " GearScore confidence", confidence);
		SmartDashboard.sendData(identifier + " GearScore distance", distance);

		if (confidence < CONFIDENCE_CUTOFF || distance < 24){
			if(lastdistance == 0.0)
				lastdistance = distance;
			isRunningPID = false;
		}

		if (isRunningPID) {
			SmartDashboard.sendData(identifier + " Vision Aided:", "working");
			drive.setInput(offset);
			drive.setSetpoint(0);
			lastdistance = distance;
			SmartDashboard.sendData(identifier + " Distance", lastdistance);
		} else {
			SmartDashboard.sendData(identifier + " Vision Aided:", "can't see target");
			drive.stopPID();
			SmartDashboard.sendData(identifier + " Last Distance", lastdistance);
			if (mover == null) {
				driveTrain.stop();
				mover = new DriveTrainMover(driveTrain, lastdistance, speed);
			}
			if (mover != null && !mover.areAnyFinished()) {
				mover.runIteration();
			} else {
				driveTrain.stop();
				isDone = true;
			}
		}
	}

	public void disable() {
		drive.stopPID();
		isRunningPID = true;
		mover = null;
	}

	public void enable() {
		drive.enablePID();
	}

	public boolean isDone() {
		return isDone;
	}
	// public void changeBasedDistance() {
	// if (vision.getDistanceTarget() < DISTANCE_STOP) {
	// speed = 0;
	// return;
	// }
	// this.speed = vision.getDistanceTarget() * speedKP;
	// }
}
