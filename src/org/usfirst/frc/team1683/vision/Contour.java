package org.usfirst.frc.team1683.vision;

/**
 * Contour class
 *
 * @author Yi Liu
 *
 */
public class Contour {

  public final int INDEX;
  public final double HEIGHT;
  public final double WIDTH;
  public final double X_POS;
  public final double Y_POS;
  public final double AREA;

  /**
   * Constructor
   *
   * @param INDEX
   *            Index of blob
   * @param HEIGHT
   *            Height of blob
   * @param WIDTH
   *            Width of blob
   * @param X_POS
   *            X position of center of blob
   * @param Y_POS
   *            Y position of center of blob
   */
  public Contour(int INDEX, double HEIGHT, double WIDTH, double X_POS,
                 double Y_POS, double AREA) {
    this.INDEX = INDEX;
    this.HEIGHT = HEIGHT;
    this.WIDTH = WIDTH;
    this.X_POS = X_POS;
    this.Y_POS = Y_POS;
    this.AREA = AREA;
  }

  /**
   * @return String representation of Blob object. For testing purposes.
   */
  @Override
  public String toString() {
    return "Index: " + INDEX + " Height: " + HEIGHT + " Width: " + WIDTH +
        " X_POS: " + X_POS + " Y_POS: " + Y_POS;
  }
}