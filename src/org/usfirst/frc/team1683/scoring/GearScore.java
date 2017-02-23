package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
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

	PIDLoop leftDrive;
	PIDLoop rightDrive;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader piReader) {
		this.piReader = piReader;
		this.driveTrain = driveTrain;
		vision = new PiVisionReader();
		this.speed = speed;

		errorKP = 2.3;

		// PID
		SmartDashboard.prefDouble("errorkp", errorKP);

		leftDrive = new PIDLoop(errorKP, 0, 0, driveTrain.getLeftGroup());
		rightDrive = new PIDLoop(-errorKP, 0, 0, driveTrain.getRightGroup());

		leftDrive.enable();
		rightDrive.enable();
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

			// PID
			leftDrive.setInput(piReader.getOffset());
			rightDrive.setInput(piReader.getOffset());
			
			leftDrive.setSetpoint(0);
			rightDrive.setSetpoint(0);
			
			SmartDashboard.sendData("PID Left", leftDrive.getPIDPosition());
			SmartDashboard.sendData("PID Right", rightDrive.getPIDPosition());
		} else {
			driveTrain.stop();
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}

	}

	public boolean isDone() {
		return done;
	}
}
