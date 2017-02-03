package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
// import org.usfirst.frc.team1683.sensors.LinearActuator;

public class DoNothing extends Autonomous {

	// LinearActuator actuator;

	public DoNothing(TankDrive driveTrain) {
		super(driveTrain);
	}

	@Override
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.END_CASE;
				// nextState = State.STOW_PISTONS;
				break;

			// case STOW_PISTONS:
			// if(actuator.getError() < Autonomous.ACTUATOR_ERROR_TOLERANCE )
			// nextState = State.END_CASE;
			// case END_CASE:
			// nextState = State.END_CASE;
			// break;

			default:
				break;
		}
		presentState = nextState;
	}
}
