package org.usfirst.frc.team1683.driveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

public class AntiDrift {

	private final int antidriftangle = 0;
	@SuppressWarnings("unused")
	// This variable is used for error correction (now automated)
	// It used to come from SmartDashboard
	private final double kp;
	private Gyro gyro;
	// 1 if right, -1 if left, 0 if no correction should be applied
	private int right;

	public AntiDrift(Gyro gyro, int right) {
		SmartDashboard.prefDouble("kp", 0.01);
		SmartDashboard.sendData("kp", 0.01);
		this.kp = SmartDashboard.getDouble("kp");
		this.gyro = gyro;
		this.right = right;
	}

	/**
	 * 
	 * @param speed The current speed of the motor
	 * @return The new speed of the motor that should be set to make the angle of the gyro closer to zero
	 */
	public double antiDrift(double speed) {
		double error = antidriftangle - gyro.getAngle();
		SmartDashboard.sendData("gyroangle", gyro.getAngle());
		
		double correction = SmartDashboard.getDouble("kp") * error / 2.0;
		SmartDashboard.sendData("speedreceived", speed);
		SmartDashboard.sendData("correction", correction);
		SmartDashboard.sendData("correctedspeed", limitSpeed(speed -  correction * right));
		return limitSpeed(speed -  correction * right);
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
}
