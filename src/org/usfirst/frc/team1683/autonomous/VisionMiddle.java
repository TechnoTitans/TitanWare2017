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
	private boolean shakeRight = true;
	
	Timer timer;
	Timer timer2;
	
	public VisionMiddle(TankDrive tankDrive, PiVisionReader piReader) {
		super(tankDrive);
		this.piReader = piReader;
		timer = new Timer();
		timer2 = new Timer();
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				gearScore = new GearScore(tankDrive, 0.3, piReader, 1.7, 0.0001, 0, "middle");
				nextState = State.APPROACH_GOAL;
				break;
			case APPROACH_GOAL:
				gearScore.run();
				gearScore.enable();
				if (gearScore.isDone()) {
					gearScore.disable();
					tankDrive.stop();
					mover = new DriveTrainMover(tankDrive, -1, 0.3);
				}
				break;
			case BACK_UP:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.SHAKE;
					timer.start();
					timer2.start();
				}
				break;
			case SHAKE:
				if (timer2.get() > 3) {
					nextState = State.END_CASE;
					tankDrive.stop();
				} else {
					tankDrive.turnInPlace(shakeRight, 0.15);
					if (timer.get() > 0.18) {
						shakeRight = !shakeRight;
						timer.reset();
					}
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