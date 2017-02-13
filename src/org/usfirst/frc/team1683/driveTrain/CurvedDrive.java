package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

/**
 * Curved drive to follow path based on a function y
 * 
 * ALL METERS HERE
 * 
 * @author Yi Liu
 *
 */
public class CurvedDrive {
	public final static double width = 0.6985;
	public final double speed = 0.5;

	TankDrive drive;
	public double x;

	public CurvedDrive(TankDrive drive) {
		this.drive = drive;
		x = 0.0;
	}

	public void run() {
		x += 0.03;
		SmartDashboard.sendData("CurvedDriveRatio", ratioAngularVelocity(x, false));
		SmartDashboard.sendData("x", x);
		SmartDashboard.sendData("CurvedDrive", calCurve(x));
		SmartDashboard.sendData("Function", function(x));

		drive.setRight(speed * (isTurningRight(x, false) ? 1 : ratioAngularVelocity(x, false)));
		drive.setLeft(speed * (isTurningRight(x, false) ? ratioAngularVelocity(x, false) : 1));
	}

	public double function(double x) {
		return 0.1 * Math.pow(x, 4) - 4.0 * Math.pow(x, 3) + 51.0 * Math.pow(x, 2) - 220.0 * x + 172.9;
	}

	public double derivFunction(double x) {
		return 0.4 * Math.pow(x, 3) - 12.0 * Math.pow(x, 2) + 102.0 * x - 220.0;
	}

	public double deriv2Function(double x) {
		return 1.2 * Math.pow(x, 2) - 24.0 * x + 102.0;
	}

	public double calCurve(double x) {
		if (deriv2Function(x) != 0)
			return Math.pow((1 + derivFunction(x) * derivFunction(x)), 1.5) / (deriv2Function(x));
		return speed;
	}

	// parametric x
	public double parafunctionX(double x) {
		return Math.pow(x, -1);
	}

	public double derivParafunctionX(double x) {
		return -Math.pow(x, -2);
	}

	public double deriv2ParafunctionX(double x) {
		return 2 * Math.pow(x, -3);
	}

	// parametric y
	public double parafunctionY(double x) {
		return Math.pow(x, -1);
	}

	public double derivParafunctionY(double x) {
		return -Math.pow(x, -2);
	}

	public double deriv2ParafunctionY(double x) {
		return 2 * Math.pow(x, -3);
	}

	public double calParaCurve(double x) {
		double denominator = derivParafunctionX(x) * deriv2ParafunctionY(x)
				- derivParafunctionY(x) * deriv2ParafunctionX(x);
		if (denominator != 0)
			return Math.pow(
					(derivParafunctionX(x) * derivParafunctionX(x) + derivParafunctionY(x) * derivParafunctionY(x)),
					1.5) / (denominator);
		return speed;
	}

	public boolean isTurningRight(double x, boolean para) {
		if (para) {
			if (calParaCurve(x) < 0)
				return false;
		}
		if (calCurve(x) < 0)
			return true;
		return false;
	}

	public double ratioAngularVelocity(double x, boolean para) {
		if (para)
			return 1 / (1 + (width / (Math.abs(calParaCurve(x)) - width / 2)));
		return 1 / (1 + (width / (Math.abs(calCurve(x)) - width / 2)));
	}
}
