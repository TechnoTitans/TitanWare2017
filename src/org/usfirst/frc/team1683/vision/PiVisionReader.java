package org.usfirst.frc.team1683.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";
	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);
	}
	public double getTime() {
		return table.getNumber("dsTime", -1.0);
	}
	public double getDistanceTarget() {
		return table.getNumber("distance", 1e20);
	}
	public double getOffset() {
		return table.getNumber("offset", 1e20);
	}
	public boolean isNull() {
		return table.getBoolean("OnScreen", false);
	}
	public void greenLight() {
		
	}
}
