
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.FollowPath;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.motor.MotorGroup;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LimitSwitch;
import org.usfirst.frc.team1683.sensors.SimQuadEncoder;
import org.usfirst.frc.team1683.simulation.SimIterativeRobot;
import org.usfirst.frc.team1683.simulation.SimTalon;

/**
 * 
 * Main class
 *
 */
public class TechnoTitan extends SimIterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	// In the simulation, the circumference of the wheels is 1 inch
	public static final double WHEEL_RADIUS = 1 / (2*Math.PI);

	TankDrive drive;

	Autonomous auto;
	LimitSwitch limitSwitch;
	//Gyro gyro;

	MotorGroup leftGroup;
	MotorGroup rightGroup;

	Path path;
	
	boolean teleopReady = false;
	
	Gyro gyro;

	@Override
	public void robotInit() {

		gyro = new Gyro(HWR.GYRO);
		//limitSwitch = new LimitSwitch(HWR.LIMIT_SWITCH);

		AntiDrift left = new AntiDrift(gyro, -1);
		AntiDrift right = new AntiDrift(gyro, 1);
		SimTalon leftETalonSRX = new SimTalon(HWR.LEFT_DRIVE_TRAIN_FRONT);
		SimTalon rightETalonSRX = new SimTalon(HWR.RIGHT_DRIVE_TRAIN_FRONT);
		leftGroup = new MotorGroup(new SimQuadEncoder(leftETalonSRX, WHEEL_RADIUS), leftETalonSRX);
		rightGroup = new MotorGroup(new SimQuadEncoder(rightETalonSRX, WHEEL_RADIUS), rightETalonSRX);
		drive = new TankDrive(leftGroup, rightGroup, gyro);
		leftGroup.enableAntiDrift(left);
		rightGroup.enableAntiDrift(right);
	}
	
	//FollowPath advancedPath;
	
	@Override
	public void autonomousInit() {
		//advancedPath = new FollowPath(drive);
		path = new Path(drive, new PathPoint[] {
				PathPoint.fromAngle(80),
				PathPoint.fromAngle(160),
				PathPoint.fromAngle(240),
				PathPoint.fromAngle(320),
				PathPoint.fromAngle(40),
				PathPoint.fromAngle(120)
		}, 1, 0.5, false, 0);
	}
	
	public void autonomousPeriodic() {
		if (!path.isDone()) {
			path.run();
		} else {
			drive.stop();
		}
	}
}
