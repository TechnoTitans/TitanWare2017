package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.Encoder;

import edu.wpi.first.wpilibj.Timer;

/**
 * Class to be inherited by all auto classes.
 *
 * @author David Luo
 *
 */
public abstract class Autonomous {
  // TODO: make autonomous
  public static final double GYRO_ANGLE_TOLERANCE = 15.0;
  public static final double RAMP_LENGTH = 18;
  public static final double LOW_BAR_DISTANCE = 0;

  public static final double CROSS_TIME = 2;
  public static final double CROSS_DEFENSE_TIMEOUT = 5;

  protected TankDrive tankDrive;
  protected Encoder leftEncoder;
  protected Encoder rightEncoder;

  protected Timer timer;
  protected Timer timeout;

  public Autonomous(TankDrive tankDrive) {
    this.tankDrive = tankDrive;
    leftEncoder = tankDrive.getLeftEncoder();
    rightEncoder = tankDrive.getRightEncoder();
  }

  public static enum State {
    INIT_CASE,
    END_CASE,
    DRIVE_FORWARD,
    CROSS_DEFENSE,
    REACH_DISTANCE,
    FIND_TARGET,
    SPINUP,
    FIRE,
    REALIGN,
    STOP,
    STOW_PISTONS,
    LOW_GOAL_TARGET
  }

  public static enum AutonomousMode {
    DO_NOTHING,
    REACH_DEFENSE,
    BREACH_DEFENSE,
    TEST_AUTO,
    SHOOT_AT_TARGET
  }

  public static final double ACTUATOR_ERROR_TOLERANCE =
      0.05; // get actual
  // number
  public static final double REACH_DISTANCE = 74; // 74 inches
  public static State presentState = State.INIT_CASE;
  public static State nextState;
  public static double lowGoalScoreDistance = 120 + 144; // FIX NUMBER
  public static double lowGoalAngle = 56; // FIX NUMBER

  // public static double properDistance;

  public static void resetAuto() { presentState = State.INIT_CASE; }

  public abstract void run();
}
