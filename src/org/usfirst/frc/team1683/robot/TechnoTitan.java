
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.DriveStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.2135;

	TankDrive drive;
	Controls controls;

	Timer waitTeleop;
	Timer waitAuto;
	Timer brownProtection;
	boolean brownTripped = false;

	PiVisionReader piReader;
	CameraServer server;

	Autonomous auto;
	AutonomousSwitcher autoSwitch;

	LightRing lightRing;
	Gyro gyro;

	MotorGroup leftGroup;
	MotorGroup rightGroup;

	boolean teleopReady = false;

	// TODO Make sure to change this value during competition
	public static final boolean isCompetitionTime = false;

	@Override
	public void robotInit() {
		waitTeleop = new Timer();
		waitAuto = new Timer();
		brownProtection = new Timer();

		gyro = new Gyro(HWR.GYRO);

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

		autoSwitch = new AutonomousSwitcher(drive, piReader);

		controls = new Controls(drive, lightRing, piReader);
	}

	@Override
	public void autonomousInit() {
		waitAuto.reset();
		waitAuto.start();

		drive.stop();
		autoSwitch.getSelected();
		gyro.reset();
	}

	@Override
	public void autonomousPeriodic() {
		SmartDashboard.sendData("Wait Auto Timer", waitAuto.get(), false);
		if (waitAuto.get() > 0.2)
			autoSwitch.run();
	}

	@Override
	public void teleopInit() {
		waitTeleop.reset();
		waitTeleop.start();

		drive.stop();
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.sendData("Voltage", DriverStation.getInstance().getBatteryVoltage(), false);
		if (!brownTripped && DriverStation.getInstance().getBatteryVoltage() < 7.2) {
			SmartDashboard.sendData("BrownOut Protection:", "triggered", true);
			brownProtection.reset();
			brownProtection.start();
			brownTripped = true;
		}

		if (brownProtection.get() > 2 || DriveStation.rightStick.getRawButton(HWR.OVERRIDE_BROWN))
			brownTripped = false;

		if (!brownTripped) {
			SmartDashboard.sendData("Wait Teleop Timer", waitTeleop.get(), false);
			if (waitTeleop.get() > 0.2 || DriveStation.rightStick.getRawButton(HWR.OVERRIDE_TIMER))
				teleopReady = true;
			if (teleopReady)
				controls.run();
			brownProtection.reset();
			SmartDashboard.sendData("BrownOut Protection:", "N/A", true);
		} else {
			drive.coast();
		}
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}
