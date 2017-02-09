package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Controls {
	DriveTrain drive;
	Winch winch;
	LimitSwitch limitSwitch;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		winch = new Winch(HWR.WINCH);
		limitSwitch = new LimitSwitch(HWR.CLIMB_SWITCH);
	}

	public void run() {
		double lSpeed = -DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
		double rSpeed = -DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
		drive.driveMode(lSpeed, rSpeed);

		if (DriverStation.rightStick.getRawButton(HWR.SPIN_UP_INTAKE)) {
			
		}
		SmartDashboard.sendData("LimitSwitch", limitSwitch.isPressed());
		if(DriverStation.auxStick.getRawButton(HWR.SPIN_WINCH)){
			winch.turnWinch();
		}
		if (DriverStation.auxStick.getRawButton(HWR.STOP_WINCH) || limitSwitch.isPressed())
			winch.stop();
	}
}
