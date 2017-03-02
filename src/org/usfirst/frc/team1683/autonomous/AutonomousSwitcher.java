package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LinearActuator;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;

	private SendableChooser chooser;

	BuiltInAccel accel;
	Gyro gyro;
	LinearActuator actuator;

	public AutonomousSwitcher(TankDrive tankDrive, PiVisionReader piReader) {
		chooser = new SendableChooser();
		chooser.addDefault("Do nothing", new DoNothing(tankDrive));
		chooser.addObject("Square Auto", new SquareAuto(tankDrive));
		chooser.addObject("Edge Gear Left", new EdgeGear(tankDrive, false, piReader));
		chooser.addObject("Edge Gear Right", new EdgeGear(tankDrive, true, piReader));
		chooser.addObject("MiddleGear", new MiddleGear(tankDrive));
		
		SmartDashboard.putData("Auto to run", chooser);
	}
	public void setAuto(){
		autoSelected = (Autonomous) chooser.getSelected();
	}
	
	public Autonomous getAutoSelected() {
		return autoSelected;
	}

	public void run() {
		autoSelected.run();
	}
}
