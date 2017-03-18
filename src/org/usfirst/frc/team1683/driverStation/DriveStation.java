package org.usfirst.frc.team1683.driverStation;

import org.usfirst.frc.team1683.robot.HWR;

import edu.wpi.first.wpilibj.Joystick;

public class DriveStation {
	public static final int XAxis = 0;
	public static final int YAxis = 1;
	public static final int ZAxis = 2;
	public static final int dialAxis = 3;
	public static Joystick leftStick = new Joystick(HWR.LEFT_JOYSTICK);
	public static Joystick rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
	public static Joystick auxStick = new Joystick(HWR.AUX_JOYSTICK);
}
