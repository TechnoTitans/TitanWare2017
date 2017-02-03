package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.sensors.Encoder;
import edu.wpi.first.wpilibj.Timer;

public abstract class Autonomous {
	public static final double GYRO_ANGLE_TOLERANCE = 15.0;

	protected TankDrive tankDrive;
	protected Encoder leftEncoder;
	protected Encoder rightEncoder;

	protected Timer timer;
	protected Timer timeout;

	// public static final double ACTUATOR_ERROR_TOLERANCE = 0.05;
	// public static final double REACH_DISTANCE = 74;
	public static State presentState = State.INIT_CASE;
	public static State nextState;

	public Autonomous(TankDrive tankDrive) {
		this.tankDrive = tankDrive;
		leftEncoder = tankDrive.getLeftEncoder();
		rightEncoder = tankDrive.getRightEncoder();
	}

	public static enum State {
		INIT_CASE, END_CASE, DRIVE_FORWARD, FIND_TARGET, FIRE, REALIGN, STOP, APPROACH_GOAL, SCORE
	}

	public static enum AutonomousMode {
		DO_NOTHING, REACH_DEFENSE, BREACH_DEFENSE, TEST_AUTO, SHOOT_AT_TARGET
	}

	public static void resetAuto() {
		presentState = State.INIT_CASE;
	}
	public static void stop(){
		presentState = State.STOP;
	}

	public abstract void run();
}
