package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.HWR;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class TalonSRX extends CANTalon implements Motor {

	private Encoder encoder;
	// This thread handles moving a certain distance in a separate thread
	private Thread thread;
	private double PIDTargetSpeed;
	AntiDrift anti;
	Gyro gyro;
	private class MotorMover implements Runnable {

		private double distance;
		private double speed;
		private TalonSRX talonSrx;
		private Boolean left;
		
		public MotorMover(TalonSRX talonSrx, double distance, double speed, Boolean left) {
			this.talonSrx = talonSrx;
			this.left = left;
			this.distance = distance;
			if (distance < 0)
				this.speed = -speed;
			else
				this.speed = speed;
		}

		@Override
		public void run() {
			encoder.reset();
			while (Math.abs(encoder.getDistance()) < Math.abs(distance)) {
				if (encoder.getDistance() != 0) {
					SmartDashboard.sendData("current distance", encoder.getDistance());
					SmartDashboard.sendData("distance goal", distance);
				}
				talonSrx.set(anti.antiDrift(speed, true));
				SmartDashboard.sendData("antidrift", anti.antiDrift(speed, true));
			}
			talonSrx.stop();

			encoder.reset();
		}
	}

	/**
	 * Constructor for a TalonSRX motor
	 *
	 * @param channel
	 *            The port where the TalonSRX is plugged in.
	 * @param reversed
	 *            If the TalonSRX should invert the signal.
	 */
	public TalonSRX(int channel, boolean reversed) {
		super(channel);
		super.setInverted(reversed);
	}
	public TalonSRX(int channel, boolean reversed, AntiDrift anti) {
		super(channel);
		super.setInverted(reversed);
		this.anti = anti;
	}

	/**
	 * Constructor
	 *
	 * @param channel
	 *            The port where the TalonSRX is plugged in.
	 * @param reversed
	 *            If the TalonSRX should invert the signal.
	 * @param encoder
	 *            Encoder to attach to this TalonSRX.
	 */
	public TalonSRX(int channel, boolean reversed, AntiDrift anti, Encoder encoder) {
		super(channel);
		super.setInverted(reversed);
		this.anti = anti;
		this.encoder = encoder;
	}

	/**
	 * Move distance in inches at mid speed
	 *
	 * @param distance
	 *            Distance in inches
	 */
	@Override
	public void moveDistance(double distance, Boolean left) throws EncoderNotFoundException {
		moveDistance(distance, Motor.MID_SPEED, left);
	}

	/**
	 * Move distance in inches
	 *
	 * @param distance
	 *            Distance in inches.
	 * @param speed
	 *            Speed from 0 to 1.
	 */
	@Override
	public void moveDistance(double distance, double speed, Boolean left) throws EncoderNotFoundException {

		if (hasEncoder()) {
			if (thread == null || thread.getState().equals(Thread.State.TERMINATED)) {
				thread = new Thread(new MotorMover(this, distance, speed, left));
			}
			if (thread.getState().equals(Thread.State.NEW)) {
				thread.start();
			}
		} else {
			throw new EncoderNotFoundException();
		}
	}

	/**
	 * Set the speed of the TalonSRX.
	 *
	 * @param speed
	 *            -- Speed from 0 to 1.
	 */
	@Override
	public void set(double speed) {
		super.changeControlMode(TalonControlMode.PercentVbus);
		super.set(speed);
		super.enableControl();
	}

	/**
	 * Gets speed of the TalonSRX in RPM
	 */
	// speed = enc counts / 100 ms
	// (speed * 60 secs)
	// --------------------------------------
	// 4096 encoder counts * 100 milliseconds

	@Override
	public double getSpeed() {
		return (super.getSpeed() * 60) / (4096 * 0.1);
	}

	/**
	 * Resets the position/distance counter
	 */
	public void calibrate() {
		super.setPosition(0);
	}

	/**
	 * Initializes PID by ensuring that the feedback device is the relative mag
	 * encoder
	 * 
	 * @throws EncoderNotFoundException
	 *             if there is no encoder
	 */
	public void PIDInit() throws EncoderNotFoundException {
		if (!hasEncoder())
			throw new EncoderNotFoundException("No encoder for PIDInit");
		super.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
	}

	/**
	 * Standard PID loop
	 * 
	 * @param P
	 *            -- Proportional error coefficient
	 * @param I
	 *            -- Integral error coefficient
	 * @param D
	 *            -- Derivative error coefficient
	 */
	public void PIDUpdate(double P, double I, double D) {
		PIDUpdate(P, I, D, 0);
	}

	private void PIDUpdate(double P, double I, double D, double F) {
		super.setPID(P, I, D);
		super.enableControl();
	}

	/**
	 * Sets the position of motor Note: changes mode to POSITION
	 * 
	 * @param angle
	 *            -- angle in degrees
	 */
	public void PIDAngle(double angle) {
		PIDPosition(angle / 360.0);
	}

	/**
	 * Sets PID position of motor
	 * 
	 * @param position
	 *            -- position
	 */
	public void PIDPosition(double position) {
		super.changeControlMode(TalonControlMode.Position);
		super.set(position);
	}

	/**
	 * Sets the PID target speed and sets the rpm
	 * 
	 * @param rpm
	 */
	public void PIDSpeed(double rpm) {
		// PIDUpdate();
		// speed = RPM * 1 min/(6000 (10 milliseconds)) * 4096 encoder counts
		// /
		// revolution
		super.enableControl();
		PIDTargetSpeed = rpm;
		// double speed = rpm * (4096.0 / 6000.0);
		super.changeControlMode(TalonControlMode.Speed);
		super.set(rpm);
	}

	public double PIDErrorSpeed() {
		return PIDTargetSpeed - getSpeed();
	}

	public double PIDTargetSpeed() {
		return PIDTargetSpeed;
	}

	/**
	 * Stops motor.
	 */
	@Override
	public void stop() {
		// super.enableBrakeMode(true);
		super.disableControl();
	}

	/**
	 * @return If there is an encoder attached to this TalonSRX.
	 */
	@Override
	public boolean hasEncoder() {
		return !(encoder == null);
	}

	/**
	 * @return The encoder attached to this Talon if exists, null otherwise.
	 */
	@Override
	public Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	// TODO: make sure this works.
	@Override
	public int getChannel() {
		return super.getDeviceID();
	}

	@Override
	public double getError() {
		return super.getError();
	}

	/**
	 * @return If the Talon is reversed.
	 */
	@Override
	public boolean isReversed() {
		return super.getInverted();
	}
}
