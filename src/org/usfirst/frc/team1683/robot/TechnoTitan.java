
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.autonomous.PassLine;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.AnalogUltra;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LimitSwitch;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.test.GyroTester;
import org.usfirst.frc.team1683.sensors.PressureReader;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class TechnoTitan extends IterativeRobot {
	public static AutonomousSwitcher switcher;
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2;
	TankDrive drive;
	Timer endGameTimer;
	PressureReader pressureReader;
	// LightRing lightRing;
	Autonomous auto;
	Compressor compressor = new Compressor(1);
	Solenoid solenoid;
	GyroTester gyroTester;
	Gyro gyro;

	MotorGroup leftGroup;
	MotorGroup rightGroup;
	
	LimitSwitch limitSwitch;
	AnalogUltra ultrasonic;
	// SolenoidTest solenoidTest;

	@Override
	public void robotInit() {
		limitSwitch = new LimitSwitch(HWR.SWITCH);
		ultrasonic = new AnalogUltra(HWR.ULTRASONIC);
		
		gyro = new Gyro(HWR.GYRO);

		TalonSRX leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE, new AntiDrift(gyro, -1));
		TalonSRX rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT_E, RIGHT_REVERSE, new AntiDrift(gyro, 1));

		leftGroup = new MotorGroup(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS), leftETalonSRX,
				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK_E, LEFT_REVERSE), new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE));
		rightGroup = new MotorGroup(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS), rightETalonSRX,
				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE), new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE));

		drive = new TankDrive(leftGroup, rightGroup, gyro);

		endGameTimer = new Timer();

		// BuiltInAccel accel = new BuiltInAccel();
		// lightRing = new LightRing(HWR.LIGHT_RING);
		// lightRing.set(1);

		// switcher = new AutonomousSwitcher(drive, accel);

		// CameraServer server = CameraServer.getInstance();
		// server.setQuality(50);
		// server.startAutomaticCapture("cam1");
	}

	@Override
	public void autonomousInit() {
		gyro.reset();
		auto = new PassLine(drive);
		auto.run();
	}

	@Override
	public void autonomousPeriodic() {
		SmartDashboard.sendData("Gyro Angle", gyro.getRaw());
	}

	@Override
	public void teleopInit() {
		endGameTimer.start();

	}

	@Override
	public void teleopPeriodic() {
		drive.driveMode();
		SmartDashboard.sendData("Limit Switch", limitSwitch.isPressed());

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
