package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.Encoder;

public abstract class Autonomous {
	public static final double GYRO_ANGLE_TOLERANCE = 15.0;

	protected TankDrive tankDrive;
	protected Encoder leftEncoder;
	protected Encoder rightEncoder;

	public State presentState = State.INIT_CASE;
	public State nextState;

	public Autonomous(TankDrive tankDrive) {
		this.tankDrive = tankDrive;
		leftEncoder = tankDrive.getLeftEncoder();
		rightEncoder = tankDrive.getRightEncoder();
		resetAuto();
	}

	// Different states the autonomous could take
	public static enum State {
		INIT_CASE, END_CASE, WAIT;
	}

	public boolean isAtEndCase() {
		return presentState == State.END_CASE;
	}

	public void resetAuto() {
		presentState = State.INIT_CASE;
	}

	public void stop() {
		presentState = State.END_CASE;
	}

	public abstract void run();
}
