package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

import edu.wpi.first.wpilibj.Timer;

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
	public final double speed = 0.2;

	private Timer timer;
	TankDrive drive;
	Gyro gyro;
	double t;

	PathPoint[] points = { new PathPoint(0, 0), new PathPoint(0, 1), new PathPoint(1, 1), new PathPoint(2, 2),
			new PathPoint(3, 5), };

	public CurvedDrive(TankDrive drive, Gyro gyro) {
		this.drive = drive;
		this.gyro = gyro;
		gyro.reset();
		SmartDashboard.prefDouble("kp", 0.022);
		SmartDashboard.sendData("kp", 0.022);
		timer = new Timer();
		timer.start();
		t = 0.0;
	}

	public void run() {
		t = timer.get() / 3.00;
		
		SmartDashboard.sendData("Time(x)", t);
		SmartDashboard.sendData("Function", function());
		SmartDashboard.sendData("Curved Drive Ratio", ratioAngularVelocity(t, false));
		SmartDashboard.sendData("Radius of Curve", calCurve());

		SmartDashboard.sendData("Curved left speed",
				speed * (isTurningRight(t, false) ? 1 : ratioAngularVelocity(t, false)));
		SmartDashboard.sendData("Curved right speed",
				speed * (isTurningRight(t, false) ? ratioAngularVelocity(t, false) : 1));

		 drive.setRight(speed * (isTurningRight(t, false) ? 1 :
		 ratioAngularVelocity(t, false)));
		 drive.setLeft(speed * (isTurningRight(t, false) ?
		 ratioAngularVelocity(t, false) : 1));

		SmartDashboard.sendData("Antidrift", antiDrift(-1));
		SmartDashboard.sendData("Antidrift", antiDrift(1));
	}

	public double antiDrift(int right) {
		double error = currentAngle() - gyro.getAngle();
		SmartDashboard.sendData("gyro angle", gyro.getAngle());

		double correction = SmartDashboard.getDouble("kp") * error / 2.0;
		SmartDashboard.sendData("Anti - Curved Speed Received", speed);
		SmartDashboard.sendData("Anti - Curved Correction", correction);
		SmartDashboard.sendData("Anti - Curved Corrected Speed", limitSpeed(speed - correction * right));
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

	private double currentAngle() {
		if (Math.atan(derivFunction()) > Math.PI) {
			return Math.atan(derivFunction()) + Math.PI;
		}
		return Math.atan(derivFunction());
	}

	// meters from now on!!
	private double function() {
		return 0.05 * (0.1 * Math.pow(t, 4) - 4.0 * Math.pow(t, 3) + 51.0 * Math.pow(t, 2) - 220.0 * t + 172.9);
	}

	private double derivFunction() {
		return 0.05 * (0.4 * Math.pow(t, 3) - 12.0 * Math.pow(t, 2) + 102.0 * t - 220.0);
	}

	private double deriv2Function() {
		return 0.05 * (1.2 * Math.pow(t, 2) - 24.0 * t + 102.0);
	}

	private double calCurve() {
		if (deriv2Function() != 0)
			return Math.pow((1 + derivFunction() * derivFunction()), 1.5) / (deriv2Function());
		return speed;
	}

	// parametric x
	public double parafunctionX() {
		return Math.pow(t, -1);
	}

	public double derivParafunctionX() {
		return -Math.pow(t, -2);
	}

	public double deriv2ParafunctionX() {
		return 2 * Math.pow(t, -3);
	}

	// parametric y
	public double parafunctionY() {
		return Math.pow(t, -1);
	}

	public double derivParafunctionY() {
		return -Math.pow(t, -2);
	}

	public double deriv2ParafunctionY() {
		return 2 * Math.pow(t, -3);
	}

	private double calParaCurve() {
		double denominator = derivParafunctionX() * deriv2ParafunctionY()
				- derivParafunctionY() * deriv2ParafunctionX();
		if (denominator != 0)
			return Math.pow((derivParafunctionX() * derivParafunctionX() + derivParafunctionY() * derivParafunctionY()),
					1.5) / (denominator);
		return speed;
	}

	@SuppressWarnings("unused")
	private double calPointsCurve() {
		if (t + 2 > points.length)
			return speed;

		PathPoint point1 = points[(int) t];
		PathPoint point2 = points[(int) t + 1];
		PathPoint point3 = points[(int) t + 2];

		double side1 = Math.sqrt((point1.getX() - point2.getX()) * (point1.getX() - point2.getX())
				+ (point1.getY() - point2.getY()) * (point1.getY() - point2.getY()));
		double side2 = Math.sqrt((point3.getX() - point2.getX()) * (point3.getX() - point2.getX())
				+ (point3.getY() - point2.getY()) * (point3.getY() - point2.getY()));
		double side3 = Math.sqrt((point1.getX() - point3.getX()) * (point1.getX() - point3.getX())
				+ (point1.getY() - point3.getY()) * (point1.getY() - point3.getY()));

		double semiPeri = (side1 + side2 + side3) / 2;
		double area = Math.sqrt(semiPeri * (semiPeri - side1) * (semiPeri - side2) * (semiPeri - side3));

		double radius = side1 * side2 * side3 / (area * 4);
		return radius;
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
