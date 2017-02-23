package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.Motor;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDLoop extends PIDSubsystem {
	private double input;
	private final double TOLERANCE = 0.05;
	private MotorGroup motors;

	public PIDLoop(double p, double i, double d, MotorGroup motors) {
		super(p, i, d);
		this.motors = motors;

		setAbsoluteTolerance(TOLERANCE);
		getPIDController().setContinuous(false);
	}

	public void setInput(double input) {
		this.input = input;
	}

	@Override
	protected double returnPIDInput() {
		return input;
	}

	@Override
	protected void usePIDOutput(double output) {
		for (Motor motor : motors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).pidWrite(output);
			}
		}
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
