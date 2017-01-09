package org.usfirst.frc.team1683.test;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;

public class BuiltInAccelTester {
  BuiltInAccel accel;

  public BuiltInAccelTester() { accel = new BuiltInAccel(); }

  public void test() {
    SmartDashboard.sendData("X", accel.getX());
    SmartDashboard.sendData("Y", accel.getY());
    SmartDashboard.sendData("Z", accel.getZ());
    SmartDashboard.sendData("isFlat", accel.isFlat());
    SmartDashboard.sendData("Angle XZ", accel.getAngleXZ());
    SmartDashboard.sendData("Angle YZ", accel.getAngleYZ());
  }
}
