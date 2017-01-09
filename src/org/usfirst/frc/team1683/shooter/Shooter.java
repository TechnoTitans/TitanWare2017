package org.usfirst.frc.team1683.shooter;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.Settings;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
import org.usfirst.frc.team1683.robot.HWP;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.robot.TechnoTitan;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Timer;

public class Shooter {
  // Maximum and minimum range of shooting (subject to change).
  // private final double MAX_ENCODER_COUNT = 512;
  // private final double MIN_ENCODER_COUNT = 0;
  // public static final double MAX_ANGLE = 70;
  // public static final double MIN_ANGLE = 0;
  // private final double POSITION_TO_ANGLE_COEFFICENT = (MAX_ANGLE -
  // MIN_ANGLE)
  // / (MAX_ENCODER_COUNT - MIN_ENCODER_COUNT);
  // private final double ANGLE_TO_POSITION_COEFFICENT =
  // (MAX_ENCODER_COUNT -
  // MIN_ENCODER_COUNT)
  // / (MAX_ANGLE - MIN_ANGLE);

  public static final double MAX_DISTANCE = 190;
  public static final double MIN_DISTANCE = 90;

  public final static double MIN_ANGLE = 9;
  public static final double MAX_ANGLE = 60;

  public static final double MIN_POT_COUNT = 327;
  public static final double MAX_POT_COUNT = 706;

  public static final double COUNTS_PER_DEGREE =
      (MAX_POT_COUNT - MIN_POT_COUNT) / (MAX_ANGLE - MIN_ANGLE);

  public static final int ALLOWABLE_ERROR = 400; // find num

  public static final double ANGLE_ERROR_THRESHOLD =
      0.125 * (MAX_POT_COUNT - MIN_POT_COUNT);
  public static final double ALLOWABLE_ERROR_TIME = 5.0;
  public static boolean errorFlag = false;

  public static final double INTAKE_SPEED = -4000;
  public static final double LOW_GOAL_ANGLE = 30; // Pick better angle
  public static final double LOW_GOAL_SPEED = 4500;
  private static final double DEFAULT_SPEED = 4500;

  public static final double ANGLE_OFFSET = 60; // change based on shooter
  // mounting
  // For calculating floor distance
  public static final double TARGET_OVERHANG = 5;
  public static final double TARGET_HEIGHT = 97;
  public static final double CAMERA_HOR_OFF = 8;
  public static final double CAMERA_VER_OFF = 13;

  // For smoothing changing shooter angles
  private InputFilter inputFilter;

  // MotorGroup shooterMotors;
  TalonSRX leftMotor;
  TalonSRX rightMotor;
  TalonSRX angleMotor;

  public static Timer errorTimer;
  Solenoid shootPiston;

  // private boolean isCreated = false;
  // public double visionDistance = 20; // WE NEED TO REPLACE THIS WITH
  // YI'S
  // DISTANCE METHOD

  boolean isToggled = false;
  boolean isManualToggled = false;
  // private boolean isCreated = false;
  private State presentTeleOpState;

  public enum State {
    INTAKE,
    HOLD,
    SHOOT;
  }

  public Shooter(int leftChannel, int rightChannel, int angleMotorChannel,
                 Solenoid shootPiston) {
    this.angleMotor = new TalonSRX(angleMotorChannel, true);
    this.leftMotor = new TalonSRX(leftChannel, false);
    this.rightMotor = new TalonSRX(rightChannel, false);
    this.shootPiston = shootPiston;

    this.angleMotor.PIDInit();
    this.leftMotor.PIDInit();
    this.rightMotor.PIDInit();

    errorTimer = new Timer();
    // errorTimer.start();

    // this.angleMotor.configEncoderCodesPerRev(4096);

    this.angleMotor.calibrate();
    this.angleMotor.setAllowableClosedLoopErr(0);
    // this.angleMotor.enableLimitSwitch(true, true);

    this.leftMotor.enableBrakeMode(false); // coast mode
    this.rightMotor.enableBrakeMode(false); // coast mode
    this.leftMotor.reverseSensor(false);
    this.rightMotor.reverseSensor(false);

    // inputFilter = new InputFilter(SmartDashboard.getDouble("Shooter
    // K"),
    // getAnglePosition());
    inputFilter =
        new InputFilter(SmartDashboard.getDouble("Shooter K"), MAX_ANGLE);

    this.presentTeleOpState = State.HOLD;
  }

