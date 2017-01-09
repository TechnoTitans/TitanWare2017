package org.usfirst.frc.team1683.robot;

public class InputFilter {

  private double filterK;
  private double oldOutput;

  public InputFilter(double sensitivity) { this.filterK = sensitivity; }

  public InputFilter(double sensitivity, double initOldOutput) {
    this(sensitivity);
    this.oldOutput = initOldOutput;
  }

  public double getFilterK() { return filterK; }

  public void setFilterK(double k) {
    if (k > 1)
      k = 1;
    else if (k < 0)
      k = 0;
    this.filterK = k;
  }

  public double filterInput(double input) {
    double output = input + filterK * (oldOutput - input);
    oldOutput = output;
    return output;
  }
}
