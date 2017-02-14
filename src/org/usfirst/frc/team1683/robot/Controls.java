package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Intake;
import org.usfirst.frc.team1683.scoring.ScoringMotor;
import org.usfirst.frc.team1683.scoring.Shooter;
import org.usfirst.frc.team1683.scoring.Winch;

public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];

	DriveTrain drive;
	Winch winch;
	Shooter shooter;
	Intake intake;

	boolean frontMode;
	boolean toggleWinch;
	boolean autoShooter;

	double rSpeed;
	double lSpeed;
	public final double MAX_JOYSTICK_SPEED = 0.8;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		shooter = new Shooter(HWR.SHOOTER);
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		intake = new Intake(HWR.INTAKE);

		frontMode = true;
		toggleWinch = false;
		autoShooter = true;

	}

	public void run() {
		SmartDashboard.sendData("Front(true) or back(false) mode", frontMode);
		if (DriverStation.rightStick.getRawButton(HWR.BACK_CONTROL)) {
			frontMode = false;
		} else if (DriverStation.rightStick.getRawButton(HWR.FRONT_CONTROL)) {
			frontMode = true;
		}
		rSpeed = (frontMode ? -1 : 1) * MAX_JOYSTICK_SPEED * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		lSpeed = (frontMode ? -1 : 1) * MAX_JOYSTICK_SPEED * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
		drive.driveMode(lSpeed, rSpeed);

		SmartDashboard.sendData("Zaxisaux", DriverStation.auxStick.getRawAxis(DriverStation.ZAxis));

		// if (DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER)) {
		// winch.turnWinch();
		// } else
		// winch.stop();

		SmartDashboard.sendData("speed of shooter", shooter.getSpeed());
		if (checkToggle(HWR.AUX_JOYSTICK, HWR.TOGGLE_SHOOTER_MODE)) {
			autoShooter = !autoShooter;
		}
		if (autoShooter) {
			if (DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER))
				shooter.turnOn();
			else
				shooter.stop();
		} else {
			shooter.setSpeed(-(DriverStation.auxStick.getRawAxis(DriverStation.ZAxis) - 1) / 2);
		}

		toggle(HWR.TOGGLE_WINCH, winch);
		toggle(HWR.TOGGLE_INTAKE, intake);
	}

	/*
	 * Checks if a button is pressed to toggle it. Since teleop is periodic,
	 * needs to remember past button state to toggle
	 * 
	 */
	public static void toggle(int button, ScoringMotor motor) {
		if (checkToggle(HWR.AUX_JOYSTICK, button)) {
			toggle[button - 1] = !toggle[button - 1];
		}
		if (toggle[button - 1])
			motor.turnOn();
		else
			motor.stop();
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
