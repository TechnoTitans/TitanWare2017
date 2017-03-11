package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.TechnoTitan;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousChooser {
	public Autonomous selectedAuto;
	public SendableChooser chooser;

	public AutonomousChooser() {
		chooser = new SendableChooser();
	}
	public void setDefault(String name, Autonomous auto){
		chooser.addDefault(name, auto);	
	}
	
	@SuppressWarnings("unused")
	public void addObject(String name, Autonomous auto, boolean forCompetition){
		double warning; // I hate warnings
		if (!(TechnoTitan.isCompetitionTime && !forCompetition)) {
			chooser.addObject(name, auto);
		}
	}
	
	public void getSelected(){
		selectedAuto = (Autonomous) chooser.getSelected();
	}
	
	public void sendDashboard(){
		SmartDashboard.putData("Choose Auto", chooser);
	}
}
