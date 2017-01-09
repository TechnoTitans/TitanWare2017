package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.AnalogGyro;

public class Gyro extends AnalogGyro implements Sensor {

  public static double GYRO_SENSITIVITY = 0.00656693;

  public Gyro(int channel) {
    super(channel);
    super.initGyro();
    super.reset();
    super.setSensitivity(GYRO_SENSITIVITY);
  }

  @Override
  public double getRaw() {
    // TODO Auto-generated method stub
    return super.getAngle();
  }

  @Override
  public void calibrate() {
    // TODO Auto-generated method stub
    super.calibrate();
  }

  @Override
  public double getAngle() {
    return super.getAngle() % 360;
  }

  @Override
  public double getRate() {
    // TODO Auto-generated method stub
    return super.getRate();
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub
  }
}
