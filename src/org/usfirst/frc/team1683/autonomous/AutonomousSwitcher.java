package org.usfirst.frc.team1683.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1683.autonomous.Autonomous.AutonomousMode;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.DriverStation;
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

	public AutonomousSwitcher(TankDrive tankDrive) {
		chooser = new SendableChooser();
		chooser.addDefault("Do nothing", new DoNothing(tankDrive));
		chooser.addObject("Square Auto", new SquareAuto(tankDrive));
		chooser.addObject("Edge Gear", new EdgeGear(tankDrive, false));
		chooser.addObject("Edge Gear2", new EdgeGear(tankDrive, true));
		edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData("Auto to run", chooser);
		this.driveTrain = tankDrive;
		
		setAuto();
	}
	public void setAuto(){
		autoSelected = (Autonomous) chooser.getSelected();
	}
	
	public Autonomous getAutoSelected() {
		// updateAutoSelected();
		return autoSelected;
	}

	public void run() {
		autoSelected.run();
	}
}
