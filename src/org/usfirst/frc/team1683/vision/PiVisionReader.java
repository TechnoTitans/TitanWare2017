package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";
	private static final double CONFIDENCE_SENSITIVITY = 0.3;
	
	private static class VisionValue {
		private InputFilter filter, confidence;
		private String name;
		private NetworkTable table;
		VisionValue(String name, NetworkTable table, double sensitivity) {
			this.name = name;
			this.table = table;
			filter = new InputFilter(sensitivity);
			confidence = new InputFilter(CONFIDENCE_SENSITIVITY);
		}
		void update() {
			double inp = table.getNumber(name, -1);
			if (inp <= 0) {
				confidence.filterInput(0);
			} else {
				confidence.filterInput(1);
				filter.filterInput(inp);
			}
		}
		double getConfidence() {
			return confidence.getLastOutput();
		}
		double getValue() {
			return filter.getLastOutput();
		}
		void log() {
			SmartDashboard.sendData(name + "_filtered", getValue());
			SmartDashboard.sendData(name + "_filtered_confidence", getConfidence());
		}
	}
	private static class Camera {
		private VisionValue cx1, cx2, cy1, cy2;
		private final double CONFIDENCE_THRESHOLD = 0.4;
		private final double CENTER_SENSITIVITY = 0.8; 
		private final double CAMERA_WIDTH = 640;
		Camera(int id, NetworkTable table) {
			String camId = "Cam" + id + "_";
			cx1 = new VisionValue(camId + "Left_Center_X", table, CENTER_SENSITIVITY);
			cx2 = new VisionValue(camId + "Right_Center_X", table, CENTER_SENSITIVITY);
			cy1 = new VisionValue(camId + "Left_Center_Y", table, CENTER_SENSITIVITY);
			cy2 = new VisionValue(camId + "Right_Center_Y", table, CENTER_SENSITIVITY);
		}
		boolean isConfident() {
			return cx1.getConfidence() > CONFIDENCE_THRESHOLD && cx2.getConfidence() > CONFIDENCE_THRESHOLD;
		}
		boolean isSemiConfident() {
			// xor operation, if either camera is confident but not both
			return (cx1.getConfidence() > CONFIDENCE_THRESHOLD) != (cx2.getConfidence() > CONFIDENCE_THRESHOLD);
		}
		boolean isUnconfident() {
			return cx1.getConfidence() < CONFIDENCE_THRESHOLD && cx2.getConfidence() < CONFIDENCE_THRESHOLD;
		}
		VisionValue getGoodCenter() {
			if (cx1.getConfidence() > CONFIDENCE_THRESHOLD) return cx1;
			else if (cx2.getConfidence() > CONFIDENCE_THRESHOLD) return cx2;
			else return null;
		}
		boolean isGoodCenter1() {
			return getGoodCenter() == cx1;
		}
		double getOffset() {
			if (isConfident()) {
				double center = (cx1.getValue() + cx2.getValue()) / 2;
				return center / CAMERA_WIDTH;
			} else if (isSemiConfident()) {
				return getOffset(getGoodCenter());
			} else {
				return -1;
			}
		}
		double getOffset(VisionValue known) {
			return known.getValue();
		}
		/**
		 * Gets offset using only 1 cx
		 * @param useCx1 If true, will use only cx 1 (left center), otherwise will use only cx 2
		 * @return
		 */
		double getOffset(boolean useCx1) {
			return getOffset(useCx1 ? cx1 : cx2);
		}
		
		double getConfidence() {
			return Math.max(cx1.getConfidence(), cx2.getConfidence());
		}
		void update() {
			cx1.update();
			cx2.update();
			cy1.update();
			cy2.update();
		}
		public void log() {
			cx1.log();
			cx2.log();
		}
	}
	
	private Camera left, right;
	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);
		left = new Camera(1, table);
		right = new Camera(2, table);
	}
	
	public double getOffset() {
		boolean useOnlyOne = false, useCx1 = false;
		if (right.isUnconfident() && left.isUnconfident()) return -1;
		if (right.isSemiConfident()) {
			useOnlyOne = true;
			useCx1 = right.isGoodCenter1();
		}
		if (left.isSemiConfident()) {
			useOnlyOne = true;
			useCx1 = !left.isGoodCenter1();
		}
		SmartDashboard.sendData("using only one", useOnlyOne);
		if (useOnlyOne) {
			SmartDashboard.sendData("using left center", useCx1);
			// left and right should use opposite nodes
			return 1 - (right.getOffset(useCx1) + left.getOffset(!useCx1));
		} else {
			return right.getOffset() + left.getOffset();
		}
	}
	public double getDistanceTarget() {
		return 1;
	}
	public void update() {
		left.update();
		right.update();
	}
	public void log() {
		left.log();
		right.log();
	}
	public double getConfidence() {
		return (left.getConfidence() + right.getConfidence()) / 2;
	}
}
