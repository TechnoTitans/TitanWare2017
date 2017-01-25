
package org.usfirst.frc.team1683.robot;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.autonomous.DoNothing;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.test.SolenoidTest;
import org.usfirst.frc.team1683.sensors.PressureReader;
import org.usfirst.frc.team1683.vision.LightRing;
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
	LightRing lightRing;
	DoNothing auto;
	Compressor compressor = new Compressor(1);
	Solenoid solenoid;
		
	//SolenoidTest solenoidTest;

	@Override
	public void robotInit() {

		Gyro gyro = new Gyro(HWR.GYRO);

		TalonSRX leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE);
		TalonSRX rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT_E, RIGHT_REVERSE);

		MotorGroup leftGroup = new MotorGroup(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS), leftETalonSRX,
				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK_E, LEFT_REVERSE));
		MotorGroup rightGroup = new MotorGroup(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS), rightETalonSRX,
				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE));

		drive = new TankDrive(leftGroup, rightGroup, gyro);

		endGameTimer = new Timer();

		BuiltInAccel accel = new BuiltInAccel();
		pressureReader = new PressureReader(HWR.PRESSURE_SENSOR);
		solenoid = new Solenoid(HWR.DEFAULT_MODULE_CHANNEL, HWR.GEAR_PISTON_CHANNEL);
		//solenoidTest = new SolenoidTest();
		
		lightRing = new LightRing(HWR.LIGHT_RING);
		lightRing.set(1);
		
		//switcher = new AutonomousSwitcher(drive, accel);

		// CameraServer server = CameraServer.getInstance();
		// server.setQuality(50);
		// server.startAutomaticCapture("cam1");
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
		auto.run();
	}

	@Override
	public void teleopInit() {
		endGameTimer.start();

	}

	@Override
	public void teleopPeriodic() {
		
		//drive.driveMode();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

}
