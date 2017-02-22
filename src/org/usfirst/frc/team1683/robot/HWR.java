package org.usfirst.frc.team1683.robot;

public class HWR {

	// Motors
	public static final int RIGHT_DRIVE_TRAIN_FRONT = HWP.CAN_6;
	public static final int RIGHT_DRIVE_TRAIN_MIDDLE = HWP.CAN_5;
	public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_4;
	public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_7;
	public static final int LEFT_DRIVE_TRAIN_MIDDLE = HWP.CAN_8;
	public static final int LEFT_DRIVE_TRAIN_BACK = HWP.CAN_9;
	//public static final int LIGHT_RING = HWP.CAN_10;
	
	//public static final int AGITATOR = HWP.CAN_2;
	public static final int WINCH1 = HWP.CAN_11;
	public static final int WINCH2 = HWP.CAN_12;
	//public static final int SHOOTER = HWP.CAN_1;
	public static final int INTAKE = HWP.CAN_10;
	
	public static final int GREEN_LIGHT_LOW = HWP.CAN_3; 
	// Enable low level green light for cameras (Talon 9)
	
	//encoders
	public static final int LEFT_DRIVE_ENCODER = HWP.CAN_7;
	public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_6;
	public static final int SHOOTER_ENCODER = HWP.CAN_9;

	// Joysticks
	public static final int LEFT_JOYSTICK = HWP.JOY_0;
	public static final int RIGHT_JOYSTICK = HWP.JOY_1;
	public static final int AUX_JOYSTICK = HWP.JOY_2;

	// Sensors
	public static final int GYRO = HWP.ANALOG_1;
	//public static final int CLIMB_SWITCH = HWP.DIO_1;
	//public static final int ULTRASONIC = HWP.ANALOG_2;
	//right joystick buttons
	public static final int BACK_CONTROL = HWP.BUTTON_2;
	public static final int FRONT_CONTROL = HWP.BUTTON_3;
	
	//public static final int SPIN_SHOOTER = HWP.BUTTON_1;
	//public static final int TOGGLE_SHOOTER_MODE = HWP.BUTTON_8;
	
	public static final int TOGGLE_BACK_WINCH = HWP.BUTTON_11;
	public static final int TOGGLE_WINCH = HWP.BUTTON_3;
	
	//public static final int TURN_AGITATOR = HWP.BUTTON_10;
	
	public static final int TURN_INTAKE = HWP.BUTTON_2;
	
	//drive
	public static final int FULL_POWER = HWP.BUTTON_5;
	public static final int SECOND_POWER = HWP.BUTTON_4;
	public static final int ADD_POWER = HWP.BUTTON_5;
	public static final int SUBTRACT_POWER = HWP.BUTTON_4;
	public static final int TOGGLE_VISION_AID = HWP.BUTTON_3;
}
