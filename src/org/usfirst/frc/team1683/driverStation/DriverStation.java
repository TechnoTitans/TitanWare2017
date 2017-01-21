package org.usfirst.frc.team1683.driverStation;

import org.usfirst.frc.team1683.robot.HWR;

import edu.wpi.first.wpilibj.Joystick;

public class DriverStation {

	public static final int XAxis = 0;
	public static final int YAxis = 1;
	public static final int ZAxis = 2;
	public static final int dialAxis = 3;
	public static boolean[][] lasts = new boolean[3][11];
	public static Joystick leftStick = new Joystick(HWR.LEFT_JOYSTICK);
	public static Joystick rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
	public static Joystick auxStick = new Joystick(HWR.AUX_JOYSTICK);

	// TODO
	public static boolean antiBounce(int joystick, int button) {
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
