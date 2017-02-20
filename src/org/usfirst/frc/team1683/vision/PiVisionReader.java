package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";

	private static class VisionValue {
		private double value;
		private boolean receivedOne;
		private String name;
		private NetworkTable table;
		private double sensitivity;

		VisionValue(String name, NetworkTable table, double sensitivity) {
			this.name = name;
			this.table = table;
			this.sensitivity = sensitivity;
			value = 0;
			receivedOne = false;
		}

		void update() {
			double inp = table.getNumber(name, -1);
			double confidence = table.getNumber("Confidence", 0) * this.sensitivity;
			if (confidence > 0) {
				if (!receivedOne) {
					// If this is the first value, there is no other value to go
					// off of
					value = inp;
					receivedOne = true;
				}
				value += confidence * (inp - value);
			}
		}

		double getValue() {
			return value;
		}

		void log() {
			SmartDashboard.sendData(name + "_unfiltered", table.getNumber(name, -1));
			SmartDashboard.sendData(name + "_filtered", getValue());
		}
	}

	private VisionValue targetCenter, distance, confidence;

	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);
		targetCenter = new VisionValue("X_Offset_From_Center", table, 0.6);
		distance = new VisionValue("Distance", table, 0.9);
		confidence = new VisionValue("Confidence", table, 0.3);
	}

	/**
	 * 
	 * @return An offset between -0.5 and 0.5 where negative indicates target is
	 *         on the left
	 */
	public double getOffset() {
		return targetCenter.getValue() / 100.0;
	}

	public double getDistanceTarget() {
		return distance.getValue();
	}

	public void update() {
		targetCenter.update();
		distance.update();
		confidence.update();
	}

	public void log() {
		targetCenter.log();
		distance.log();
		confidence.log();
	}

	public double getConfidence() {
		return confidence.getValue();
	}
}
