package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

/**
 * Curved drive to follow path based on a function y
 * 
 * ALL METERS HERE except antidrift
 * 
 * @author Yi Liu
 *
 */
public class CurvedDrive {
	public final static double width = 0.6985;
	public final double speed = 0.5;

	TankDrive drive;
	Gyro gyro;
	public double x;

	public CurvedDrive(TankDrive drive, Gyro gyro) {
		this.drive = drive;
		this.gyro = gyro;
		x = 0.0;
		gyro.reset();
		SmartDashboard.prefDouble("kp", 0.022);
		SmartDashboard.sendData("kp", 0.022);
	}

	public void run() {
		x += 0.03;
		SmartDashboard.sendData("x", x);
		SmartDashboard.sendData("Function", function());
		SmartDashboard.sendData("Curved Drive Ratio", ratioAngularVelocity(x, false));
		SmartDashboard.sendData("Radius of Curve", calCurve());

		drive.setRight(speed * (isTurningRight(x, false) ? 1 : ratioAngularVelocity(x, false)));
		drive.setLeft(speed * (isTurningRight(x, false) ? ratioAngularVelocity(x, false) : 1));
	}

	public double antiDrift(int right) {
		double error = currentAngle() - gyro.getAngle();
		SmartDashboard.sendData("gyro angle", gyro.getAngle());

		double correction = SmartDashboard.getDouble("kp") * error / 2.0;
		SmartDashboard.sendData("Curved Speed Received", speed);
		SmartDashboard.sendData("Curved Correction", correction);
		SmartDashboard.sendData("Curved Corrected Speed", limitSpeed(speed - correction * right));
		return limitSpeed(speed - correction * right);
	}

	private static double limitSpeed(double speed) {
		if (speed > 1.0) {
			return 1.0;
		} else if (speed < -1.0) {
			return -1.0;
		} else {
			return speed;
		}
	}

	public double currentAngle() {
		if (Math.atan(derivFunction()) > Math.PI) {
			return Math.atan(derivFunction()) + Math.PI;
		}
		return Math.atan(derivFunction());
	}
	
	//meters from now on!!
	public double function() {
		return 0.1 * Math.pow(x, 4) - 4.0 * Math.pow(x, 3) + 51.0 * Math.pow(x, 2) - 220.0 * x + 172.9;
	}

	public double derivFunction() {
		return 0.4 * Math.pow(x, 3) - 12.0 * Math.pow(x, 2) + 102.0 * x - 220.0;
	}

	public double deriv2Function() {
		return 1.2 * Math.pow(x, 2) - 24.0 * x + 102.0;
	}

	public double calCurve() {
		if (deriv2Function() != 0)
			return Math.pow((1 + derivFunction() * derivFunction()), 1.5) / (deriv2Function());
		return speed;
	}

	// parametric x
	public double parafunctionX() {
		return Math.pow(x, -1);
	}

	public double derivParafunctionX() {
		return -Math.pow(x, -2);
	}

	public double deriv2ParafunctionX() {
		return 2 * Math.pow(x, -3);
	}

	// parametric y
	public double parafunctionY() {
		return Math.pow(x, -1);
	}

	public double derivParafunctionY() {
		return -Math.pow(x, -2);
	}

	public double deriv2ParafunctionY() {
		return 2 * Math.pow(x, -3);
	}

	public double calParaCurve() {
		double denominator = derivParafunctionX() * deriv2ParafunctionY()
				- derivParafunctionY() * deriv2ParafunctionX();
		if (denominator != 0)
			return Math.pow((derivParafunctionX() * derivParafunctionX() + derivParafunctionY() * derivParafunctionY()),
					1.5) / (denominator);
		return speed;
	}

	public boolean isTurningRight(double x, boolean para) {
		if (para) {
			if (calParaCurve() < 0)
				return false;
		}
		if (calCurve() < 0)
			return true;
		return false;
	}

	public double ratioAngularVelocity(double x, boolean isParametric) {
		if (isParametric)
			return 1 / (1 + (width / (Math.abs(calParaCurve()) - width / 2)));
		return 1 / (1 + (width / (Math.abs(calCurve()) - width / 2)));
	}
}
