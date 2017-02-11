package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Controls {
	
	public static boolean[][] lasts = new boolean[3][11];
	
	DriveTrain drive;
	Winch winch;
	LimitSwitch limitSwitch;
	boolean frontMode;
	boolean toggleWinch;
	double rSpeed;
	double lSpeed;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		winch = new Winch(HWR.WINCH);
		limitSwitch = new LimitSwitch(HWR.CLIMB_SWITCH);
		frontMode = true;
		toggleWinch = false;
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

		// if (DriverStation.auxStick.getRawButton(HWR.SPIN_SHOOTER)) {
		// winch.turnWinch();
		// } else
		// winch.stop();
		if (checkToggle(HWR.AUX_JOYSTICK,HWR.TOGGLE_WINCH)) {
			toggleWinch = !toggleWinch;
		}
		
		SmartDashboard.sendData("togglewinchtest", toggleWinch);
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
