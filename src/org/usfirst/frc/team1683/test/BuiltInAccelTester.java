package org.usfirst.frc.team1683.test;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;

public class BuiltInAccelTester {
  BuiltInAccel accel;

  public BuiltInAccelTester() { accel = new BuiltInAccel(); }

  public void test() {
    SmartDashboard.sendData("X", accel.getX(), false);
    SmartDashboard.sendData("Y", accel.getY(), false);
    SmartDashboard.sendData("Z", accel.getZ(), false);
    SmartDashboard.sendData("isFlat", accel.isFlat(), false);
    SmartDashboard.sendData("Angle XZ", accel.getAngleXZ(), false);
    SmartDashboard.sendData("Angle YZ", accel.getAngleYZ(), false);
  }
}
