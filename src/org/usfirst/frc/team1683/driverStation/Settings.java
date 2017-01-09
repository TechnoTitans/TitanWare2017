package org.usfirst.frc.team1683.driverStation;

public class Settings {

  public static double angleMotorP;
  public static double angleMotorI;
  public static double angleMotorD;

  public static int angleAllowableClosedLoopErr;

  public static double shooterMotorP;
  public static double shooterMotorI;
  public static double shooterMotorD;
  public static double shooterMotorF;

  public static double shooterK;

  public static double shooterRampRate;

  public static double shooterFilterSensitivity;
  public static double shooterTargetSpeed;

  public static double properDistance;

  public static double antiDriftKp;

  public static double climberAngle;
  public static double climberInches;

  public static double autoSpeed;
  public static double autoTime;

  public static void updateSettings() {
    angleMotorP = SmartDashboard.getDouble("Angle Motor P");
    angleMotorI = SmartDashboard.getDouble("Angle Motor I");
    angleMotorD = SmartDashboard.getDouble("Angle Motor D");

    shooterMotorP = SmartDashboard.getDouble("Shooter Motor P");
    shooterMotorI = SmartDashboard.getDouble("Shooter Motor I");
    shooterMotorD = SmartDashboard.getDouble("Shooter Motor D");
    shooterMotorF = SmartDashboard.getDouble("Shooter Motor F");

    angleAllowableClosedLoopErr =
        SmartDashboard.getInt("Shooter Allowable Closed Loop Error");

    shooterK = SmartDashboard.getDouble("Shooter K");

    shooterRampRate = SmartDashboard.getDouble("Shooter Ramp Rate");

    shooterFilterSensitivity =
        SmartDashboard.getDouble("Shooter Filter Sensitivity");
    shooterTargetSpeed =
        SmartDashboard.getDouble("TeleOp Shooter Target Speed");

    properDistance = SmartDashboard.getDouble("Proper Distance");

    antiDriftKp = SmartDashboard.getDouble("AntiDrift Kp");

    climberAngle = SmartDashboard.getDouble("Climber Pistons angle");
    climberInches = SmartDashboard.getDouble("Climber Pistons inch");

    autoSpeed = SmartDashboard.getDouble("Auto Speed");
    autoTime = SmartDashboard.getDouble("Auto Time");
  }
}
