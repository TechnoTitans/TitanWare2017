package org.usfirst.frc.team1683.robot;

public class HWR {

	// Motors
	public static final int RIGHT_DRIVE_TRAIN_FRONT = HWP.CAN_7;
	public static final int RIGHT_DRIVE_TRAIN_MIDDLE = HWP.CAN_8;
	public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_9;
	public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_6;
	public static final int LEFT_DRIVE_TRAIN_MIDDLE = HWP.CAN_5;
	public static final int LEFT_DRIVE_TRAIN_BACK = HWP.CAN_4;

	public static final int WINCH1 = HWP.CAN_11;
	public static final int WINCH2 = HWP.CAN_12;

	public static final int GREEN_LIGHT_LOW = HWP.CAN_3;

	// encoders
	public static final int LEFT_DRIVE_ENCODER = HWP.CAN_7;
	public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_6;
	public static final int SHOOTER_ENCODER = HWP.CAN_9;

	// Joysticks
	public static final int LEFT_JOYSTICK = HWP.JOY_0;
	public static final int RIGHT_JOYSTICK = HWP.JOY_1;
	public static final int AUX_JOYSTICK = HWP.JOY_2;

	// Sensors
	public static final int GYRO = HWP.ANALOG_1;
	public static final int LIMIT_SWITCH = HWP.ANALOG_0;

	// joystick buttons
	public static final int BACK_CONTROL = HWP.BUTTON_2;
	public static final int FRONT_CONTROL = HWP.BUTTON_3;
	public static final int FULL_POWER = HWP.BUTTON_11;
	public static final int SECOND_POWER = HWP.BUTTON_6;
	public static final int TOGGLE_VISION_AID = HWP.BUTTON_3;

	public static final int TOGGLE_BACK_WINCH = HWP.BUTTON_11;
	public static final int MAIN_WINCH = HWP.BUTTON_3;

	public static final int OVERRIDE_TIMER = HWP.BUTTON_9;
}