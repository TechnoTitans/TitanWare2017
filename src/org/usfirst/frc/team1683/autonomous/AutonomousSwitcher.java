package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.TechnoTitan;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;
	
	public SendableChooser chooser;

	BuiltInAccel accel;
	Gyro gyro;

	public AutonomousSwitcher(TankDrive tankDrive, PiVisionReader piReader) {
		chooser = new SendableChooser();
		
		addAuto("Do nothing", new DoNothing(tankDrive), true);
		addAuto("Square Auto", new SquareAuto(tankDrive), false);
		addAuto("Edge Gear Turn Left", new EdgeGear(tankDrive, false, false, piReader), true);
		addAuto("Edge Gear Turn Right", new EdgeGear(tankDrive, true, false, piReader), true);
		addAuto("Wide Edge Turn Left", new EdgeGear(tankDrive, false, true, piReader), true);
		addAuto("Wide Edge Turn Right", new EdgeGear(tankDrive, true, true, piReader), true);
		setDefault("MiddleGear", new MiddleGear(tankDrive, piReader));
		addAuto("PassLine", new PassLine(tankDrive), true);
		addAuto("VisionMiddle", new VisionMiddle(tankDrive, piReader), true);

		SmartDashboard.putData("Auto", chooser);
	}
	
	public void setDefault(String name, Autonomous auto) {
		chooser.addDefault(name, auto);
	}

	@SuppressWarnings("unused")
	public void addAuto(String name, Autonomous auto, boolean forCompetition) {
		double warning;
		if (!(TechnoTitan.isCompetitionTime && !forCompetition)) {
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
