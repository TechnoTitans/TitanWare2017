package org.usfirst.frc.team1683.test;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.sensors.Gyro;

public class GyroTester {
  Gyro gyro;

  public GyroTester() { gyro = new Gyro(HWR.GYRO); }

  public void test() {
    SmartDashboard.sendData("Gyro Angle", gyro.getRaw());
  }
}
