package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.sensors.Gyro;

public class AntiDrift {

	private final int antidriftangle = 0;
	@SuppressWarnings("unused")
	// This variable is used for error correction (now automated)
	// It used to come from SmartDashboard
	private final double kp;
	private Gyro gyro;

	public AntiDrift(Gyro gyro) {
		SmartDashboard.prefDouble("kp", 0.03); //TODO testing
		SmartDashboard.sendData("kp", 0.03);
		this.kp = SmartDashboard.getDouble("kp");
		this.gyro = gyro;
	}

	public double antiDrift(double speed, Boolean left) {
		double error = antidriftangle - gyro.getAngle();
		SmartDashboard.sendData("gyroanti", gyro.getAngle());

		double correction = SmartDashboard.getDouble("kp") * error / 2.0;
		if (left) {
			double leftSpeed = limitSpeed(speed - correction) ;
			return leftSpeed;
		} else if (!left) {
			double rightSpeed = limitSpeed(speed - correction);
			return rightSpeed;
		} else {
			return speed;
		}
	}

	public static double limitSpeed(double speed) {
		if (speed > 1.0) {
			return 1.0;
		} else if (speed < -1.0) {
			return -1.0;
		} else {
			return speed;
		}
	}
}
