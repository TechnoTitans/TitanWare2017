package org.usfirst.frc.team1683.vision;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.vision.Contour;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class FindGoal {
  public static NetworkTable tableContour;
  private double GOAL_X, GOAL_Y, HEIGHT, WIDTH, AREA;
  private double distance = 0;
  private double defaultvalue = 0;
  private double optic_angle =
      28.393 * Math.PI / 180; // M1103 cameras only
  // private double optic_angle = Math.toRadians(67.0/2.0); // M1103
  // cameras
  // only

  private double optic_angle1 = 0 * Math.PI / 180; // small black camera
  private double FOVpx = 320; // pixels of the grip program
  private double Targetin = 20; // target width
  private final double CENTER_WIDTH_PX = 5; // The max offset
  private final double SHOOTER_HEIGHT = 0; // TODO

  private InputFilter distanceFilter;
  /*
   * go to
   * https://wpilib.screenstepslive.com/s/4485/m/24194/l/288985-identifying-
   * and-processing-the-targets
   */

  public FindGoal() {
    tableContour = NetworkTable.getTable("RoboRealm");
    distanceFilter = new InputFilter(.95);
  }

  private Contour getData() {
    Contour contour;
    try {
      AREA = tableContour.getNumber("AREA", defaultvalue);
      GOAL_X = tableContour.getNumber("COG_X", defaultvalue);
      GOAL_Y = tableContour.getNumber("COG_Y", defaultvalue);
      WIDTH = tableContour.getNumber("WIDTH", defaultvalue);
      HEIGHT = tableContour.getNumber("HEIGHT", defaultvalue);
      contour = new Contour(0, HEIGHT, WIDTH, GOAL_X, GOAL_Y, AREA);
      // if (AREA.length > 0) {
      // for (int i = 0; i < contour.length; i++) {
      // contour[i] = new Contour(i,
      // HEIGHT[i],
      // WIDTH[i],
      // GOAL_X[i],
      // GOAL_Y[i],
      // AREA[i]);
      // }
      // }
      SmartDashboard.sendData("AREA", AREA);
      SmartDashboard.sendData("HEIGHT", HEIGHT);
      SmartDashboard.sendData("GOAL_X", GOAL_X);
      SmartDashboard.sendData("GOAL_Y", GOAL_Y);
      SmartDashboard.sendData("WIDTH", WIDTH);
    } catch (TableKeyNotDefinedException exp) {
      System.out.println("TableKeyNotDefinedExceptionFix");
      // SmartDashboard.sendData("", val);
      contour = null;
    }
    return contour;
  }

  public int getClosestContour(Contour[] contours) {
    int maxarea = 0;
    for (int i = 0; i < contours.length; i++) {
      if (contours[maxarea].AREA > contours[i].AREA) {
        maxarea = i;
      }
    }
    return maxarea;
  }

  /*
   * checks distance to target not to base of target
   */
  public double getDistance() {
    Contour contour = getData();

    SmartDashboard.sendData("contour width", contour.WIDTH);
    this.distance =
        Targetin * FOVpx / (2 * contour.WIDTH * Math.tan(optic_angle));
    // try {
    // } catch (ArrayIndexOutOfBoundsException ex) {
    //
    // } catch (NullPointerException e) {
    // this.distance = -1;
    // }
    return this.distance;
  }

  public double getFilteredDistance() {
    double distance = getDistance();

    if (distance < 20 * 12) {
      distanceFilter.setFilterK(
          SmartDashboard.getDouble("distanceFilterK"));
      distance = distanceFilter.filterInput(distance);
    }

    return distance;
  }

  //
  // public double ShooterSpeed(){
  // TODO:Test values for shooter. Plot points on graphical analysis and
  // take
  // derivative.
  // }
  public double getHeight() {
    Contour contours = getData();
    double cameratarget;
    cameratarget = 20 * (contours.Y_POS - 160) / contours.WIDTH;
    return (cameratarget - SHOOTER_HEIGHT);
  }

  public int getOffset() {
    Contour contours = getData();
    try {
      return (int)(contours.X_POS - FOVpx / 2);
    } catch (ArrayIndexOutOfBoundsException ex) {
      return Integer.MAX_VALUE;
    } catch (NullPointerException e) {
      return Integer.MIN_VALUE;
    }
  }

  public boolean isCentered() {
    // Contour[] contours = getData();
    double offset = getOffset();
    // offset = FOVpx / 2 - contours[ClosestContour(contours)].X_POS;
    // SmartDashboard.sendData("Offset", offset);
    // if (offset < -CENTER_WIDTH_PX) {
    // return 1;
    // } else if (offset > CENTER_WIDTH_PX) {
    // return -1;
    // } else if ((offset > -CENTER_WIDTH_PX) && (offset <
    // CENTER_WIDTH_PX))
    // {
    // return 0;
    // } else {
    // return 2;
    // }

    if (Math.abs(offset) < CENTER_WIDTH_PX) return true;
    return false;
  }
}
