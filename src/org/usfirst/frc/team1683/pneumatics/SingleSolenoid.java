package org.usfirst.frc.team1683.pneumatics;

public class SingleSolenoid extends edu.wpi.first.wpilibj.Solenoid {

  public SingleSolenoid(int channel) {
    super(channel);

    SingleSolenoid sol = new SingleSolenoid(0);
  }
}
