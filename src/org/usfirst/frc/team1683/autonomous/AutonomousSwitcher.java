package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;

	public SendableChooser chooser;

	BuiltInAccel accel;
	Gyro gyro;

	public boolean isCompetitionTime;

	// Creates buttons for co driver to pick autonomous
	public AutonomousSwitcher(TankDrive tankDrive) {
		isCompetitionTime = DriverStation.getInstance().isFMSAttached();
		chooser = new SendableChooser();

		addAuto("Do Nothing", new DoNothing(tankDrive), true);
		addAuto("Square Auto", new SquareAuto(tankDrive), false);

		SmartDashboard.putData("Auto", chooser);
	}

	// When chooser is displayed, this autonomous is autonomatically selected
	public void setDefault(String name, Autonomous auto) {
		chooser.addDefault(name, auto);
	}

	// Adds auto to chooser only if it is for competition
	public void addAuto(String name, Autonomous auto, boolean forCompetition) {
		if (!(isCompetitionTime && !forCompetition)) {
			chooser.addObject(name, auto);
		}
	}

	public void getSelected() {
		autoSelected = (Autonomous) chooser.getSelected();
	}

	public void run() {
		autoSelected.run();
	}
}
