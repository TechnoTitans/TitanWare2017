package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Agitator;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.scoring.Intake;
import org.usfirst.frc.team1683.scoring.ScoringMotor;
import org.usfirst.frc.team1683.scoring.Shooter;
import org.usfirst.frc.team1683.scoring.Winch;

public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];

	DriveTrain drive;
	GearScore gearScore;
	Winch winch;
	Shooter shooter;
	Intake intake;
	Agitator agitator;

	boolean frontMode;
	boolean toggleWinch;
	boolean autoShooter;
	boolean fullPowerMode;
	boolean visionAidedMovement;

	double rSpeed;
	double lSpeed;
	double maxPower;
	public final double MAX_JOYSTICK_SPEED = 1.0;
	public final double SECOND_JOYSTICK_SPEED = 0.8;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		shooter = new Shooter(HWR.SHOOTER);
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		intake = new Intake(HWR.INTAKE);
		agitator = new Agitator(HWR.AGITATOR);
		
		gearScore = new GearScore(drive, 100, 0.4);

		frontMode = true;
		toggleWinch = false;
		autoShooter = true;
		fullPowerMode = false;
		visionAidedMovement = false;

		SmartDashboard.prefDouble("shooterSpeed", 0.6);
		maxPower = 0;
	}

	public void run() {
		// drivetrain
		SmartDashboard.sendData("Front(gear) or back(intake) mode", frontMode ? "winch" : "gear");
		if (DriverStation.rightStick.getRawButton(HWR.BACK_CONTROL)) {
			frontMode = false;
		} else if (DriverStation.rightStick.getRawButton(HWR.FRONT_CONTROL)) {
			frontMode = true;
		}
		
		if (checkToggle(HWR.LEFT_JOYSTICK, HWR.TOGGLE_VISION_AID)) {
			visionAidedMovement = !visionAidedMovement;
		}
		if(!visionAidedMovement){
			if (DriverStation.rightStick.getRawButton(HWR.FULL_POWER))
				maxPower = MAX_JOYSTICK_SPEED;
			else if (DriverStation.rightStick.getRawButton(HWR.SECOND_POWER))
				maxPower = SECOND_JOYSTICK_SPEED;
			else if (DriverStation.leftStick.getRawButton(HWR.ADD_POWER))
				maxPower += 0.01;
			else if (DriverStation.leftStick.getRawButton(HWR.SUBTRACT_POWER))
				maxPower -= 0.01;
			if (maxPower > 1.0)
				maxPower = 1.0;
			SmartDashboard.sendData("current power", maxPower);
			if (frontMode) {
				lSpeed = -maxPower * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
				rSpeed = -maxPower * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
			} else {
				lSpeed = maxPower * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
				rSpeed = maxPower * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
			}
			
			drive.driveMode(Math.pow(lSpeed, 3), Math.pow(rSpeed, 3));
		}
		else{
			gearScore.run();
		}
		
		// shooter
		SmartDashboard.sendData("Zaxisaux", DriverStation.auxStick.getRawAxis(DriverStation.ZAxis));
		if (checkToggle(HWR.AUX_JOYSTICK, HWR.TOGGLE_SHOOTER_MODE)) {
			autoShooter = !autoShooter;
		}
		if (autoShooter) {
			if (DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER))
				shooter.setSpeed(SmartDashboard.getDouble("shooterSpeed"));
			else
				shooter.stop();
		} else {
			shooter.setSpeed(-(DriverStation.auxStick.getRawAxis(DriverStation.ZAxis) - 1) / 2);
		}

		// winch
		if (DriverStation.auxStick.getRawButton(HWR.TURN_WINCH)) {
			winch.turnOn();
		} else if (DriverStation.auxStick.getRawButton(HWR.TURN_BACK_WINCH)) {
			winch.turnOtherWay();
		} else {
			winch.stop();
		}
		SmartDashboard.sendData("winch voltage1", winch.getMotor1().getOutputVoltage());
		SmartDashboard.sendData("winch voltage2", winch.getMotor2().getOutputVoltage());

		//agitator
		if(DriverStation.auxStick.getRawButton(HWR.TURN_AGITATOR)){
			agitator.turnOn();
		}
		else
			agitator.stop();
		
		// intake
		toggle(HWR.TOGGLE_INTAKE, intake);
	}

	/*
	 * 
	 * doge
	 * 
	 * Checks if a button is pressed to toggle it. Since teleop is periodic,
	 * needs to remember past button state to toggle
	 * 
	 */
	public static void toggle(int button, ScoringMotor motor) {
		if (checkToggle(HWR.AUX_JOYSTICK, button)) {
			toggle[button - 1] = !toggle[button - 1];
		}
		if (toggle[button - 1]) {
			motor.turnOn();
			SmartDashboard.sendData("intake clicked", "true");
		} else {
			motor.stop();
			SmartDashboard.sendData("intake clicked", "false");
		}
	}

	public static boolean checkToggle(int joystick, int button) {
		boolean pressed = false;

		switch (joystick) {
			case HWR.AUX_JOYSTICK:
				pressed = DriverStation.auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = DriverStation.rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = DriverStation.leftStick.getRawButton(button);
				break;
			default:
				break;
		}

		if (pressed && !joystickCheckToggle[joystick][button - 1]) {
			joystickCheckToggle[joystick][button - 1] = true;
			return true;
		} else if (pressed && joystickCheckToggle[joystick][button - 1]) {
			return false;
		} else {
			joystickCheckToggle[joystick][button - 1] = false;
			return false;
		}
	}
}
