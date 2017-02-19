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

public class AutonomousSwitcher {
	public final AutonomousMode DEFAULT_AUTO = AutonomousMode.DO_NOTHING;
	public Autonomous autoSelected;

	private SendableChooser chooser;

	private DriveTrain driveTrain;
	BuiltInAccel accel;
	Gyro gyro;
	LinearActuator actuator;

	public AutonomousSwitcher(DriveTrain driveTrain) {
		chooser = new SendableChooser();
		chooser.addDefault(DEFAULT_AUTO.name(), DEFAULT_AUTO.name());
		for (AutonomousMode mode : Autonomous.AutonomousMode.values()) {
			if (!mode.equals(DEFAULT_AUTO))
				chooser.addObject(mode.name(), mode.name());
		}
		edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData("Auto to run", chooser);

		this.driveTrain = driveTrain;
		updateAutoSelected();
	}

	public void updateAutoSelected() {
		int autoMode = SmartDashboard.getInt("Auto Mode");
		// switch (toMode(SmartDashboard.getString("Auto Selector"))) {
		switch (autoMode) {
			case -1:
				// case TEST_AUTO:
				// Which isn't implemented? (Deleted?)
				// autoSelected = new TestAuto((TankDrive)driveTrain);
				break;
			case 0:
				autoSelected = new DoNothing((TankDrive) driveTrain);
			case 1:
				autoSelected = new EdgeGear((TankDrive) driveTrain, false);
			case 2:
				autoSelected = new EdgeGear((TankDrive) driveTrain, true);
			default:
				autoSelected = new DoNothing((TankDrive) driveTrain);
				break;
		}
	}

	public Autonomous getAutoSelected() {
		// updateAutoSelected();
		return autoSelected;
	}

	public void run() {
		autoSelected.run();
	}
}
