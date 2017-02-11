package org.usfirst.frc.team1683.robot;

public class HWR {

	// Motors
	public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_3;
	public static final int LEFT_DRIVE_TRAIN_MIDDLE = HWP.CAN_2;
	public static final int LEFT_DRIVE_TRAIN_BACK = HWP.CAN_1;
	public static final int RIGHT_DRIVE_TRAIN_FRONT = HWP.CAN_6;
	public static final int RIGHT_DRIVE_TRAIN_MIDDLE = HWP.CAN_5;
	public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_4;
	//public static final int LIGHT_RING = HWP.CAN_10;
	
	public static final int AGITATOR = HWP.CAN_8;
	public static final int WINCH = HWP.CAN_7;
	public static final int SHOOTER = HWP.CAN_9;

	// Encoders

	public static final int LEFT_DRIVE_ENCODER = HWP.CAN_3;
	public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_6;

	// Joysticks
	public static final int LEFT_JOYSTICK = HWP.JOY_0;
	public static final int RIGHT_JOYSTICK = HWP.JOY_1;
	public static final int AUX_JOYSTICK = HWP.JOY_2;

	// Sensors
	public static final int GYRO = HWP.ANALOG_1;
	public static final int CLIMB_SWITCH = HWP.DIO_1;
	// public static final int ACCEL_CHANNEL_X = HWP.ANALOG_2;
	// public static final int ACCEL_CHANNEL_Y = HWP.ANALOG_3;
	// public static final int ClIMB_DEPLOY_CHANNEL = 0;
	// public static final int CLIMB_RETRACT_CHANNEL = 1;

	public static final int ULTRASONIC = HWP.ANALOG_2;
	// Buttons Hp DriverStation
	public static final int BACK_CONTROL = HWP.BUTTON_2;
	public static final int FRONT_CONTROL = HWP.BUTTON_3;
	
	public static final int SPIN_SHOOTER = HWP.BUTTON_1;
	public static final int TOGGLE_SHOOTER_MODE = HWP.BUTTON_8;
	
	
	public static final int SPIN_INTAKE = HWP.BUTTON_2;
	
	public static final int TOGGLE_WINCH = HWP.BUTTON_5;
}
