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
	private final double ERROR_KP = 0.02;
	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;
	private double speed;
	private boolean done;

	private double lp;
	private double li;
	private double ld;

	private double rp;
	private double ri;
	private double rd;

	ArrayList<Motor> leftMotors;
	ArrayList<Motor> rightMotors;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader piReader) {
		this.piReader = piReader;
		this.driveTrain = driveTrain;
		vision = new PiVisionReader();
		this.speed = speed;

		// PID
		SmartDashboard.prefDouble("LP", lp);
		SmartDashboard.prefDouble("LI", li);
		SmartDashboard.prefDouble("LD", ld);

		SmartDashboard.prefDouble("RP", rp);
		SmartDashboard.prefDouble("RI", ri);
		SmartDashboard.prefDouble("RD", rd);

		leftMotors = driveTrain.getLeftGroup().getMotor();
		rightMotors = driveTrain.getLeftGroup().getMotor();
		
		startPID();
	}

	public void run() {
		vision.update();
		vision.log();
		double offset = vision.getOffset(); // between -0.5 and 0.5
		SmartDashboard.sendData("GearScore offset", offset);
		if (vision.getConfidence() > CONFIDENCE_CUTOFF) {
			if (vision.getDistanceTarget() < DISTANCE_STOP) {
				done = true;
			} else {
				driveTrain.set(speed * (1 - offset * ERROR_KP), speed * (1 + offset * ERROR_KP));
			}
			SmartDashboard.sendData("Vision Aided is:", "working");
		} else {
			driveTrain.stop();
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}
		
		// PID
		updatePID();
		setPID();
				
	}

	public void startPID() {
		for (Motor motor : leftMotors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).PIDInit();
			}
		}

		ArrayList<Motor> rightMotors = driveTrain.getLeftGroup().getMotor();
		for (Motor motor : rightMotors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).PIDInit();
			}
		}
	}
	
	public void setPID() {
		for (Motor motor : leftMotors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).setP(lp);
				((TalonSRX) motor).setP(li);
				((TalonSRX) motor).setP(ld);
				((TalonSRX) motor).setSetPoint(piReader.getOffset());
			}
		}
		for (Motor motor : rightMotors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).setP(rp);
				((TalonSRX) motor).setP(ri);
				((TalonSRX) motor).setP(rd);
			}
		}
	}
	
	public void moveLeft() {
		for (Motor motor : leftMotors) {
			if (motor instanceof TalonSRX) {
				
				((TalonSRX) motor).PIDPosition(0);
			}
		}
	}
	public void updatePID() {
		lp = SmartDashboard.getDouble("LP");
		li = SmartDashboard.getDouble("LI");
		ld = SmartDashboard.getDouble("LD");

		rp = SmartDashboard.getDouble("RP");
		ri = SmartDashboard.getDouble("RI");
		rd = SmartDashboard.getDouble("RD");
	}

	public boolean isDone() {
		return done;
	}
}
