
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.autonomous.Dance;
import org.usfirst.frc.team1683.autonomous.EdgeGearScore;
import org.usfirst.frc.team1683.autonomous.MiddleGear;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.CurvedDrive;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.AnalogUltra;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.test.GyroTester;
import org.usfirst.frc.team1683.vision.PiVisionReader;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class TechnoTitan extends IterativeRobot {
	public static AutonomousSwitcher switcher;
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.2135;
	public static final double AGITATOR_SPEED = 1.00;

	TankDrive drive;
	Timer endGameTimer;
	// LightRing lightRing;
	Autonomous auto;
	Solenoid solenoid;
	GyroTester gyroTester;
	Gyro gyro;

	MotorGroup leftGroup;
	MotorGroup rightGroup;

	AnalogUltra ultrasonic;
	// SolenoidTest solenoidTest;
	Controls controls;
	PiVisionReader vision;

	CurvedDrive curvedDrive;

	@Override
	public void robotInit() {
		ultrasonic = new AnalogUltra(HWR.ULTRASONIC);

		new TalonSRX(HWR.AGITATOR, true).set(AGITATOR_SPEED);

		gyro = new Gyro(HWR.GYRO);

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
		controls = new Controls(drive);

		endGameTimer = new Timer();
		leftGroup.enableAntiDrift(left);
		rightGroup.enableAntiDrift(right);

		curvedDrive = new CurvedDrive(drive, gyro);
		// BuiltInAccel accel = new BuiltInAccel();
		// lightRing = new LightRing(HWR.LIGHT_RING);
		// lightRing.set(1);

		// switcher = new AutonomousSwitcher(drive, accel);

		// CameraServer server = CameraServer.getInstance();
		// server.setQuality(50);
		// server.startAutomaticCapture("cam1");

		vision = new PiVisionReader();
	}

	@Override
	public void autonomousInit() {
		gyro.reset();
		auto = new EdgeGearScore(drive, false);
	}

	@Override
	public void autonomousPeriodic() {
		SmartDashboard.sendData("Gyro Angle", gyro.getRaw());
		SmartDashboard.sendData("Robot time in milliseconds", vision.getTime() * 1000);
		auto.run();
	}

	@Override
	public void teleopInit() {
		drive.stop();
		endGameTimer.start();

	}

	@Override
	public void teleopPeriodic() {
		// drive.driveMode();
		controls.run();
		// drive.driveMode();
		controls.run();
		// curvedDrive.run();
		SmartDashboard.sendData("Ultrasonic", ultrasonic.getDistance());
		SmartDashboard.sendData("Gyro Angle", gyro.getRaw());

	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

}
