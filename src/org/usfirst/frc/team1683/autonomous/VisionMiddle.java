package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.vision.PiVisionReader;

import edu.wpi.first.wpilibj.Timer;

public class VisionMiddle extends Autonomous {
	GearScore gearScore;
	PiVisionReader piReader;
	DriveTrainMover mover;
	
	Timer waitTimer;
	
	Timer timer;
	Timer timer2;
	
	public VisionMiddle(TankDrive tankDrive, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;
		timer = new Timer();
		timer2 = new Timer();
		
		waitTimer = new Timer();
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				gearScore = new GearScore(tankDrive, 0.3, piReader, 1.4, 0, 0, "middle");
				nextState = State.APPROACH_GOAL;
				break;
			case APPROACH_GOAL:
				gearScore.run();
				gearScore.enable();
				if (gearScore.isDone()) {
					gearScore.disable();
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, -1, 0.3);
					nextState = State.WAIT;
				}
				break;
			case WAIT:
				tankDrive.stop();
				if (waitTimer.get() > 0.1) {
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, -1, 0.3);
					nextState = State.BACK_UP;
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.END_CASE;
					waitTimer.start();
				}
				break;
			case END_CASE:
				tankDrive.stop();
				break;
			default:
				break;

		}
		SmartDashboard.sendData("Auto Timer", timer.get(), true);
		SmartDashboard.sendData("Auto State", presentState.toString(), true);
		presentState = nextState;
	}
}