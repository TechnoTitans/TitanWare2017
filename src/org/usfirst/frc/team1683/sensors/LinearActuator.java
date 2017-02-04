package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class LinearActuator extends TalonSRX {

	public static final double ROT_PER_INCH = 8.76 / 12;
	public static final double COUNTS_PER_INCH = 820 / 10.9375;
	public static final int POT_TURN_NUM = 10;
	public static final double MAX_INCHES = 9.29;
	public static final double MIN_INCHES = 0.02;
	// public static final double L_BASE = 23.37;
	// public static final double L_PIVOT = 5.02;
	// public static final double L_FIXED = 18.38;
	public static final double ANGLE_OFFSET = 20;
	public static final int ALLOWABLE_ERROR = 4;

	public LinearActuator(int deviceNumber, boolean reversed) {
		super(deviceNumber, reversed);
		// TODO Auto-generated constructor stub
		PIDinit();
	}

	public void PIDinit() {
		// boolean isPresent;
		// if(super.isSensorPresent(FeedbackDevice.AnalogPot).equals(FeedbackDeviceStatus.FeedbackStatusNotPresent))
		// {
		// isPresent = false;
		// }
		// else isPresent = true;
		// SmartDashboard.sendData("Linear Actuator present ", isPresent);
		super.setFeedbackDevice(FeedbackDevice.AnalogPot);
		super.setInverted(false);
		super.changeControlMode(TalonControlMode.Position);
		super.enableBrakeMode(true);
		super.setAllowableClosedLoopErr(ALLOWABLE_ERROR);
		// super.configPotentiometerTurns(POT_TURN_NUM);
		super.enable();
	}

	public void PIDupdate(double posInInches) {
		super.setPID(SmartDashboard.getDouble("Linear Actuator P"), SmartDashboard.getDouble("Linear Actuator I"),
				SmartDashboard.getDouble("Linear Actuator D"));

		super.PIDPosition(clampInches(posInInches) * COUNTS_PER_INCH + 62); // conversion
		// to
		// rotations

		SmartDashboard.sendData("Talon " + super.getDeviceID(), "Position(inches)" + posInInches);
		SmartDashboard.sendData("Talon " + super.getDeviceID(), "Position(rotations)" + super.getPosition());

		SmartDashboard.sendData("Linear Actuator error", super.getError());
	}

	public double clampInches(double posInInches) {
		if (posInInches > MAX_INCHES)
			posInInches = MAX_INCHES;
		if (posInInches < MIN_INCHES)
			posInInches = MIN_INCHES;
		return posInInches;
	}

	/**
	 *
	 * @return position in inches from encoder counts
	 */
	public double getPosInches() {
		return super.getPosition() / COUNTS_PER_INCH;
	}
}
