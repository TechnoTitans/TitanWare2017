package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.driveTrain.TalonSRX;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class QuadEncoder implements Encoder {

  public static final double PULSES_PER_REVOLUTION = 1024.0;
  private TalonSRX talonSRX;
  private double wheelRadius;

  public QuadEncoder(TalonSRX talonSRX, double wheelRadius) {
    // if (talonSRX.isSensorPresent(FeedbackDevice.QuadEncoder) ==
    // FeedbackDeviceStatus.FeedbackStatusPresent) {
    this.talonSRX = talonSRX;
    // } else {
    // throw new EncoderNotFoundException();
    // this.talonSRX = null;
    // }
    this.talonSRX.setFeedbackDevice(
        FeedbackDevice.CtreMagEncoder_Relative);

    this.wheelRadius = wheelRadius;

    this.talonSRX.configEncoderCodesPerRev((int)PULSES_PER_REVOLUTION);
  }

  @Override
  public double getDistance() {
    // double pos = talonSRX.getEncPosition();
    // return ;
    // this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    return talonSRX.getPosition() * 2 * Math.PI * wheelRadius;
  }

  public double getPosition() {
    // this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    double pos = talonSRX.getEncPosition();
    // if (pos > 0) {
    // return (pos % PULSES_PER_REVOLUTION) / PULSES_PER_REVOLUTION *
    // 360.0;
    // } else {
    // return Math.abs(360.0 - (pos % PULSES_PER_REVOLUTION) /
    // PULSES_PER_REVOLUTION * 360.0);
    // }
    return pos / PULSES_PER_REVOLUTION;
  }

  @Override
  public double getSpeed() {
    // TODO Auto-generated method stub
    // this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    return talonSRX.getSpeed();
  }

  @Override
  public void reset() {
    // this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    talonSRX.setPosition(0);
    // talonSRX.setpos
  }

  public TalonSRX getTalon() { return talonSRX; }
}
