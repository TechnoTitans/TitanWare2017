package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class CurvedDrive {
	public final static double width = 20;

	TankDrive drive;
	public double x;

	public CurvedDrive(TankDrive drive) {
		this.drive = drive;
		x = 0;
	}

	public void run() {
		x+=0.1;
			SmartDashboard.sendData("CurvedDrive", ratioAngularVelocity(x * 3.14 / 180));
			SmartDashboard.sendData("x", x);
			drive.setLeft(0.8 * ratioAngularVelocity(x * 3.14 / 180));
		
	}

	public static double function(double x) {
		return Math.sin(x);
	}

	public static double derivFunction(double x) {
		return Math.cos(x);
	}

	public static double deriv2Function(double x) {
		return -Math.sin(x);
	}

	public static double calCurve(double x) {
		SmartDashboard.sendData("CurveRadius",
				 Math.abs(Math.pow((1 + derivFunction(x) * derivFunction(x)), 1.5) / (deriv2Function(x))));
		return Math.abs(Math.pow((1 + derivFunction(x) * derivFunction(x)), 1.5) / (deriv2Function(x)));
	}

	public static double ratioAngularVelocity(double x) {
		return 1 / (1 + (width / (calCurve(x) + width / 2)));
	}
}
