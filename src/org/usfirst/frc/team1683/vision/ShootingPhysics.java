package org.usfirst.frc.team1683.vision;

public class ShootingPhysics {
  double HEIGHT = 2.4638; // height between robot and shooter
  double length; // distance between robot and shooter
  final double radius = 0.0492125;
  final double powerincrease = 1.0; // TODO

  public ShootingPhysics(double length) { this.length = length; }

  public double findSpeedY() { return (Math.sqrt(2 * 9.81 * HEIGHT)); }

  public double findSpeedX() {
    return (this.length / (Math.sqrt(HEIGHT * 2 / 9.81)));
  }

  public double FindSpinSpeed() {
    return (Math.sqrt(Math.pow(2, (findSpeedX())) +
                      Math.pow(2, (findSpeedY()))) *
            powerincrease / (radius * 0.051)); // returns in RPM
  }

  public double FindAngle() {
    return (Math.tan(findSpeedX() / findSpeedY())); // returns in radians
  }
}
