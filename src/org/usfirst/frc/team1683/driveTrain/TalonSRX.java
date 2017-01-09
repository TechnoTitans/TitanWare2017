package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.shooter.Shooter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

/**
 * Class to represent TalonSRXs attatched to motors.
 *
 * @author David Luo
 *
 */
public class TalonSRX extends CANTalon implements Motor {

  private Encoder encoder;
  private Thread thread;
  private boolean calibrated = false;
  private double PIDTargetSpeed;

  /**
   * Private class to move motor in separate thread.
   *
   * @author David Luo
   *
   */
  private class MotorMover implements Runnable {

    private double distance;
    private double speed;
    private TalonSRX talonSrx;
    private Timer timer;
    // private Encoder encoder;

    public MotorMover(TalonSRX talonSrx, double distance, double speed) {
      this.talonSrx = talonSrx;
      this.distance = distance;
      timer = new Timer();
      // this.encoder = encoder;
      if (distance < 0)
        this.speed = -speed;
      else
        this.speed = speed;
    }

    @Override
    public void run() {
      encoder.reset();
      // synchronized (this) {
      // TODO: make not magic number
      timer.start();
      // while (Math.abs(encoder.getDistance()) < Math.abs(distance)) {
      while (Math.abs(encoder.getDistance()) < Math.abs(distance) &&
             timer.get() < 3) {
        talonSrx.set(speed);
        // SmartDashboard.sendData("encoder val",
        // encoder.getDistance());
        // do nothing
      }
      // }
      talonSrx.stop();
      // notify();

      encoder.reset();
    }
  }

  /**
   * Constructor
   *
   * @param channel
   *            The port where the TalonSRX is plugged in.
   * @param reversed
   *            If the TalonSRX should invert the signal.
   */
  public TalonSRX(int channel, boolean reversed) {
    super(channel);
    super.setInverted(reversed);
  }

  /**
   * Constructor
   *
   * @param channel
   *            The port where the TalonSRX is plugged in.
   * @param reversed
   *            If the TalonSRX should invert the signal.
   * @param encoder
   *            Encoder to attach to this TalonSRX.
   */
  public TalonSRX(int channel, boolean reversed, Encoder encoder) {
    super(channel);
    super.setInverted(reversed);
    this.encoder = encoder;
  }

  /**
   * Move distance in inches
   *
   * @param distance
   *            Distance in inches
   */
  @Override
  public void moveDistance(double distance)
      throws EncoderNotFoundException {
    moveDistance(distance, Motor.MID_SPEED);
  }

  /**
   * Move distance in inches
   *
   * @param distance
   *            Distance in inches.
   * @param speed
   *            Speed from 0 to 1.
   */
  @Override
  public void moveDistance(double distance, double speed)
      throws EncoderNotFoundException {

    if (hasEncoder()) {
      if (thread == null ||
          thread.getState().equals(Thread.State.TERMINATED)) {
        thread = new Thread(new MotorMover(this, distance, speed));
      }
      if (thread.getState().equals(Thread.State.NEW)) {
        thread.start();

        // synchronized (thread) {
        // try {
        // thread.wait();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        SmartDashboard.sendData("EncoderNotFound", false);
      }
    } else {
      SmartDashboard.sendData("EncoderNotFound", true);
      throw new EncoderNotFoundException();
    }
  }

  /**
   * Set the speed of the TalonSRX.
   *
   * @param speed
   *            Speed from 0 to 1.
   */
  @Override
  public void set(double speed) {
    super.changeControlMode(TalonControlMode.PercentVbus);
    super.set(speed);
    super.enableControl();
  }

  /**
   * Gets speed of the TalonSRX in RPM
   */
  // @Override
  // public double get() {
  // speed = enc counts / 100 ms
  // (speed * 60 secs)
  // --------------------------------------
  // 4096 encoder counts * 100 milliseconds
  // }

  @Override
  public double getSpeed() {
    // return this.get();
    return (super.getSpeed() * 60) / (4096 * 0.1);
  }

  public double getRPM() { return this.get(); }

  public void calibrate() {
    super.setPosition(0);
    calibrated = true;
  }

  public void PIDInit() throws EncoderNotFoundException {
    // if
    // (!super.isSensorPresent(FeedbackDevice.PulseWidth).equals(FeedbackDeviceStatus.FeedbackStatusPresent))
    // {
    // SmartDashboard.sendData("Encoder on Talon " + getChannel() + "
    // not found", true);
    // throw new EncoderNotFoundException();
    // } else
    // SmartDashboard.sendData("Encoder on Talon " + getChannel() + "
    // not found", false);
    // super.setFeedbackDevice(FeedbackDevice.QuadEncoder);

    // super.setInverted(false);
    // super.configEncoderCodesPerRev(4096);
    // calibrate();
    super.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    // super.configEncoderCodesPerRev(4096);
    // super.setPosition(0);
  }

  public void PIDUpdate(double P, double I, double D) {
    PIDUpdate(P, I, D, 0);
  }

  public void PIDUpdate(double P, double I, double D, double F) {
    super.setPID(P, I, D);
    super.enableControl();
  }

  public void PIDAngle(double angle) { PIDPosition(angle / 360.0); }

  public void PIDPosition(double position) {
    // PIDUpdate();
    super.changeControlMode(TalonControlMode.Position);
    if (!Shooter.errorFlag) {
      super.set(position);
      SmartDashboard.sendData("Talon " + this.getChannel() + " Position",
                              super.getPosition());
    }
  }

  public void PIDSpeed(double rpm) {
    // PIDUpdate();
    // speed = RPM * 1 min/(6000 (10 milliseconds)) * 4096 encoder counts
    // /
    // revolution
    super.enableControl();
    PIDTargetSpeed = rpm;
    // double speed = rpm * (4096.0 / 6000.0);

    // SmartDashboard.sendData(this.getChannel() + "RPM", rpm);
    // SmartDashboard.sendData(this.getChannel() + "Speed", speed);
    super.changeControlMode(TalonControlMode.Speed);
    super.set(rpm);
    // SmartDashboard.sendData("Talon " + this.getChannel() + " Speed",
    // getSpeed());
  }

  public double PIDErrorSpeed() { return PIDTargetSpeed - getSpeed(); }

  public double PIDTargetSpeed() { return PIDTargetSpeed; }

  /**
   * Stops motor.
   */
  @Override
  public void stop() {
    // super.enableBrakeMode(true);
    super.disableControl();
  }

  /**
   * @return If there is an encoder attached to this TalonSRX.
   */
  @Override
  public boolean hasEncoder() {
    return !(encoder == null);
  }

  /**
   * @return The encoder attached to this Talon if exists, null otherwise.
   */
  @Override
  public Encoder getEncoder() {
    return encoder;
  }

  public void setEncoder(Encoder encoder) { this.encoder = encoder; }

  // TODO: make sure this works.
  @Override
  public int getChannel() {
    return super.getDeviceID();
  }

  @Override
  public double getError() {
    return super.getError();
  }

  /**
   * @return If the Talon is reversed.
   */
  @Override
  public boolean isReversed() {
    return super.getInverted();
  }
}
