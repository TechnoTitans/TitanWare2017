package org.usfirst.frc.team1683.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class DriverCameraReader {
	private NetworkTable networkTable;
	public DriverCameraReader(){
		networkTable = NetworkTable.getTable("GRIP/myContoursReport");
	}
	public boolean checkForGear(){
		try {
			double[] area = networkTable.getNumberArray("area", new double[0]);
			
			if (area.length > 0) {
				return true;
			}
		} catch (TableKeyNotDefinedException exp) {
			System.out.println("TableKeyNotDefinedExceptionFix");
		}
		return false;
	}
}
