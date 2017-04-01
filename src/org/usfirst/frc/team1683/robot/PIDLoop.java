package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.MotorGroup;
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
	private MotorGroup group;
	private String identifier;

	private double input;
	private boolean disabled = true;
	private double speed;

	public PIDLoop(double p, double i, double d, DriveTrain drive, double speed, String identifier) {
		super(p, i, d);
		this.drive = drive;
		this.speed = speed;
		this.identifier = identifier;
	}

	public PIDLoop(double p, double i, double d, TalonSRX talon, String identifier) {
		super(p, i, d);
		this.talon = talon;
		this.identifier = identifier;
	}

	public PIDLoop(double p, double i, double d, MotorGroup group) {
		super(p, i, d);
		this.group = group;
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

	public void setTarget(double input){
		setSetpoint(input);
	}
	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.sendData("GearScoreDisabled", disabled, false);
		if (!disabled) {
			SmartDashboard.sendData(identifier + " PID Output ", speed * (1 - output), false);
			SmartDashboard.sendData(identifier + " PIDLeft", speed * (1 - output), false);
			SmartDashboard.sendData(identifier + " PIDRight", speed * (1 + output), false);
			if (getType() == 1)
				runDrive(output);
			if (getType() == 2)
				runTalon(output);
		}
	}

	private void runDrive(double output) {
		drive.getLeftGroup().set(speed * (1 - output));
		drive.getRightGroup().set(speed * (1 + output));
	}

	private void runTalon(double output) {
		talon.set(output);
	}

	public double getPIDPosition() {
		return getPosition();
	}

	// return 0 if neither found, 1 if drivetrain, -1 if talon
	public int getType() {
		if (drive != null)
			return 1;
		if (talon != null)
			return 2;
		if (group != null)
			return 3;
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
