package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.autonomous.ShootAtTarget;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1683.autonomous.Autonomous.AutonomousMode;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LinearActuator;
import org.usfirst.frc.team1683.shooter.Shooter;
import org.usfirst.frc.team1683.vision.FindGoal;

/**
 *
 * @author Sneha Kadiyala
 *
 *         Switches between different autonomous programs
 *
 */
public class AutonomousSwitcher { // Testing commit system will delete later
  // Autonomous autonomous;

  // public AutonomousSwitcher(TankDrive tankDrive) {
  // int autoMode = DriverStation.getInt("autonomousMode");
  //
  // if (autoMode == 1)
  // autonomous = new ReachDefense(tankDrive);
  // else if (autoMode == 2)
  // autonomous = new DoNothing(tankDrive);
  //
  // // else autonomous = new DefaultAuto();
  // }
  public final AutonomousMode DEFAULT_AUTO = AutonomousMode.DO_NOTHING;
  public Autonomous autoSelected;

  private SendableChooser chooser;

  private DriveTrain driveTrain;
  BuiltInAccel accel;
  FindGoal vision;
  Shooter shooter;
  Gyro gyro;
  LinearActuator actuator;
  // ShootingPhysics physics;

  public AutonomousSwitcher(DriveTrain driveTrain, BuiltInAccel accel,
                            FindGoal vision, Shooter shooter,
                            LinearActuator actuator) {
    // ShootingPhysics physics) {
    chooser = new SendableChooser();
    chooser.addDefault(DEFAULT_AUTO.name(), DEFAULT_AUTO.name());
    for (AutonomousMode mode : Autonomous.AutonomousMode.values()) {
      if (!mode.equals(DEFAULT_AUTO))
        chooser.addObject(mode.name(), mode.name());
    }
    edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData(
        "Auto to run", chooser);

    this.driveTrain = driveTrain;
    // this.physics = physics;
    this.accel = accel;
    this.vision = vision;
    this.actuator = actuator;
    updateAutoSelected();
  }

  public void updateAutoSelected() {
    // SmartDashboard.sendData("AUTO SELECTED",
    // SmartDashboard.getString("Auto Selector"));
    int autoMode = SmartDashboard.getInt("Auto Mode");
    // switch (toMode(SmartDashboard.getString("Auto Selector"))) {
    switch (autoMode) {
      case 1:
        // case REACH_DEFENSE:
        autoSelected = new ReachDefense((TankDrive)driveTrain, shooter);
        break;
      case 2:
        // case BREACH_DEFENSE:
        autoSelected =
            new BreachDefense((TankDrive)driveTrain, accel, actuator);
        break;
      case 3:
        // case SHOOT_AT_TARGET:
        autoSelected = new ShootAtTarget((TankDrive)driveTrain, accel,
                                         vision, shooter, gyro);
        break;
      case -1:
        // case TEST_AUTO:
        autoSelected = new TestAuto((TankDrive)driveTrain);
        break;
      case 0:
      // case DO_NOTHING:
      default:
        autoSelected = new DoNothing((TankDrive)driveTrain);
        break;
    }
  }

  private AutonomousMode toMode(String mode) {
    return AutonomousMode.valueOf(mode);
  }

  public Autonomous getAutoSelected() {
    // updateAutoSelected();
    return autoSelected;
  }

  public void run() { autoSelected.run(); }
}
