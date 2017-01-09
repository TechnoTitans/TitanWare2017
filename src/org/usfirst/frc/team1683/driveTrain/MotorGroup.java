package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;

import org.usfirst.frc.team1683.driverStation.Settings;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Encoder;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDeviceStatus;

/**
 * Represents a group of motors that move together, tied to the same
 * gearsbox
 * and using the same encoder.
 *
 * @author David Luo
 *
 */
public class MotorGroup extends ArrayList<Motor> {

  // This variable was required for some reason.
  private static final long serialVersionUID = 1L;
  private Encoder encoder;

  private AntiDrift antiDrift;

  /**
   * Constructor
   *
   * @param encoder
   *            The encoder attached to this MotorGroup
   * @param motors
   *            The motors.
   */
  public MotorGroup(Encoder encoder, Motor... motors) {
    this.encoder = encoder;
    for (Motor motor : motors) {
      if (motor instanceof TalonSRX) {
        ((TalonSRX)motor).setEncoder(encoder);
      }
      super.add(motor);
    }
  }

  /**
   * Constructor
   *
   * @param motors
   *            The motors.
   */
  public MotorGroup(Motor... motors) {
    for (Motor motor : motors) {
      super.add(motor);
    }
  }

  /**
   * Move distance in inches.
   *
   * @param distance
   *            Distance in inches.
   * @throws EncoderNotFoundException
   *             If encoder is not found.
   *
   */
  public void moveDistance(double distance)
      throws EncoderNotFoundException {
    moveDistance(distance, Motor.MID_SPEED);
  }

  /**
   * Move distance in inches.
   *
   * @param distance
   *            Distance in inches.
   * @throws EncoderNotFoundException
   *             If encoder not found.
   */
  // TODO: make this linear instead of rotations
  public void moveDistance(double distance, double speed)
      throws EncoderNotFoundException {
    for (Motor motor : this) {
      motor.moveDistance(distance, speed);
    }
  }

  /**
   * Set collective speed of motors.
   *
   * @param speed
   *            Speed from 0 to 1.
   */
  public void set(double speed) {
    for (Motor motor : this) {
      ((TalonSRX)motor).set(speed);
    }
  }

  /**
   * Gets collective speed of motors
   */
  public double getSpeed() {
    double speed = 0;
    for (Motor motor : this) {
      speed += motor.get();
    }
    return speed / this.size();
  }

  /**
   * Stops group.
   */
  public void stop() {
    for (Motor motor : this) {
      motor.stop();
    }
  }

  /**
   * @return If there is an encoder associated with the group.
   */
  public boolean hasEncoder() { return !(encoder == null); }

  public double getError() {
    double error = 0;
    for (Motor motor : this) {
      error += ((TalonSRX)motor).getError();
    }
    error /= this.size();
    return error;
  }

  /**
   * @return The encoder associated with the group.
   */
  public Encoder getEncoder() { return encoder; }

  public void enableBrakeMode(boolean enabled) {
    for (Motor motor : this) {
      if (motor instanceof TalonSRX) {
        ((TalonSRX)motor).enableBrakeMode(enabled);
      }
    }
  }

  // TODO: probably want to find a better way to use antidrift than this
  // way.
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    return true;
  }

  public void enableAntiDrift(AntiDrift antiDrift) {
    this.antiDrift = antiDrift;
  }

  public void disableAntiDrift() { this.antiDrift = null; }

  public boolean isAntiDriftEnabled() { return !(antiDrift == null); }
}
