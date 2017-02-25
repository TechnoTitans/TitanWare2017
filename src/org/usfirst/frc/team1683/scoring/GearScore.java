package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.robot.PIDLoop;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class GearScore {
	private PiVisionReader vision;

	private double errorKP;
	private double speedKP;

	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;

	private boolean done;

	PIDLoop drive;
	private double speed;

	double offset;
	double setPoint;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader vision) {
		this.vision = vision;
		this.speed = speed;

		offset = 0.0;

		errorKP = 2.3;
		speedKP = 1.3;
		setPoint = 0.0;

		SmartDashboard.prefDouble("errorkp", errorKP);
		SmartDashboard.prefDouble("speedkp", speedKP);

		drive = new PIDLoop(2.3, 0, 0, driveTrain, 0.3);

	}

	public void run() {
		errorKP = SmartDashboard.getDouble("errorkp");
		speedKP = SmartDashboard.getDouble("speedkp");

		vision.update();

		// offset = vision.getOffset(); // between -0.5 and 0.5
		double confidence = 1.0;// vision.getConfidence();
		SmartDashboard.sendData("GearScore offset", offset);

		// testing
		if (DriverStation.leftStick.getRawButton(HWR.ADD_POWER))
			offset += 0.01;
		else if (DriverStation.leftStick.getRawButton(HWR.SUBTRACT_POWER))
			offset -= 0.01;

		if (confidence > CONFIDENCE_CUTOFF) {
			SmartDashboard.sendData("Vision Aided is:", "working");

			drive.setInput(offset);

			drive.setSetpoint(setPoint);
		} else {
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}
	}

	public void disable() {
		drive.stopPID();
	}

	public void enable() {
		drive.enablePID();
	}

	public void changeSpeed() {
		if (vision.getDistanceTarget() < DISTANCE_STOP) {
			speed = 0;
			return;
		}
		this.speed = vision.getDistanceTarget() * speedKP;
	}

	public boolean isDone() {
		return done;
	}
}
