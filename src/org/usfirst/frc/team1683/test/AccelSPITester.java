package org.usfirst.frc.team1683.test;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.AccelSPI;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class AccelSPITester {
  AccelSPI accel;

  public AccelSPITester() {
    accel = new AccelSPI(Accelerometer.Range.k2G);
  }

  public void test() {
    SmartDashboard.sendData("X normal", accel.getX(), false);
    SmartDashboard.sendData("Y normal", accel.getY(), false);
    SmartDashboard.sendData("Z normal", accel.getZ(), false);
  }
}
