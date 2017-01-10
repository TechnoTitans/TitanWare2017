package org.usfirst.frc.team1683.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1683.autonomous.Autonomous.AutonomousMode;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LinearActuator;

/**
 *
 * @author Sneha Kadiyala
 *
 *         Switches between different autonomous programs
 *         Depending on our Alliance's strengths and weaknesses,
 *         we may want to do different things during autonomous mode
 *         This provides an easy way to quickly decide before the match which mode we want to use
 *
 */
public class AutonomousSwitcher {
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
  Gyro gyro;
  LinearActuator actuator;

  public AutonomousSwitcher(DriveTrain driveTrain, BuiltInAccel accel, LinearActuator actuator) {
    chooser = new SendableChooser();
    chooser.addDefault(DEFAULT_AUTO.name(), DEFAULT_AUTO.name());
    for (AutonomousMode mode : Autonomous.AutonomousMode.values()) {
      if (!mode.equals(DEFAULT_AUTO))
        chooser.addObject(mode.name(), mode.name());
    }
    edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData(
        "Auto to run", chooser);

    this.driveTrain = driveTrain;
    this.accel = accel;
    this.actuator = actuator;
    updateAutoSelected();
  }

  public void updateAutoSelected() {
    int autoMode = SmartDashboard.getInt("Auto Mode");
    // switch (toMode(SmartDashboard.getString("Auto Selector"))) {
    switch (autoMode) {
      case -1:
        // case TEST_AUTO:
    	// Which isn't implemented? (Deleted?)
        //autoSelected = new TestAuto((TankDrive)driveTrain);
        break;
      case 0:
      // case DO_NOTHING:
      default:
        autoSelected = new DoNothing((TankDrive)driveTrain);
        break;
    }
  }

  public Autonomous getAutoSelected() {
    // updateAutoSelected();
    return autoSelected;
  }

  public void run() { autoSelected.run(); }
}
