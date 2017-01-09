package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.LinearActuator;

import edu.wpi.first.wpilibj.Timer;

public class BreachDefense extends Autonomous {

  BuiltInAccel accel;
  LinearActuator actuator;

  public BreachDefense(TankDrive tankDrive, BuiltInAccel accel,
                       LinearActuator actuator) {
    super(tankDrive);
    this.accel = accel;
    timer = new Timer();
    timeout = new Timer();
    this.actuator = actuator;
  }

  @Override
  public void run() {
    switch (presentState) {
      case INIT_CASE:
        nextState = State.STOW_PISTONS;
        break;

      case STOW_PISTONS:
        if (actuator.getError() < Autonomous.ACTUATOR_ERROR_TOLERANCE)
          nextState = State.DRIVE_FORWARD;
        break;
      case DRIVE_FORWARD:
        // TODO: Need to add timeout to moveDistance?
        tankDrive.moveDistance(REACH_DISTANCE + RAMP_LENGTH);
        nextState = State.CROSS_DEFENSE;
        timer.start();
        timeout.start();
        break;

      // Uses accelerometer to tell if on defense or not
      case CROSS_DEFENSE:
        if (timeout.get() > CROSS_DEFENSE_TIMEOUT) {
          nextState = State.STOP;
          break;
        }
        if (!accel.isFlat()) {
          tankDrive.set(Motor.MID_SPEED);
          timer.reset();
          nextState = State.CROSS_DEFENSE;
        } else {
          if (timer.get() < CROSS_TIME)
            nextState = State.CROSS_DEFENSE;
          else
            nextState = State.STOP;
        }
        break;

      case STOP:
        tankDrive.stop();
        nextState = State.END_CASE;
        break;

      case END_CASE:
        nextState = State.END_CASE;
        break;

      default:
        break;
    }
    presentState = nextState;
  }
}