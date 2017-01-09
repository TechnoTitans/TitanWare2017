package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;

public class PressureReader implements Sensor {
  private final double MAX_VOLTAGE = 4.5; // volts
  private final double MIN_VOLTAGE = 0.46; // volts
  private final double MIN_PRESSURE = 0; // PSI
  private final double MAX_PRESSURE = 200; // PSI
  private double averageVoltage;
  private double portNumber;
  AnalogInput sensor;
  private double pressure;
  private final double VOLTAGE_RANGE = MAX_VOLTAGE - MIN_VOLTAGE;
  private final double PRESSURE_SLOPE = MAX_PRESSURE / VOLTAGE_RANGE;

  public PressureReader(int portNumber) {
    this.portNumber = portNumber;
    sensor = new AnalogInput(portNumber);
  }

  // gets port number and returns it
  public double getPortNumber() { return portNumber; }

  // gets voltage and returns average as an Analog Value
  @Override
  public double getRaw() {
    return sensor.getAverageVoltage();
  }

  public double getPressure() {
    boolean isAttached = true;
    averageVoltage = sensor.getAverageVoltage();
    if (averageVoltage > MAX_VOLTAGE) {
      pressure = MAX_PRESSURE;
    } else if (averageVoltage < MIN_VOLTAGE) {
      pressure = MIN_PRESSURE;
      isAttached = false;
    } else {
      pressure = PRESSURE_SLOPE * (averageVoltage - MIN_VOLTAGE);
      isAttached = true;
    }
    SmartDashboard.putNumber("Pressure", pressure);
    SmartDashboard.putBoolean("SensorAttached", isAttached);
    return pressure;
  }
}
