
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.EdgeGear;
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
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.2135;
	public static final double AGITATOR_SPEED = 1.00;

	TankDrive drive;
	Timer endGameTimer;
	Timer timer;
	// LightRing lightRing;
	Autonomous auto;
	LightRing lightRing;
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

		lightRing = new LightRing(HWR.GREEN_LIGHT_LOW);
		vision = new PiVisionReader();
	}

	@Override
	public void autonomousInit() {
		gyro.reset();
		auto = new EdgeGear(drive, false);
	}

	@Override
	public void autonomousPeriodic() {
		//curvedDrive.run();

		SmartDashboard.sendData("Gyro Angle", gyro.getRaw());
		//auto.run();
		curvedDrive.run();
	}

	@Override
	public void teleopInit() {
		timer = new Timer();
		drive.stop();
		endGameTimer.start();
	}

	@Override
	public void teleopPeriodic() {
		controls.run();
		//curvedDrive.run();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

}
