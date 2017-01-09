package org.usfirst.frc.team1683.robot;

/**
 * HWR - Stands for HardWare References. Each variable references a piece
 * of
 * hardware to a channel or port.
 *
 * @author David Luo
 *
 */
public class HWR {

  // Motors
  public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_4;
  public static final int LEFT_DRIVE_TRAIN_BACK_E = HWP.CAN_5;
  public static final int RIGHT_DRIVE_TRAIN_FRONT_E = HWP.CAN_9; // no
  public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_2;
  public static final int LIGHT_RING = HWP.CAN_1;
  // public static final int ANGLE_MOTOR = HWP.CAN_5;
  public static final int ANGLE_MOTOR = HWP.CAN_8;
  public static final int SHOOTER_LEFT = HWP.CAN_6; // change to can_6
  public static final int SHOOTER_RIGHT = HWP.CAN_7; // change to can_7
  public static final int LINEAR_ACTUATOR = HWP.CAN_3;

  // // Test
  // public static final int TEST_TALON_1 = HWP.CAN_5;
  // public static final int TEST_TALON_2 = HWP.CAN_3;
  // public static final int TEST_ENCODER_1 = HWR.TEST_TALON_1;
  // public static final int TEST_ENCODER_2 = HWR.TEST_TALON_2;
  // public static final int TEST_GYRO = HWP.ANALOG_1;

  // Encoders
  // TODO: Get these values
  public static final int LEFT_DRIVE_ENCODER = HWP.CAN_4;
  public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_2;

  // Joysticks
  public static final int LEFT_JOYSTICK = HWP.JOY_0;
  public static final int RIGHT_JOYSTICK = HWP.JOY_1;
  public static final int AUX_JOYSTICK = HWP.JOY_2;

  // Sensors
  public static final int GYRO = HWP.ANALOG_1;
  // public static final int ACCEL_CHANNEL_X = HWP.ANALOG_2;
  // public static final int ACCEL_CHANNEL_Y = HWP.ANALOG_3;
  public static final int PRESSURE_SENSOR = HWP.ANALOG_0;

  // Compressor
  public static final int COMPRESSOR = 0;
  // TODO: Get these values
  public static final int DEFAULT_MODULE_CHANNEL = 1;
  public static final int SHOOTER_PISTON_CHANNEL = 3;
  // public static final int ANGLE_PISTON_CHANNEL = ;
  public static final int ClIMB_DEPLOY_CHANNEL = 0;
  public static final int CLIMB_RETRACT_CHANNEL = 1;

  // Buttons Hp DriverStation
  public static final int SPIN_UP_INTAKE = HWP.BUTTON_4;
  public static final int SPIN_UP_SHOOTER = HWP.BUTTON_5;
  public static final int SHOOT_BALL = HWP.BUTTON_2;
  public static final int LIFT_HOOK = HWP.BUTTON_4;
  public static final int ROBOT_CHINUP = HWP.BUTTON_1;
  public static final int SWITCH_SHOOTER_MODE =
      HWP.BUTTON_8; // find button

  // //Buttons Dell DriverStation
  // public static final int SPIN_UP_INTAKE = HWP.BUTTON_5;
  // public static final int SPIN_UP_SHOOTER = HWP.BUTTON_4;
  // public static final int SHOOT_BALL = HWP.BUTTON_6;
  // public static final int LIFT_HOOK = HWP.BUTTON_5;
  // public static final int ROBOT_CHINUP = HWP.BUTTON_1;
}
