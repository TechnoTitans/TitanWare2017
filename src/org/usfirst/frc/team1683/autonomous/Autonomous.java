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
	public State presentState = State.INIT_CASE;
	public State nextState;

	public Autonomous(TankDrive tankDrive) {
		this.tankDrive = tankDrive;
		leftEncoder = tankDrive.getLeftEncoder();
		rightEncoder = tankDrive.getRightEncoder();
		org.usfirst.frc.team1683.vision.PiVisionReader visionReader = new org.usfirst.frc.team1683.vision.PiVisionReader();
		
		resetAuto();
	}

	public static enum State {
		INIT_CASE, END_CASE, DRIVE_FORWARD, FIND_TARGET, FIRE, REALIGN, STOP, APPROACH_GOAL, SCORE, DRIVE_FORWARD_WAITING, DRIVE_PATH;
	}

	public static enum AutonomousMode {
		DO_NOTHING, EDGE_GEAR, EDGE_GEAR2, SQUARE_AUTO; 
	}
	
	public boolean isAtEndCase() {
		return presentState == State.END_CASE;
	}

	public void resetAuto() {
		presentState = State.INIT_CASE;
	}
	public void stop(){
		presentState = State.STOP;
	}
	public void turnGreenLightOn() {
		
	}

	public abstract void run();
}
