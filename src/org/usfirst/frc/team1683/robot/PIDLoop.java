package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * 
 * @author Yi Liu
 * 
 */
public class PIDLoop extends PIDSubsystem {
	private final double TOLERANCE = 0.05;

	private DriveTrain drive;
	private TalonSRX talon;
	private String identifier;

	private double input;
	private boolean disabled;
	private double speed;

	public PIDLoop(double p, double i, double d, DriveTrain drive, double speed, String identifier) {
		super(p, i, d);
		this.drive = drive;
		this.speed = speed;
		this.identifier = identifier;

		disabled = true;
	}

	public PIDLoop(double p, double i, double d, TalonSRX talon) {
		super(p, i, d);
		this.talon = talon;
		disabled = true;
	}

	public void setInput(double input) {
		this.input = input;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
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
		SmartDashboard.sendData("GearScoreDisabled", disabled, false);
		if (!disabled) {
			SmartDashboard.sendData(identifier + " PID Output ", speed * (1 - output), false);
			SmartDashboard.sendData(identifier + " PIDLeft", speed * (1 - output), false);
			SmartDashboard.sendData(identifier + " PIDRight", speed * (1 + output), false);
			if (drive != null) {
				drive.getLeftGroup().set(speed * (1 - output));
				drive.getRightGroup().set(speed * (1 + output));
			} else if (talon != null) {
				talon.set(output);
			}
		}
	}

	public double getPIDPosition() {
		return getPosition();
	}

	// return 0 if neither found, 1 if drivetrain, -1 if talon
	public int getType() {
		if (drive != null) {
			return 1;
		}
		if (talon != null) {
			return -1;
		}
		return 0;
	}

	public void stopPID() {
		disable();
		disabled = true;
	}

	public void enablePID() {
		enable();
		disabled = false;
	}

	public void setPID(double p, double i, double d) {
		getPIDController().setPID(p, i, d);
	}
}
