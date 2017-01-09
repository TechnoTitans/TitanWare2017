package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Encoder;

/**
 * Class to represent the Talons attached to motors.
 *
 * @author David Luo
 * @deprecated Talons are now discontinued.
 *
 */
@Deprecated
public class Talon extends edu.wpi.first.wpilibj.Talon implements Motor {

  private Encoder encoder;
  private Thread thread;

  /**
   * Private class to move motor in separate thread.
   *
   * @author David Luo
   */
  private class MotorMover implements Runnable {

    private double distance;
    private double speed;
    private Talon talon;

    public MotorMover(Talon talon, double distance, double speed) {
      this.talon = talon;
      this.distance = distance;
      if (distance < 0)
        this.speed = -speed;
      else
        this.speed = speed;
    }

    @Override
    public void run() {
      encoder.reset();
      // synchronized (this) {
      while (Math.abs(encoder.getDistance()) < Math.abs(distance)) {
        talon.set(speed);
      }
      // }
      talon.stop();
      // notify();
      // encoder.reset();
    }
  }

  /**
   * Constructor
   *
   * @param channel
   *            The port where the Talon is plugged in.
   * @param reversed
   *            If the Talon should invert the signal.
   */
  public Talon(int channel, boolean reversed) {
    super(channel);
    super.setInverted(reversed);
  }

  /**
   * Constructor
   *
   * @param channel
   *            The port where the Talon is plugged in.
   * @param reversed
   *            If the Talon should invert the signal.
   * @param encoder
   *            Encoder to attach to this Talon.
   */
  public Talon(int channel, boolean reversed, Encoder encoder) {
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
      }
    } else {
      throw new EncoderNotFoundException();
    }
  }

  /**
   * Set the speed of the Talon.
   *
   * @param Speed
   *            from 0 to 1.
   */
  @Override
  public void set(double speed) {
    super.set(speed);
  }

  /**
   * Gets speed of the Talon
   */
  @Override
  public double get() {
    return super.get();
  }

  /**
   * Stops motor.
   */
  @Override
  public void stop() {
    super.stopMotor();
  }

  /**
   * @return If there is an encoder attached to this Talon.
   */
  @Override
  public boolean hasEncoder() {
    return !encoder.equals(null);
  }

  /**
   * @return The encoder attached to this Talon if exists, null otherwise.
   */
  @Override
  public Encoder getEncoder() {
    return encoder;
  }

  /**
   * @return The channel this Talon is attatched to.
   */
  @Override
  public int getChannel() {
    return super.getChannel();
  }

  /**
   * @return If the Talon is reversed.
   */
  @Override
  public boolean isReversed() {
    return super.getInverted();
  }
}
