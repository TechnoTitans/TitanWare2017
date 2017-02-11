package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Shooter;
import org.usfirst.frc.team1683.scoring.Winch;

public class Controls {

	public static boolean[][] lasts = new boolean[3][11];

	DriveTrain drive;
	Winch winch;
	Shooter shooter;

	boolean frontMode;
	boolean toggleWinch;
	boolean autoShooter;

	double rSpeed;
	double lSpeed;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		shooter = new Shooter(HWR.SHOOTER);
		winch = new Winch(HWR.WINCH);

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
		rSpeed = (frontMode ? -1 : 1) * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		lSpeed = (frontMode ? -1 : 1) * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
		drive.driveMode(lSpeed, rSpeed);

		SmartDashboard.sendData("Zaxisaux", DriverStation.auxStick.getRawAxis(DriverStation.ZAxis));

		// if (DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER)) {
		// winch.turnWinch();
		// } else
		// winch.stop();
		if (checkToggle(HWR.AUX_JOYSTICK,HWR.TOGGLE_SHOOTER_MODE)) {
			autoShooter = !autoShooter;
		}
		if (autoShooter) {
			if(DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER))
				shooter.turnOn();
			else
				shooter.turnOff();
		} else {
			shooter.setSpeed(-(DriverStation.auxStick.getRawAxis(DriverStation.ZAxis) - 1) / 2);
		}

		if (checkToggle(HWR.AUX_JOYSTICK, HWR.TOGGLE_WINCH)) {
			toggleWinch = !toggleWinch;
		}
		if (toggleWinch)
			winch.turnWinch();
		else
			winch.stop();
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

		if (pressed && !lasts[joystick][button - 1]) {
			lasts[joystick][button - 1] = true;
			return true;
		} else if (pressed && lasts[joystick][button - 1]) {
			return false;
		} else {
			lasts[joystick][button - 1] = false;
			return false;
		}
	}
}
