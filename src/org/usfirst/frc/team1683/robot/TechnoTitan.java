
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LimitSwitch;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * Main class
 *
 */
public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.0356;

	TankDrive drive;
	Controls controls;

	Timer waitTeleop;
	Timer waitAuto;

	PiVisionReader piReader;
	CameraServer server;

	Autonomous auto;
	AutonomousSwitcher autoSwitch;
	LimitSwitch limitSwitch;

	LightRing lightRing;
	Gyro gyro;
	BuiltInAccel accel;
	Winch winch;

	MotorGroup leftGroup;
	MotorGroup rightGroup;

	boolean teleopReady = false;

	@Override
	public void robotInit() {
		waitTeleop = new Timer();
		waitAuto = new Timer();

		gyro = new Gyro(HWR.GYRO);
		limitSwitch = new LimitSwitch(HWR.LIMIT_SWITCH);
		accel = new BuiltInAccel();
		piReader = new PiVisionReader();

		AntiDrift left = new AntiDrift(gyro, -1);
		AntiDrift right = new AntiDrift(gyro, 1);
		TalonSRX leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE, left);
		TalonSRX rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT, RIGHT_REVERSE, right);
		leftGroup = new MotorGroup(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS), leftETalonSRX,
				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK, LEFT_REVERSE),
				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE));
		rightGroup = new MotorGroup(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS), rightETalonSRX,
				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE),
				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE));
		drive = new TankDrive(leftGroup, rightGroup, gyro);
		leftGroup.enableAntiDrift(left);
		rightGroup.enableAntiDrift(right);

		autoSwitch = new AutonomousSwitcher(drive, piReader, limitSwitch);
		
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		controls = new Controls(drive, lightRing, piReader, winch);
		CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
		waitAuto.reset();
		waitAuto.start();
		
		SmartDashboard.sendData("Autorasp_side", DriverStation.getInstance().getLocation(), true);
		String color = "blue";
		if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red)
			color = "red";
		SmartDashboard.sendData("Autorasp_alli", color, true);
		drive.stop();
		autoSwitch.getSelected();
		gyro.reset();
	}

	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.sendData("Wait Auto Timer", waitAuto.get(), false);
		SmartDashboard.sendData("AutoGyro", gyro.getAngle(), true);
		if (waitAuto.get() > 0.2)
			autoSwitch.run();
	} 

	@Override
	public void teleopInit() {
		try{
			winch = new Winch(HWR.WINCH1, HWR.WINCH2);
			System.out.print("Initializeing winc h");
			
		}
		catch(Exception e){
			
		}
		waitTeleop.reset();
		waitTeleop.start();
			
		drive.stop();
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.sendData("Wait Teleop Timer", waitTeleop.get(), false);
		if (waitTeleop.get() > 0.2 || DriverSetup.rightStick.getRawButton(HWR.OVERRIDE_TIMER))
			teleopReady = true;
		if (teleopReady)
			controls.run();
		SmartDashboard.sendData("LimitSwitch", limitSwitch.isPressed(), true);
		SmartDashboard.sendData("Gyro", gyro.getAngle(), true);
		SmartDashboard.sendData("Competition Time", DriverStation.getInstance().isFMSAttached(), true);
		SmartDashboard.sendData("Accelerometer", accel.getX(), true);
		try{
			winch = new Winch(HWR.WINCH1, HWR.WINCH2);
			System.out.print("Initializeing winc h");
			
		}
		catch(Exception e){
			
		}
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}
