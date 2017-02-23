package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDLoop extends PIDSubsystem {
	private double input;
	private final double TOLERANCE = 0.05;
	private MotorGroup motors;
	private TalonSRX talon;

	public PIDLoop(double p, double i, double d, MotorGroup motors) {
		super(p, i, d);
		this.motors = motors;
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
	}

	@Override
	protected double returnPIDInput() {
		return input;
	}

	@Override
	protected void usePIDOutput(double output) {
		if (motors != null) {
			for (Motor motor : motors) {
				if (motor instanceof TalonSRX) {
					((TalonSRX) motor).pidWrite(output);
				}
			}
		} else if (talon != null) {
			talon.pidWrite(output);
		}
	}
	
	public double getPIDPosition(){
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
