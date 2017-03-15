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
		
		setDefault("Do nothing", new DoNothing(tankDrive));
		addAuto("Square Auto", new SquareAuto(tankDrive), false);
		addAuto("Edge Gear Turn Left", new EdgeGear(tankDrive, false, piReader), true);
		addAuto("Edge Gear Turn Right", new EdgeGear(tankDrive, true, piReader), true);
		addAuto("MiddleGear", new MiddleGear(tankDrive, piReader), true);
		addAuto("PassLine", new PassLine(tankDrive), true);
		addAuto("VisionMiddle", new VisionMiddle(tankDrive, piReader), false);

		SmartDashboard.putData("Choose Auto", chooser);
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
