package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class AutonomousSwitcher {
	public Autonomous autoSelected;
	
	private AutonomousChooser autoChooser;

	BuiltInAccel accel;
	Gyro gyro;

	public AutonomousSwitcher(TankDrive tankDrive, PiVisionReader piReader) {
		autoChooser = new AutonomousChooser();
		
		autoChooser.setDefault("Do nothing", new DoNothing(tankDrive));
		autoChooser.addObject("Square Auto", new SquareAuto(tankDrive), false);
		autoChooser.addObject("Edge Gear Left", new EdgeGear(tankDrive, false, piReader), true);
		autoChooser.addObject("Edge Gear Right", new EdgeGear(tankDrive, true, piReader), true);
		autoChooser.addObject("MiddleGear", new MiddleGear(tankDrive, piReader), true);
		autoChooser.addObject("PassLine", new PassLine(tankDrive), true);

		autoChooser.sendDashboard();
	}

	public void setAuto() {
		autoChooser.getSelected();
	}
	public void run() {
		autoChooser.selectedAuto.run();
	}
}
