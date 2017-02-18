package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";
	private double lastDistance, lastOffset, confidence;
	private final double CONFIDENCE_SENSITIVITY = 0.1,
			DISTANCE_SENSITIVITY = 0.1,
			OFFSET_SENSITIVITY = 0.1;
	
	private InputFilter distanceFilter, offsetFilter, confidenceFilter;
	
	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);
		distanceFilter = new InputFilter(DISTANCE_SENSITIVITY, 100);
		offsetFilter = new InputFilter(OFFSET_SENSITIVITY, 0);
		confidenceFilter = new InputFilter(CONFIDENCE_SENSITIVITY, 0);
	}
	public double getDistanceTarget() {
		double distance = table.getNumber("distance", -1);
		if (distance < 0) {
			distance = lastDistance;
		}
		lastDistance = distanceFilter.filterInput(distance);
		return lastDistance;
	}
	public double getOffset() {
		double offset = table.getNumber("offset", -1);
		if (offset < 0) {
			offset = lastOffset;
		}
		lastOffset = offsetFilter.filterInput(offset);
		return lastOffset;
	}
	public double getConfidence() {
		double newConfidence = table.getNumber("confidence", -1);
		if (newConfidence == -1) {
			return 1; // confidence not implemented yet
		}
		confidence = confidenceFilter.filterInput(newConfidence);
		return confidence;
	}
}
