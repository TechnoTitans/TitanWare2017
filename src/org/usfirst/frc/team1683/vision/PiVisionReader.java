package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";

	private static class VisionValue {
		private double value = 0;
		private boolean receivedOne = false;

		private String cam;
		private String name;
		private NetworkTable table;
		private double sensitivity;

		VisionValue(String cam, String input, NetworkTable table, double sensitivity) {
			this.cam = cam;
			this.name = (cam + input);
			this.table = table;
			this.sensitivity = sensitivity;
		}

		void update() {
			String confidenceName = cam + "_Confidence";
			double networkValue = table.getNumber(name, -1);
			double confidence = table.getNumber(confidenceName, 0) * this.sensitivity;

			if (name.equals(confidenceName))
				confidence = 1;
			if (confidence > 0) {
				if (!receivedOne) {
					value = networkValue;
					receivedOne = true;
				}
				value += confidence * (networkValue - value);
			}
		}

		double getValue() {
			return value;
		}
	}

	private VisionValue targetCenter1, targetCenter2, distance1, distance2, confidence1, confidence2;

	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);

		targetCenter1 = new VisionValue("Cam1", "_X_Offset", table, 1.0);
		targetCenter2 = new VisionValue("Cam2", "_X_Offset", table, 1.0);
		distance1 = new VisionValue("Cam1", "_Distance", table, 0.1);
		distance2 = new VisionValue("Cam2", "_Distance", table, 0.1);
		confidence1 = new VisionValue("Cam1", "_Confidence", table, 0.9);
		confidence2 = new VisionValue("Cam2", "_Confidence", table, 0.9);
	}

	/**
	 * 
	 * @return An offset between -0.5 and 0.5 where negative indicates target is
	 *         on the left
	 */
	private final double ONE_CAM_OFFSET = 0.5;

	public double getOffset() {
		double offset1 = targetCenter1.getValue();
		double offset2 = targetCenter2.getValue();

		double offset = 0.0;
		if (offset1 == 0.0 && offset2 == 0.0) {
			offset = 0.0;
		} else if (offset1 == 0.0) {
			offset = (offset2 + ONE_CAM_OFFSET) / 100;
		} else if (offset2 == 0.0) {
			offset2 = (offset1 - ONE_CAM_OFFSET) / 100;
		} else {
			offset = (offset1 + offset2) / 100.0;
		}
		SmartDashboard.sendData("vision offset", offset, false);
		return offset;
	}

	public double getDistanceTarget() {
		return (distance1.getValue() + distance2.getValue()) / 2;
	}

	public void update() {
		targetCenter1.update();
		targetCenter2.update();
		distance1.update();
		distance2.update();
		confidence1.update();
		confidence2.update();
	}

	public double getConfidence() {
		return Math.max(confidence1.getValue(), confidence2.getValue());
	}
}
