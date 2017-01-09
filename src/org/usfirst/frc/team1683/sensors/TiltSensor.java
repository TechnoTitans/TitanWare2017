package org.usfirst.frc.team1683.sensors;

public class TiltSensor {
  AnalogAccel accelX;
  AnalogAccel accelY;

  public TiltSensor(int channelX, int channelY) {
    accelX = new AnalogAccel(channelX);
    accelY = new AnalogAccel(channelY);
  }

  public double getX() { return accelX.getAcceleration(); }

  public double getY() { return accelY.getAcceleration(); }

  public double getAngle() {
    return Math.atan2(accelY.getAcceleration(),
                      accelX.getAcceleration()) *
        180 / Math.PI;
  }
}
