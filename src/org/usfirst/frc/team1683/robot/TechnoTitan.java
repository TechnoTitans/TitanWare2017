
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.2135;

	TankDrive drive;
	Controls controls;

	PiVisionReader piReader;
	CameraServer server;

	Autonomous auto;
	AutonomousSwitcher autoSwitch;

	LightRing lightRing;
	Gyro gyro;

	MotorGroup leftGroup;
	MotorGroup rightGroup;

	Timer endGameTimer;

	// TODO Make sure to change this value during competition
	public static final boolean isCompetitionTime = false;

	@Override
	public void robotInit() {

		endGameTimer = new Timer();
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

		server = CameraServer.getInstance();
		server.startAutomaticCapture();

	}

	@Override
	public void autonomousInit() {
		autoSwitch.getSelected();
		gyro.reset();
	}

	@Override
	public void autonomousPeriodic() {
		autoSwitch.run();
	}

	@Override
	public void teleopInit() {
		drive.stop();
		endGameTimer.start();
	}

	@Override
	public void teleopPeriodic() {
		controls.run();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

}
