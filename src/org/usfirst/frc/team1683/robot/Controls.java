package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;

public class Controls {
	DriveTrain drive;

	public Controls(DriveTrain drive) {
		this.drive = drive;
	}

	public void run() {
		double lSpeed = -DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		double rSpeed = -DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
		drive.driveMode(lSpeed, rSpeed);
		
		//if(DriverStation.rightStick.getRawButton(HWR.SPIN_UP_INTAKE)){
			
		//}
	}
}
