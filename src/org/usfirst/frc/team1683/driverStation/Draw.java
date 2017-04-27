package org.usfirst.frc.team1683.driverStation;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.usfirst.frc.team1683.driveTrain.PathPoint;

public class Draw extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<PathPoint> array = new ArrayList<PathPoint>();

	public Draw() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Graphics g = getGraphics();
				g.drawOval(e.getX() - 5, e.getY() - 5, 10, 10);
				array.add(new PathPoint(e.getX(), e.getY()));
				g.dispose();
			}
		});
	}

	public ArrayList<PathPoint> getArray() {
		return array;
	}
}