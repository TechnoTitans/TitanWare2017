package org.usfirst.frc.team1683.pneumatics;

import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWP;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.sensors.LinearActuator;

import edu.wpi.first.wpilibj.Timer;

public class Climber {
  Timer chinUpTimer;
  Timer endGameTimer;
  LinearActuator actuator;
  Solenoid bottomSolenoid;
  Solenoid topSolenoid;
  public static State presentState;
  public static State nextState;

  public static final double LIFT_ANGLE = 45;
  public static final double RETRACT_ANGLE = 0;
  public static final double CHINUP_TIME = 5;
  public static final double END_GAME_TIME = 80;

  public static final boolean EXTEND = true;
  public static final boolean RETRACT = false;

  public static enum State {
    INIT_CASE,
    END_CASE,
    LIFT_HOOK,
    START_CHINUP,
    ROBOT_CHINUP,
    LOWER_HOOK
  }

  public Climber(int bottomSolenoidChannel, int topSolenoidChannel,
                 Timer endGameTimer) {
    presentState = State.INIT_CASE;
    chinUpTimer = new Timer();
    this.endGameTimer = endGameTimer;
    topSolenoid =
        new Solenoid(HWR.DEFAULT_MODULE_CHANNEL, topSolenoidChannel);
    bottomSolenoid =
        new Solenoid(HWR.DEFAULT_MODULE_CHANNEL, bottomSolenoidChannel);

    actuator = new LinearActuator(HWR.LINEAR_ACTUATOR, false);
    actuator.PIDinit();
  }

  public void deployHook() {
    bottomSolenoid.set(EXTEND);
    topSolenoid.set(EXTEND);
  }

  public void retractHook() {
    topSolenoid.set(RETRACT);
    bottomSolenoid.set(RETRACT);
  }

  public void climbMode() {

    // actuator.angleClimberPistons();
    SmartDashboard.sendData("Present Climber State", presentState.name());

    switch (presentState) {
      case INIT_CASE: {
        nextState = State.LOWER_HOOK;
        break;
      }
      case LOWER_HOOK: {
        this.retractHook();
        nextState = State.LOWER_HOOK;
        if (DriverStation.leftStick.getRawButton(HWR.LIFT_HOOK) &&
            DriverStation.rightStick.getRawButton(HWR.LIFT_HOOK) &&
            endGameTimer.hasPeriodPassed(END_GAME_TIME)) {
          nextState = State.LIFT_HOOK;
        }

        break;
      }
      case LIFT_HOOK: {
        this.deployHook();
        chinUpTimer.reset();
        nextState = State.LIFT_HOOK;
        if (DriverStation.leftStick.getRawButton(HWR.ROBOT_CHINUP) &&
            DriverStation.rightStick.getRawButton(HWR.ROBOT_CHINUP)) {
          nextState = State.START_CHINUP;
          chinUpTimer.start();
        }
        break;
      }
      case START_CHINUP: {
        this.deployHook();

        nextState = State.START_CHINUP;
        if (!(DriverStation.leftStick.getRawButton(HWR.ROBOT_CHINUP) &&
              DriverStation.rightStick.getRawButton(HWR.ROBOT_CHINUP)))
          nextState = State.LIFT_HOOK;
        if (chinUpTimer.hasPeriodPassed(CHINUP_TIME))
          nextState = State.ROBOT_CHINUP;
        break;
      }
      case ROBOT_CHINUP: {
        this.retractHook();
        nextState = State.ROBOT_CHINUP;
        break;
      }
      case END_CASE: {

        nextState = State.END_CASE;
        break;
      }
      default:
        nextState = State.END_CASE;
        break;
    }

    SmartDashboard.sendData("Chinup timer", chinUpTimer.get());
    presentState = nextState;
  }

  // /**
  // *
  // * @param inch
  // * @return angle
  // * Converts inches (moving) into angles (degrees)
  // */
  // public double convertToAngle(double inch) {
  //
  // inch = clampInches(inch);
  // inch += L_FIXED;
  // double angle = Math.acos((Math.pow(L_BASE, 2) + Math.pow(L_PIVOT,
  // 2)-Math.pow(inch, 2))/(2*L_BASE*L_PIVOT))*180/Math.PI;
  // angle += ANGLE_OFFSET;
  // SmartDashboard.sendData("Linear actuator Angle", angle);
  // return angle;
  // }
  //
  // public void angleClimberPistons() {
  // // scales raw input from knob
  // double angle = *
  // (DriverStation.auxStick.getRawAxis(DriverStation.ZAxis)
  // + 1)/2;
  // SmartDashboard.sendData("Knob angle", angle);
  // SmartDashboard.sendData("knob raw",
  // DriverStation.auxStick.getRawAxis(DriverStation.ZAxis));
  // PIDupdate(convertToInch(angle));
  // }
}
