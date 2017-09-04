package org.usfirst.frc.team1683.simulation;

import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.motor.Motor;
import org.usfirst.frc.team1683.sensors.Encoder;

public class SimTalon implements Motor {
	private static SimTalon[] initializedTalons = {null, null};
	private int port;
	private double setSpeed = 0,
				   actualSpeed = 0;
	private Encoder encoder;
	private boolean brakes = false;
	private double position;
	private final static double COAST_FRICTION = 0.1;
	private static final double MAX_TRUE_SPEED = 5; // inches/sec
	private static final double AVERAGE_ERROR = 0.2;
	private double bias;

	public SimTalon(int port) {
		if (port < 0 || port > initializedTalons.length) {
			throw new IllegalAccessError("Cannot access motor at port " + port);
		}
		if (initializedTalons[port] != null) {
			throw new IllegalAccessError("Motor on port " + port + " already initialized");
		}
		initializedTalons[port] = this;
		this.port = port;
		bias = (SimIterativeRobot.random.nextDouble() - 0.5) * AVERAGE_ERROR;
	}

	@Override
	public void set(double speed) {
		this.setSpeed = Math.max(-1, Math.min(1, speed));
		brakes = false;
	}

	@Override
	public double get() {
		return setSpeed;
	}

	@Override
	public void stop() {
		setSpeed = 0;
	}

	@Override
	public void brake() {
		setSpeed = 0;
		brakes = true;
	}

	@Override
	public void coast() {
		if (-COAST_FRICTION <= setSpeed && setSpeed <= COAST_FRICTION) {
			setSpeed = 0;
		} else if (setSpeed > 0) {
			setSpeed = Math.max(setSpeed - COAST_FRICTION, 0);
		} else if (setSpeed < 0) {
			setSpeed = Math.min(setSpeed + COAST_FRICTION, 0);
		}
	}

	@Override
	public boolean hasEncoder() {
		return encoder != null;
	}

	@Override
	public Encoder getEncoder() {
		return encoder;
	}

	@Override
	public int getChannel() {
		return port;
	}

	@Override
	public boolean isReversed() {
		return false;
	}

	@Override
	public void enableBrakeMode(boolean enable) {
		brakes = enable;
	}

	@Override
	public double getError() {
		return (actualSpeed - setSpeed) * MAX_TRUE_SPEED;
	}

	@Override
	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public double getPosition() {
		return position;
	}

	public double getSpeed() {
		return actualSpeed * MAX_TRUE_SPEED;
	}

	public void setPosition(double p) {
		position = p;
	}
	
	void update(double dt) {
		actualSpeed = setSpeed * ((SimIterativeRobot.random.nextDouble() - 0.5) * AVERAGE_ERROR + 1 + bias);
		position += actualSpeed * dt * MAX_TRUE_SPEED;
	}
	
	static SimTalon getTalon(int port) {
		if (initializedTalons[port] == null) {
			return new SimTalon(port);
		}
		return initializedTalons[port];
	}
}
