package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;

	public SendableChooser chooser;

	BuiltInAccel accel;
	Gyro gyro;
	
	public boolean isCompetitionTime;

	// Creates buttons for co driver to pick autonomous
	public AutonomousSwitcher(TankDrive tankDrive, PiVisionReader piReader) {
		isCompetitionTime = DriverStation.getInstance().isFMSAttached();
		chooser = new SendableChooser();
		
		addAuto("Do nothing", new DoNothing(tankDrive), true);
		addAuto("Square Auto", new SquareAuto(tankDrive), false);
		addAuto("Edge Gear Turn Left", new EdgeGear(tankDrive, false, false, piReader), true);
		addAuto("Edge Gear Turn Right", new EdgeGear(tankDrive, true, false, piReader), true);
		addAuto("Wide Edge Turn Left", new EdgeGear(tankDrive, false, true, piReader), true);
		setDefault("Wide Edge Turn Right", new EdgeGear(tankDrive, true, true, piReader));
		addAuto("MiddleGear", new MiddleGear(tankDrive, piReader), true);
		addAuto("PassLine Turn Right", new PassLine(tankDrive, true), true);
		addAuto("PassLine Turn Left", new PassLine(tankDrive, false), true);
		addAuto("VisionMiddle", new VisionMiddle(tankDrive, piReader), false);
		addAuto("Test Everything", new TestEverything(tankDrive), true);

		SmartDashboard.putData("Auto", chooser);
	}

	// When chooser is displayed, this autonomous is autonomatically selected
	public void setDefault(String name, Autonomous auto) {
		chooser.addDefault(name, auto);
	}

	// Adds auto to chooser only if it is for competition
	@SuppressWarnings("unused")
	public void addAuto(String name, Autonomous auto, boolean forCompetition) {
		double warning;
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
