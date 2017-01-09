package org.usfirst.frc.team1683.pickerUpper;

import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.LinearActuator;

public class PickerUpper extends LinearActuator {

  // public static final double MAX_ANGLE = 260;
  // public static final double MIN_ANGLE = 0;

  public PickerUpper(int deviceNumber, boolean reversed) {
    super(deviceNumber, reversed);
    // TODO Auto-generated constructor stub
  }

  // /**
  // *
  // * @param angle
  // * @return inches
  // *
  // * Converts angle (degrees) to linear actuator length (inches)
  // */
  // public double convertToInch(double angle) {
  //
  // angle -= ANGLE_OFFSET;
  //
  // if (angle > MAX_ANGLE)
  // angle = MAX_ANGLE;
  // if (angle < MIN_ANGLE)
  // angle = MIN_ANGLE;
  //
  // double inches = angle *
  // (MAX_INCHES-MIN_INCHES)/(MAX_ANGLE-MIN_ANGLE);
  //
  // return inches;
  //
  //
  // }

  /**
   *
   * @param angle
   *
   *            Angles pickerUpper
   */
  public void anglePickerUpper() {
    double inches =
        (MAX_INCHES - MIN_INCHES) *
            (DriverStation.auxStick.getRawAxis(DriverStation.ZAxis) + 1) /
            2 +
        MIN_INCHES;
    SmartDashboard.sendData("PickerUpper inches", inches);
    super.PIDupdate(inches);
    SmartDashboard.sendData("actual value", super.getPosInches());
  }
}