  /**
   * returns scaled joystick output
   *
   * @return
   */
  public double getJoystickAngle() {
    double angle;
    // if (DriverStation.auxStick.getRawAxis(DriverStation.YAxis) > 0.04)
    // {
    // angle = DriverStation.scaleToMax(DriverStation.auxStick);
    // } else if (DriverStation.auxStick.getRawAxis(DriverStation.YAxis) <
    // -0.04) {
    // angle = DriverStation.scaleToMin(DriverStation.auxStick);
    // } else {
    // angle = (BACK_LIMIT_ANGLE + FORWARD_LIMIT_ANGLE) / 2;
    // }

    angle = (DriverStation.auxStick.getRawAxis(DriverStation.YAxis) + 1) /
                2 * (MAX_ANGLE - MIN_ANGLE) +
            MIN_ANGLE;

    SmartDashboard.sendData("Joystick position", angle);

    return angle;
  }

  private boolean isToggled() {
    if (DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWP.BUTTON_6)) {
      isToggled = !isToggled;
    }
    SmartDashboard.sendData("Button Toggle", isToggled ? "on" : "off");
    return isToggled;
  }

  private boolean isManualToggled() {
    if (DriverStation.antiBounce(HWR.AUX_JOYSTICK, HWP.BUTTON_11)) {
      isManualToggled = !isManualToggled;
    }
    SmartDashboard.sendData(" Override Button Toggle",
                            isManualToggled ? "on" : "off");
    return isManualToggled;
  }

  public void setShooterSensitivity(double sensitivity) {
    inputFilter.setFilterK(sensitivity);
  }

  /**
   * Extends pistons to shoot ball
   */
  public void shootBall() {
    if (!shootPiston.isExtended()) shootPiston.set(true);
  }

  /**
   * Retracts Pistons
   */
  public void resetShootPistons() {
    if (shootPiston.isExtended()) {
      shootPiston.set(false);
    }
  }

  /**
   * takes in distance from vision in inches
   *
   *
   * @return
   */
  public double getFloorDistance() {
    return Math.sqrt(Math.pow(TechnoTitan.vision.getDistance(), 2) -
                     Math.pow(TARGET_HEIGHT - CAMERA_VER_OFF, 2)) +
        CAMERA_HOR_OFF + TARGET_OVERHANG;
  }

  public double getSpeed() {

    double speed;
    speed = DriverStation.auxStick.getRawAxis(DriverStation.ZAxis) * 750 +
            3750;

    // if (getAngle() < LOW_GOAL_ANGLE)
    // speed = LOW_GOAL_SPEED;
    // else if (getFloorDistance() < MAX_DISTANCE && getFloorDistance() >
    // MIN_DISTANCE)
    // speed = 0.0001054 * Math.pow(getFloorDistance(), 4) - 0.04996 *
    // Math.pow(getFloorDistance(), 3)
    // + 8.614 * Math.pow(getFloorDistance(), 2) - 632.4 *
    // getFloorDistance() + 2.083 * Math.pow(10, 4);
    // else
    // speed = DEFAULT_SPEED;
    //
    SmartDashboard.sendData("Shooter speed", speed);

    return speed;
  }

  /**
   *
   * @return angle (degrees) based off vision distance
   *
   */
  public double getAngle() {

    double angle;
    double visionDistance = TechnoTitan.vision.getDistance();
    SmartDashboard.sendData("DistanceTarget", visionDistance);
    // double visionDistance = TechnoTitan.vision.getFilteredDistance();

    if (visionDistance <= MAX_DISTANCE &&
        visionDistance >= MIN_DISTANCE) {
      angle = .00000008707 * Math.pow(visionDistance, 5) -
              .00004673 * Math.pow(visionDistance, 4) +
              .009827 * Math.pow(visionDistance, 3) -
              1.011 * Math.pow(visionDistance, 2) +
              50.69 * visionDistance - 930.6;
      // equation for shooting curve
      SmartDashboard.sendData("Shooter Vision Angle", angle);
    } else
      angle = getJoystickAngle();

    return restrictAngle(angle);
  }

  public double getAnglePosition() {
    // double potCounts = (angle - MIN_ANGLE) * COUNTS_PER_DEGREE +
    // MIN_POT_COUNT;
    return (angleMotor.getPosition() - MIN_POT_COUNT) /
        COUNTS_PER_DEGREE +
        MIN_ANGLE;
  }

  public double restrictAngle(double angle) {
    if (angle > MAX_ANGLE) {
      angle = MAX_ANGLE;
    } else if (angle < MIN_ANGLE) {
      angle = MAX_ANGLE;
    }

    return angle;
  }

  /**
   * Set shooter to absolute position angle
   *
   * @param angle
   */
  public void angleShooter(double angle) {
    angle = restrictAngle(angle);

    // angle -= ANGLE_OFFSET;
    SmartDashboard.sendData("Pot Position", angleMotor.getPosition());
    SmartDashboard.sendData("Angle Motor Error", angleMotor.getError());

    if (Math.abs(angleMotor.getError()) < ANGLE_ERROR_THRESHOLD) {
      errorTimer.reset();
    }

    // if (errorTimer.hasPeriodPassed(ALLOWABLE_ERROR_TIME)) {
    if (errorTimer.get() > ALLOWABLE_ERROR_TIME) {
      SmartDashboard.sendData("Error Timer", errorTimer.get());
      angleMotor.disableControl();
      errorFlag = true;
      angleMotor.stop();
      SmartDashboard.sendData("Control Enabled", false);
    } else if (isManualToggled()) {
      angleMotor.disable();
      SmartDashboard.sendData("Control Enabled", false);
    } else {
      angleMotor.enableControl();
      SmartDashboard.sendData("Control Enabled", true);
    }

    updatePIDF();

    SmartDashboard.sendData("Shooter Set Angle", angle);

    angle = inputFilter.filterInput(angle);

    SmartDashboard.sendData("Shooter Filtered Angle", angle);
    SmartDashboard.sendData("Error Timer", errorTimer.get());
    SmartDashboard.sendData("Target Angle", angle);

    double potCounts =
        (angle - MIN_ANGLE) * COUNTS_PER_DEGREE + MIN_POT_COUNT;

    SmartDashboard.sendData("Target Pot Counts", potCounts);

    // angle = angle * ANGLE_TO_POSITION_COEFFICENT;

    // angleMotor.PIDPosition(angle);
    // angleMotor.PIDAngle(angle);
    if (!errorFlag) {
      angleMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);
      angleMotor.PIDPosition(potCounts);
    }
  }

  /**
   * TeleOp
   */
  public void shootMode() {
    updatePIDF();
    updatePrefs();

    stateSwitcher();

    if (isToggled())
      // angleShooter(getAngle());
      angleShooter(SmartDashboard.getDouble("Target Angle"));
    else
      angleShooter(getJoystickAngle());

    if (presentTeleOpState == State.SHOOT &&
        DriverStation.auxStick.getRawButton(HWR.SHOOT_BALL))
      shootBall();
    else
      resetShootPistons();

    updatePrefs();
    report();
  }

  public void intake() {
    leftMotor.PIDSpeed(INTAKE_SPEED);
    rightMotor.PIDSpeed(INTAKE_SPEED);
  }

  public void hold() {
    leftMotor.stop();
    rightMotor.stop();
    // leftMotor.PIDSpeed(0);
    // rightMotor.PIDSpeed(0);
  }

  public void shoot(double speed) {
    leftMotor.PIDSpeed(speed);
    rightMotor.PIDSpeed(speed);
  }

  public void updatePIDF() {
    Settings.updateSettings();

    leftMotor.setP(Settings.shooterMotorP);
    leftMotor.setI(Settings.shooterMotorI);
    leftMotor.setD(Settings.shooterMotorD);
    leftMotor.setF(Settings.shooterMotorF);

    rightMotor.setP(Settings.shooterMotorP);
    rightMotor.setI(Settings.shooterMotorI);
    rightMotor.setD(Settings.shooterMotorD);
    rightMotor.setF(Settings.shooterMotorF);

    leftMotor.setCloseLoopRampRate(Settings.shooterRampRate);
    rightMotor.setCloseLoopRampRate(Settings.shooterRampRate);

    angleMotor.setP(Settings.angleMotorP);
    angleMotor.setI(Settings.angleMotorI);
    angleMotor.setD(Settings.angleMotorD);
  }

  public void stateSwitcher() {
    String state = "out";
    double speed = getSpeed();
    State nextState;

    updatePIDF();

    switch (presentTeleOpState) {
      case HOLD: {
        state = "HOLD";
        hold();
        nextState = State.HOLD;
        if (DriverStation.auxStick.getRawButton(HWR.SPIN_UP_INTAKE) &&
            DriverStation.auxStick.getRawButton(HWR.SPIN_UP_SHOOTER)) {
          nextState = State.HOLD;
        } else if (DriverStation.auxStick.getRawButton(
                       HWR.SPIN_UP_SHOOTER)) {
          nextState = State.SHOOT;
        } else if (DriverStation.auxStick.getRawButton(
                       HWR.SPIN_UP_INTAKE)) {
          nextState = State.INTAKE;
        }
        break;
      }
      case INTAKE: {
        state = "INTAKE";
        intake();
        nextState = State.INTAKE;
        if (!DriverStation.auxStick.getRawButton(HWR.SPIN_UP_INTAKE)) {
          nextState = State.HOLD;
        }

        break;
      }
      case SHOOT: {
        state = "SHOOT";
        // shoot(getSpeed());
        // double speed = SmartDashboard.getDouble("Shooter Speed RPM");
        shoot(speed);
        nextState = State.SHOOT;
        if (!DriverStation.auxStick.getRawButton(HWR.SPIN_UP_SHOOTER)) {
          nextState = State.HOLD;
        }
        break;
      }
      default: {
        nextState = State.HOLD;
        SmartDashboard.sendData("Shooter state machine error", true);
        break;
      }
    }

    presentTeleOpState = nextState;
    SmartDashboard.sendData("Shooter state", state);
    // report();
  }

  public void reset() {
    // targetPos = angleMotor.getPosition();
    angleMotor.calibrate();
  }

  public void report() {
    SmartDashboard.sendData("Shooter Encoder Position",
                            angleMotor.getEncPosition());
    SmartDashboard.sendData("Shooter Position", angleMotor.getPosition());
    // This is the position you're looking for.
    SmartDashboard.sendData("Shooter relAngle",
                            angleMotor.getPosition() / COUNTS_PER_DEGREE);
    SmartDashboard.sendData(
        "Shooter Angle",
        ((angleMotor.getPosition() - MIN_POT_COUNT) / COUNTS_PER_DEGREE) +
            MIN_ANGLE);

    SmartDashboard.sendData("Left Talon Target",
                            leftMotor.PIDTargetSpeed());
    SmartDashboard.sendData("Right Talon Target",
                            rightMotor.PIDTargetSpeed());
    SmartDashboard.sendData("Left Talon Speed", leftMotor.getSpeed());
    SmartDashboard.sendData("Right Talon Speed", rightMotor.getSpeed());
    SmartDashboard.sendData("Left Talon Error",
                            leftMotor.PIDErrorSpeed());
    SmartDashboard.sendData("Right Talon Error",
                            rightMotor.PIDErrorSpeed());

    SmartDashboard.sendData("Left Setpoint", leftMotor.getSetpoint());
    SmartDashboard.sendData("Left Speed", leftMotor.get());
    SmartDashboard.sendData("Left Error",
                            leftMotor.getClosedLoopError() / 4096.0);

    SmartDashboard.sendData("right Setpoint", rightMotor.getSetpoint());
    SmartDashboard.sendData("right Speed", rightMotor.get());
    SmartDashboard.sendData("right Error",
                            rightMotor.getClosedLoopError() / 4096.0);
  }

  public void updatePrefs() {
    inputFilter.setFilterK(SmartDashboard.getDouble("Shooter K"));
  }

  /**
   * Set shooter to speed {@param rpm}
   *
   * @param rpm
   */
  public void spinShooter(double rpm) {
    leftMotor.PIDSpeed(rpm);
    rightMotor.PIDSpeed(-rpm);
  }
}
