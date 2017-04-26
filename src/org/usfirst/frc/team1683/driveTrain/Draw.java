package org.usfirst.frc.team1683.driveTrain;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Draw extends JPanel {
	private static final long serialVersionUID = 1L;
	public Draw() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Graphics g = getGraphics();
				g.drawOval(e.getX(), e.getY(), 2, 2);
				g.dispose();
			}
		});
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("Simple Sketching Program");
		frame.getContentPane().add(new Draw(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setVisible(true);
	}
}