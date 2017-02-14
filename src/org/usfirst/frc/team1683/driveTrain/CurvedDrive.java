package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

/**
 * Curved drive to follow path based on a function y
 * 
 * @author Yi Liu
 *
 */
public class CurvedDrive {
	public final static double width = 20;

	TankDrive drive;
	public double x;
	public final double speed;

	public CurvedDrive(TankDrive drive) {
		this.drive = drive;
		x = 0;
		speed = 0.5;
	}

	public void run() {
		x += 0.1;
		SmartDashboard.sendData("CurvedDrive", ratioAngularVelocity(Math.toRadians(x)));
		SmartDashboard.sendData("x", x);
		
		drive.setRight(speed * (isTurnRight(x) ? 1 : ratioAngularVelocity(Math.toRadians(x))));
		drive.setLeft(speed * (isTurnRight(x) ? ratioAngularVelocity(Math.toRadians(x)) : 1));
	}

	public double function(double x) {
		return Math.sin(x);
	}

	public double derivFunction(double x) {
		return Math.cos(x);
	}

	public double deriv2Function(double x) {
		return -Math.sin(x);
	}

	public double calCurve(double x) {
		if (derivFunction(x) != 0)
			return Math.pow((1 + derivFunction(x) * derivFunction(x)), 1.5) / (deriv2Function(x));
		return speed;
	}

	public boolean isTurnRight(double x) {
		if (calCurve(x) < 0)
			return true;
		return false;
	}

	public double ratioAngularVelocity(double x) {
		return 1 / (1 + (width / (Math.abs(calCurve(x)) + width / 2)));
	}
}
