package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

public class AntiDrift {

  private final int antidriftangle = 0;
  @SuppressWarnings("unused")
  // This variable is used for error correction (now automated)
  // It used to come from SmartDashboard
  private final double kp; 
  private Gyro gyro;

  private MotorGroup left;
  private MotorGroup right;

  public AntiDrift(MotorGroup left, MotorGroup right, Gyro gyro,
                   double kp) {
    this.left = left;
    this.right = right;
    // this.kp = kp;
    // 9 was good estimate.
    SmartDashboard.prefDouble("kp", 9);
    this.kp = SmartDashboard.getDouble("kp");
    this.gyro = gyro;
  }

  public double antiDrift(double speed, MotorGroup motorGroup) {
    double error = antidriftangle - gyro.getAngle();
    // double correction = kp*error/2.0;

    double correction = SmartDashboard.getDouble("kp") * error / 2.0;
    if (motorGroup.equals(left)) {
      // TODO:make sure motors are spinning the correct direction
      double leftSpeed = limitSpeed(speed + correction); // motors need to
      // be spinning
      // the correct
      // direction
      return leftSpeed;
    } else if (motorGroup.equals(right)) {
      double rightSpeed =
          limitSpeed(speed - correction); // motors need to
      // be spinning
      // the correct
      // direction
      return rightSpeed;
    } else {
      return speed;
    }
  }

  public static double limitSpeed(double speed) {
    if (speed > 1.0) {
      return 1.0;
    } else if (speed < -1.0) {
      return -1.0;
    } else {
      return speed;
    }
  }
}
