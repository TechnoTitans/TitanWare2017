package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.robot.PIDLoop;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class GearScore {
	private DriveTrain driveTrain;
	private PiVisionReader vision;

	private double errorKP;
	private double speedKP;

	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;
	private double speed;

	private boolean done;

	PIDLoop leftDrive;
	//PIDLoop rightDrive;

	double offset;
	double setPoint;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader vision) {
		this.vision = vision;
		this.driveTrain = driveTrain;
		this.speed = speed;
		
		offset = 0.7;
		

		errorKP = 2.3;
		speedKP = 1.3;
		setPoint = 0.5;

		// PID
		SmartDashboard.prefDouble("errorkp", errorKP);
		SmartDashboard.prefDouble("speedkp", speedKP);

		SmartDashboard.prefDouble("setPoint", setPoint);

		 leftDrive = new PIDLoop(0.005, 0, 0, driveTrain.getLeftGroup(), "left");
		 //rightDrive = new PIDLoop(-0.005, 0, 0, driveTrain.getRightGroup(), "right");

	}

	public void run() {
		errorKP = SmartDashboard.getDouble("errorkp");
		speedKP = SmartDashboard.getDouble("speedkp");

		setPoint = SmartDashboard.getDouble("setPoint");

		vision.update();
		vision.log();

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

			SmartDashboard.sendData("left speed vision", speed * (1 - offset * errorKP));
			SmartDashboard.sendData("right speed vision", speed * (1 + offset * errorKP));
			// driveTrain.setLeft(speed * (1 - offset * errorKP));
			// driveTrain.setRight(speed * (1 + offset * errorKP));

			// PID
			leftDrive.setInput(offset);
			//rightDrive.setInput(offset);

			leftDrive.setSetpoint(setPoint);
			//rightDrive.setSetpoint(setPoint);

			SmartDashboard.sendData("PID Left Position", leftDrive.getPIDPosition());
			//SmartDashboard.sendData("PID Right Position", rightDrive.getPIDPosition());
			SmartDashboard.sendData("PID Left Setpoint", leftDrive.getSetpoint());
			//SmartDashboard.sendData("PID Right Setpoint", rightDrive.getSetpoint());
		} else {
			driveTrain.stop();
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
		}
	}
	
	public void disable(){
		leftDrive.disable();
		//rightDrive.disable();
	}
	
	public void enable(){
		leftDrive.enable();
		//rightDrive.enable();
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
