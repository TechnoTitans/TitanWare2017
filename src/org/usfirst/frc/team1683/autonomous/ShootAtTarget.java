package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.shooter.Shooter;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LinearActuator;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.Settings;
import org.usfirst.frc.team1683.vision.FindGoal;

import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.vision.*;

/**
 * Shoots at target
 *
 * @author Yi Liu
 *
 */
public class ShootAtTarget extends Autonomous {
  BuiltInAccel accel;
  FindGoal vision;
  Shooter shooter;
  ShootingPhysics physics;
  Gyro gyro;
  // LinearActuator actuator;
  double originalAngle;

  public ShootAtTarget(TankDrive driveTrain, BuiltInAccel accel,
                       FindGoal vision, Shooter shooter, Gyro gyro) {
    super(driveTrain);
    this.accel = accel;
    this.vision = vision;
    this.shooter = shooter;
    this.gyro = gyro;

    timer = new Timer();
    timeout = new Timer();
    // this.physics=physics;
  }

  @Override
  public void run() {
    switch (presentState) {
      case INIT_CASE:
        nextState = State.DRIVE_FORWARD;
        break;

      // case STOW_PISTONS:
      // if(actuator.getError() < Autonomous.ACTUATOR_ERROR_TOLERANCE )
      // nextState = State.DRIVE_FORWARD;
      case DRIVE_FORWARD:
        // originalAngle = gyro.getAngle();
        tankDrive.moveDistance(REACH_DISTANCE);
        nextState = State.CROSS_DEFENSE;
        timer.start();
        timeout.start();
        break;

      // TODO: Need to add timeout to moveDistance
      case CROSS_DEFENSE:
        if (timeout.get() > CROSS_DEFENSE_TIMEOUT) {
          nextState = State.REALIGN;
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
            nextState = State.REALIGN;
        }
        break;

      case REALIGN:
        if (Math.abs(gyro.getAngle() - originalAngle) >=
            GYRO_ANGLE_TOLERANCE)
          tankDrive.turn(gyro.getAngle() - originalAngle);
        nextState = State.REACH_DISTANCE;
        break;

      case REACH_DISTANCE:
        Settings.updateSettings();
        tankDrive.moveDistance(Settings.properDistance);
        nextState = State.FIND_TARGET;
        break;

      case FIND_TARGET:
        if (vision.getOffset() > 0) {
          tankDrive.turn(-1);
        } else if (vision.getOffset() < 0) {
          tankDrive.turn(1);
        } else {
          nextState = State.FIRE;
        }
        break;

      case SPINUP:
        shooter.angleShooter(physics.FindAngle());
        // TODO: Get correct units for spinShooter
        shooter.spinShooter(physics.FindSpinSpeed());
        nextState = State.FIRE;
        break;
      case FIRE:
        // if()
        // try{
        // Thread.sleep(3000);
        // }
        // catch(InterruptedException e){
        // System.out.print("Thread fail");
        // }
        shooter.shootBall();
        nextState = State.END_CASE;
        break;

      case END_CASE:
        break;

      default:
        break;
    }

    presentState = nextState;
  }
}
