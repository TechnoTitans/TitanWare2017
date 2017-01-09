package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.shooter.Shooter;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class BreachLowGoal extends Autonomous {

  BuiltInAccel accel;
  Gyro gyro;
  Shooter shooter;

  public BreachLowGoal(TankDrive tankDrive, BuiltInAccel accel, Gyro gyro,
                       Shooter shooter) {
    super(tankDrive);
    this.accel = accel;
    this.gyro = gyro;
    this.shooter = shooter;
  }

  @Override
  public void run() {
    switch (presentState) {
      case INIT_CASE: {
        nextState = State.DRIVE_FORWARD;
        break;
      }
      case DRIVE_FORWARD: {
        tankDrive.moveDistance(lowGoalScoreDistance);
        tankDrive.turn(lowGoalAngle);
        nextState = State.LOW_GOAL_TARGET;
        break;
      }
      case LOW_GOAL_TARGET: {
        shooter.shoot(Shooter.LOW_GOAL_SPEED);
        nextState = State.END_CASE;
        break;
      }
      case END_CASE: {
        nextState = State.END_CASE;
        break;
      }
    }
    presentState = nextState;
  }
}
