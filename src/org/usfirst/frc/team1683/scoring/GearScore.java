package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.MotorMover;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.robot.PIDLoop;
import org.usfirst.frc.team1683.vision.LightRing;
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
	private double brightKP;

	private final double DISTANCE_STOP = 2;
	private final double CONFIDENCE_CUTOFF = 0.3;

	PIDLoop drive;
	private double p;
	private double i;
	private double d;
	
	private DriveTrainMover mover;
	private DriveTrain driveTrain;

	private double speed;
	private double lastdistance;

	public GearScore(DriveTrain driveTrain, double speed, PiVisionReader vision) {
		this.vision = vision;
		this.speed = speed;
		this.driveTrain = driveTrain;

		p = 1.8;
		i = 0;
		d = 0;
		
		errorKP = 2.3;
		speedKP = 1.3;
		SmartDashboard.prefDouble("errorkp", errorKP);
		SmartDashboard.prefDouble("speedkp", speedKP);
		
		SmartDashboard.prefDouble("gearspeed", speed);

		drive = new PIDLoop(p, i, d, driveTrain, speed);
		SmartDashboard.prefDouble("ap", p);
		SmartDashboard.prefDouble("ai", i);
		SmartDashboard.prefDouble("ad", d);
	}

	public void run() {
		errorKP = SmartDashboard.getDouble("errorkp");
		speedKP = SmartDashboard.getDouble("speedkp");
		brightKP = SmartDashboard.getDouble("brightkp");
		
		speed = SmartDashboard.getDouble("gearspeed");
		drive.setSpeed(speed);

		vision.update();

		double offset = vision.getOffset(); // between -0.5 and 0.5
		double confidence = vision.getConfidence();
		SmartDashboard.sendData("GearScore offset", offset);
		SmartDashboard.sendData("GearScore confidence", confidence);

		if (DriverStation.leftStick.getRawButton(2)) {
			setPID();
		}
		
//		SmartDashboard.sendData("Gear Speed", speed);
//		drive.setSpeed(speed);
//		changeBasedDistance();

		if (confidence > CONFIDENCE_CUTOFF) {
			SmartDashboard.sendData("Vision Aided is:", "working");
			drive.setInput(offset);
			drive.setSetpoint(0);
			lastdistance = vision.getDistanceTarget();
			SmartDashboard.sendData("lastdistance1", lastdistance);
		} else {
			SmartDashboard.sendData("Vision Aided is:", "not seeing target");
			drive.stopPID();
			SmartDashboard.sendData("lastdistance", lastdistance);
			if(mover == null){
				driveTrain.stop();
				mover = new DriveTrainMover(driveTrain, lastdistance, 0.5);
			}
			if(!mover.areAnyFinished()){
				mover.runIteration();
			}
			else{
				driveTrain.stop();
			}
			
		}
	}

	public void setPID() {
		p = SmartDashboard.getDouble("ap");
		i = SmartDashboard.getDouble("ai");
		d = SmartDashboard.getDouble("ad");
		drive.setPID(p, i, d);
	}

	public void disable() {
		drive.stopPID();
	}

	public void enable() {
		drive.enablePID();
	}

//	public void changeBasedDistance() {
//		if (vision.getDistanceTarget() < DISTANCE_STOP) {
//			speed = 0;
//			return;
//		}
//		this.speed = vision.getDistanceTarget() * speedKP;
//	}
}
