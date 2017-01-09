package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;

public class TestAuto extends Autonomous {

  public TestAuto(TankDrive tankDrive) {
    super(tankDrive);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void run() {
    switch (presentState) {
      case INIT_CASE:
        nextState = State.DRIVE_FORWARD;
        break;
      case DRIVE_FORWARD:
        // tankDrive.set(0.1);
        tankDrive.moveDistance(2 * Math.PI * 2 * 5, 0.5);

        // try {
        // tankDrive.moveDistance(100, 0.05);
        // } catch (EncoderNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        nextState = State.END_CASE;
        break;
      case END_CASE:
      default:
        nextState = State.END_CASE;
        break;
    }
    presentState = nextState;
  }
}
