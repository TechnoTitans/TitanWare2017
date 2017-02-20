package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class GearScore {
	private DriveTrain driveTrain;
	private PiVisionReader vision;
	private final double ERROR_KP = 0.02;
	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;
	private double speed;
	private boolean done;

	public GearScore(DriveTrain driveTrain, double speed) {
		this.driveTrain = driveTrain;
		vision = new PiVisionReader();
		this.speed = speed;
	}

	public void run() {
		vision.update();
		vision.log();
		double offset = vision.getOffset(); // between -0.5 and 0.5
		SmartDashboard.sendData("error offset", offset);
		if (vision.getConfidence() > CONFIDENCE_CUTOFF) {
			if (vision.getDistanceTarget() < DISTANCE_STOP) {
				done = true;
			} else {
				driveTrain.set(speed * (1 - offset * ERROR_KP), speed * (1 + offset * ERROR_KP));
			}
			SmartDashboard.sendData("Vision Aided is:", "working");
		} else{
			driveTrain.stop();
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}
	}

	public boolean isDone() {
		return done;
	}
}
