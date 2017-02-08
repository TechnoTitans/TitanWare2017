package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;

import edu.wpi.first.wpilibj.Solenoid;
/*
 * 
 * Author: Yi Liu
 * 
 */
public class ScoreGear {
	public final double distanceToGear = 0;//TODO get proper distance
	public final double travelSpeed = 0;//TODO get proper speed
	
	DriveTrain driveTrain;
	Solenoid solenoid;
	
	public ScoreGear(DriveTrain driveTrain, Solenoid solenoid){
		this.driveTrain = driveTrain;
		this.solenoid = solenoid;
	}
	public void approachGear(){
		/*while(vision.getDistance < distanceToGear){
			drivetrain.setSpeed(travelSpeed);
		}
		drivetrain.set(0);
		*/
	}
}
