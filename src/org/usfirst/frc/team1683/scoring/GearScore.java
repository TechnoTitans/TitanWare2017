package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class GearScore {
	private DriveTrain driveTrain;
	private PiVisionReader vision;
	private DriveTrainMover mover;
	private double DIFFERENCE_WEIGHT = 0.05;
	private double ERROR_KP = 0.02;
	private double speed;
	private boolean done;

	public GearScore(DriveTrain driveTrain, double distance, double speed) {
		this.driveTrain = driveTrain;
		vision = new PiVisionReader();
		mover = new DriveTrainMover(driveTrain, distance, speed);
		this.speed = speed;
	}

	public void run() {
		/*
		 * if (done) return; double distanceTarget = vision.getDistanceTarget();
		 * double difference = Math.abs(distanceTarget -
		 * mover.getAverageDistanceLeft());
		 * SmartDashboard.sendData("gear score vision distance",
		 * distanceTarget);
		 * SmartDashboard.sendData("gear score encoder distance",
		 * mover.getAverageDistanceLeft());
		 * SmartDashboard.sendData("gear score distance anomoly", difference);
		 * double confidence = vision.getConfidence() * 1/(1 +
		 * difference*difference*DIFFERENCE_WEIGHT);
		 * SmartDashboard.sendData("gear score confidence", confidence); if
		 * (confidence > 0.6) { if (distanceTarget < 10) speed = 0.2; if
		 * (distanceTarget < 5) { done = true; return; } double error =
		 * vision.getOffset(); SmartDashboard.sendData("gear score offset",
		 * error); driveTrain.set(speed*(1-error*ERROR_KP),
		 * speed*(1+error*ERROR_KP)); } else { mover.runIteration(); if
		 * (mover.areAnyFinished()) { done = true; return; } }
		 */
		vision.update();
		vision.log();
		double offset = vision.getOffset(); // between -0.5 and 0.5
		SmartDashboard.sendData("error offset", offset);
		if (offset == -1) {
			offset = 0;
		}
		driveTrain.set(speed * (1 - offset * ERROR_KP), speed * (1 + offset * ERROR_KP));
	}

	public boolean isDone() {
		return done;
	}
}
