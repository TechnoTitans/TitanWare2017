package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.scoring.Winch;

public class Controls {
	DriveTrain drive;
	Winch winch;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		winch = new Winch(HWR.WINCH);
	}

	public void run() {
		double lSpeed = -DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		double rSpeed = -DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
		drive.driveMode(lSpeed, rSpeed);

		if (DriverStation.rightStick.getRawButton(HWR.SPIN_UP_INTAKE)) {

		}
		if (DriverStation.rightStick.getRawButton(HWR.SPIN_WINCH))
			winch.turnWinch();
		else
			winch.stop();
	}
}
