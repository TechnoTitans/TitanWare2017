package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.MotorGroup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDLoop extends PIDSubsystem {
	private double input;
	private final double TOLERANCE = 0.05;
	private MotorGroup motors;
	private TalonSRX talon;
	
	private String identifier;

	public PIDLoop(double p, double i, double d, MotorGroup motors, String identifier) {
		super(p, i, d);
		this.motors = motors;
		this.identifier = identifier;
	}

	public PIDLoop(double p, double i, double d, TalonSRX talon) {
		super(p, i, d);
		this.talon = talon;
	}

	public void setInput(double input) {
		this.input = input;
	}

	@Override
	protected void initDefaultCommand() {
		setAbsoluteTolerance(TOLERANCE);
		getPIDController().setContinuous(false);
		setOutputRange(-1, 1);
	}

	@Override
	protected double returnPIDInput() {
		return input;
	}

	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.sendData("output PID "+identifier, output);
		if (motors != null) {
			motors.set(output);
		} else if (talon != null) {
			talon.set(output);
		}
	}

	public double getPIDPosition() {
		return super.getPosition();
	}

	// return 0 if neither found, 1 if motorgroup, -1 if talon
	public int getType() {
		if (motors != null) {
			return 1;
		}
		if (talon != null) {
			return -1;
		}
		return 0;
	}
}
