package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

/*
 * 
 * Not used
 * 
 */
public class AccelSPI extends edu.wpi.first.wpilibj.ADXL362 {

	public AccelSPI(SPI.Port port, Accelerometer.Range range) {
		super(port, range);
		// TODO Auto-generated constructor stub
	}

	public AccelSPI(Accelerometer.Range range) {
		super(range);
		super.initTable(getTable());
	}

	public double getAngleXZ() {
		return Math.atan2(getX(), getZ()) * 180 / Math.PI;
	}

	public double getAngleYZ() {
		return Math.atan2(getY(), getZ()) * 180 / Math.PI;
	}

	@Override
	public double getX() {
		return super.getAccelerations().XAxis;
	}

	@Override
	public double getY() {
		return super.getAccelerations().YAxis;
	}

	@Override
	public double getZ() {
		return super.getAccelerations().ZAxis;
	}
}
