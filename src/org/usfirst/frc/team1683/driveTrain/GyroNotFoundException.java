package org.usfirst.frc.team1683.driveTrain;

public class GyroNotFoundException extends RuntimeException {
  public GyroNotFoundException() { super(); }

  public GyroNotFoundException(String msg) { super(msg); }
}
