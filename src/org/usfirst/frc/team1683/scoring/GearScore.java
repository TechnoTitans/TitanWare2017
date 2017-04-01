package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.PIDLoop;
import org.usfirst.frc.team1683.vision.PiVisionReader;

/**
 * 
 * Gear scoring
 * 
 * Uses vision and antidrift
 * 
 * @author Yi Liu
 *
 */
public class GearScore {
	private PiVisionReader vision;

	private double errorKP = 2.3;

	private final double CONFIDENCE_CUTOFF = 0.3;
	private final double DISTANCE_NOT_SEEN = 100;

	PIDLoop drive;
	String identifier;

	private DriveTrainMover mover;
	private DriveTrain driveTrain;

	private double speed;
	private double lastdistance;
	private boolean isRunningPID = true;
	private boolean isEnabled = true;

	public boolean isDone = false;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader vision, double p, double i, double d,
			String identifier) {
		this.vision = vision;
		this.speed = speed;
		this.driveTrain = driveTrain;
		this.identifier = identifier;

		SmartDashboard.prefDouble("errorkp", errorKP);
		SmartDashboard.prefDouble("gearspeed", speed);

		drive = new PIDLoop(p, i, d, driveTrain, speed, identifier);
	}

	public void run() {
		vision.update();

		errorKP = SmartDashboard.getDouble("errorkp");

		speed = SmartDashboard.getDouble("gearspeed");
		drive.setSpeed(speed);

		double offset = vision.getOffset(); // between -0.5 and 0.5
		double confidence = vision.getConfidence();
		double distance = vision.getDistanceTarget();
		SmartDashboard.sendData(identifier + " GearScore offset", offset, false);
		SmartDashboard.sendData(identifier + " GearScore confidence", confidence, false);
		SmartDashboard.sendData(identifier + " GearScore distance", distance, false);

		if (confidence < CONFIDENCE_CUTOFF || distance < 8) {
			if (lastdistance == 0.0)
				lastdistance = DISTANCE_NOT_SEEN;
			isRunningPID = false;
			SmartDashboard.sendData(identifier + " antidrift:", "conf low or distance less", false);
		}
		if (Math.abs(offset) < 0.02 && distance < 15) {
			isRunningPID = false;
			SmartDashboard.sendData(identifier + " antidrift:", "offset low", false);
		}
		if (isEnabled) {
			if (isRunningPID) {
				SmartDashboard.sendData(identifier + " Vision Aided:", "working", true);
				drive.setInput(offset);
				drive.setTarget(0);
				lastdistance = distance;
				SmartDashboard.sendData(identifier + " Distance", lastdistance, false);
			} else {
				SmartDashboard.sendData(identifier + " Vision Aided:", "run antidrift", false);
				drive.stopPID();
				SmartDashboard.sendData(identifier + " Last Distance", lastdistance, false);
				if (mover == null) {
					driveTrain.stop();
					mover = new DriveTrainMover(driveTrain, lastdistance, speed);
				}
				if (mover != null && !mover.areAnyFinished()) {
					mover.runIteration();
				} else {
					driveTrain.stop();
					isEnabled = false;
					isDone = true;
				}
			}
		}
	}

	public void disable() {
		drive.stopPID();
		mover = null;
		isEnabled = false;
		driveTrain.stop();

	}

	public void enable() {
		drive.enablePID();
		isEnabled = true;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public boolean isDone() {
		return isDone;
	}
}
