package org.usfirst.frc.team1683.scoring;

import java.util.ArrayList;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class GearScore {
	private DriveTrain driveTrain;
	private PiVisionReader piReader;
	private PiVisionReader vision;
	private double errorKP;// 0.2;
	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;
	private double speed;
	private boolean done;

	private double p;
	private double i;
	private double d;

	ArrayList<Motor> leftMotors;
	ArrayList<Motor> rightMotors;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader piReader) {
		this.piReader = piReader;
		this.driveTrain = driveTrain;
		vision = new PiVisionReader();
		this.speed = speed;

		errorKP = 4;
		// PID
		SmartDashboard.prefDouble("P", p);
		SmartDashboard.prefDouble("I", i);
		SmartDashboard.prefDouble("D", d);
		
		SmartDashboard.prefDouble("errorkp", errorKP);

		leftMotors = driveTrain.getLeftGroup().getMotor();
		rightMotors = driveTrain.getLeftGroup().getMotor();

	}

	public void run() {
		errorKP = SmartDashboard.getDouble("errorkp");
		
		vision.update();
		vision.log();
		double offset = vision.getOffset(); // between -0.5 and 0.5
		SmartDashboard.sendData("GearScore offset", offset);
		if (vision.getConfidence() > CONFIDENCE_CUTOFF) {
			SmartDashboard.sendData("left speed vision", speed * (1 - offset * errorKP));
			SmartDashboard.sendData("right speed vision", speed * (1 + offset * errorKP));
			driveTrain.setLeft(speed * (1 - offset * errorKP));
			driveTrain.setRight(speed * (1 + offset * errorKP));
			SmartDashboard.sendData("Vision Aided is:", "working");
		} else {
			driveTrain.stop();
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}

	}
	public void updatePID() {
		p = SmartDashboard.getDouble("P");
		i = SmartDashboard.getDouble("I");
		d = SmartDashboard.getDouble("D");
	}

	public boolean isDone() {
		return done;
	}
}
