package org.usfirst.frc.team1683.driveTrain;

@SuppressWarnings("serial")
public class GyroNotFoundException extends RuntimeException {

	public GyroNotFoundException() {
		super();
	}

	public GyroNotFoundException(String msg) {
		super(msg);
	}
}
