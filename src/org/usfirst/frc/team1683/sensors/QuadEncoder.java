package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

//import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.FeedbackDevice;

public class QuadEncoder implements Encoder {

  private TalonSRX talonSRX;
  private double wheelRadius;

  public QuadEncoder(TalonSRX talonSRX, double wheelRadius) {
    this.talonSRX = talonSRX;
    this.talonSRX.setFeedbackDevice(
        FeedbackDevice.CtreMagEncoder_Relative);

    this.wheelRadius = wheelRadius;

  }

  @Override
  public double getDistance() {
    return talonSRX.getPosition() * 2 * Math.PI * wheelRadius;
  }
  @Override
  public double getSpeed() {
    return talonSRX.getSpeed();
  }

  @Override
  public void reset() {
    // this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    talonSRX.setPosition(0);
  }

  public TalonSRX getTalon() { return talonSRX; }
}
