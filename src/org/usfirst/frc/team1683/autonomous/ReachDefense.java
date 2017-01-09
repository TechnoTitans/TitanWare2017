package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.Settings;
import org.usfirst.frc.team1683.sensors.LinearActuator;
import org.usfirst.frc.team1683.shooter.Shooter;

import edu.wpi.first.wpilibj.Timer;

/**
 * Reaches base of defense
 *
 * @author Sneha Kadiyala
 *
 */
public class ReachDefense extends Autonomous {

  // LinearActuator actuator;
  Shooter shooter;
  // Timer timeout;

  public ReachDefense(TankDrive driveTrain, Shooter shooter) {
    super(driveTrain);
    this.shooter = shooter;
    // timeout = new Timer();
  }

  @Override
  public void run() {
    shooter.angleShooter(Shooter.MAX_ANGLE);
    switch (presentState) {
      case INIT_CASE:
        // timeout.start();
        nextState = State.DRIVE_FORWARD;
        break;

      // case STOW_PISTONS:
      // if(actuator.getError() < Autonomous.ACTUATOR_ERROR_TOLERANCE)
      // nextState = State.DRIVE_FORWARD;
      case DRIVE_FORWARD:
        Settings.updateSettings();
        // double speed = Settings.autoSpeed;
        // double time = Settings.autoTime;
        tankDrive.moveDistance(REACH_DISTANCE, 0.8);
        // if (timeout.get() < time) {
        // tankDrive.set(speed);
        // }
        nextState = State.END_CASE;
        break;

      case END_CASE:
        // tankDrive.stop();
        nextState = State.END_CASE;
        break;

      default:
        break;
    }
    presentState = nextState;
  }
}
