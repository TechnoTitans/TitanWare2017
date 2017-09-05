package org.usfirst.frc.team1683.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.usfirst.frc.team1683.constants.Constants;
import org.usfirst.frc.team1683.robot.TechnoTitan;

public class SimIterativeRobot {
	private static SimIterativeRobot self = null;
	public static final int FRAME_WIDTH = 720;
	public static final int FRAME_HEIGHT = 720;
	public static final double PIXELS_PER_INCH =  2.5;
	public static final double FRAMES_PER_SECOND = 30;
	public static final double ROBOT_HEIGHT = 40;
	public static Random random = new Random();
	private static int frames = 0;
	public static double debug = 0;
	private boolean initAuto = false;
	
	private Vector2D positionLeft, positionRight;
	
	private boolean imageFound;
	private BufferedImage image;
	private ImageObserver observer;
	
	public SimIterativeRobot() {
		// Put the robot at the center
		positionLeft = new Vector2D(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
		positionRight = new Vector2D(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
		// convert to inches
		positionLeft.multiply(1 / PIXELS_PER_INCH);
		positionLeft.add(-Constants.ROBOT_WIDTH / 2, 0);
		positionRight.multiply(1 / PIXELS_PER_INCH);
		positionRight.add(Constants.ROBOT_WIDTH / 2, 0);
		try {
			image = ImageIO.read(new File("robot.png"));
			imageFound = true;
		} catch (IOException e) {
			imageFound = false;
			System.out.println("The robot image was not found.");
			e.printStackTrace();
		}
		observer = new ImageObserver() {
			
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		};
	}
	
	public void robotInit() {
		System.out.println("Override me");
	}
	
	public void autonomousInit() {
		System.out.println("Override me");
	}
	
	public void autonomousPeriodic() {
		System.out.println("Override me");
	}
	public static SimIterativeRobot get() {
		return self;
	}
	
	public static void startSim() {
		JFrame frame = new JFrame("simulation");
		JComponent drawer = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				for (double x = 0; x < FRAME_WIDTH; x += PIXELS_PER_INCH * 12) {
					g.drawLine((int) x, 0, (int) x, FRAME_HEIGHT);
				}
				for (double y = 0; y < FRAME_HEIGHT; y += PIXELS_PER_INCH * 12) {
					g.drawLine(0, (int) y, FRAME_WIDTH, (int) y);
				}
				g.drawString("Time: " + frames / FRAMES_PER_SECOND, 20, 20);
				self.paintComponent(g);
			}
		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        self = new TechnoTitan();
        self.robotInit();
        //Display the window.
        frame.getContentPane().add(drawer);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
        java.util.Timer t = new java.util.Timer();
        t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				self.update();
				frames++;
				frame.repaint();
			}
		}, 0, 10);
	}
	
	private void line(Graphics g, double x1, double y1, double x2, double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
	
	public void update() {
		if (!initAuto) {
			autonomousInit();
			initAuto = true;
		}
		autonomousPeriodic();
		TalonSRX left = TalonSRX.getTalon(0),
				 right = TalonSRX.getTalon(1);
		double dt = 1 / FRAMES_PER_SECOND;
		left.update(dt);
		right.update(dt);
		double leftSpeed = left.getSpeed(),
			   rightSpeed = right.getSpeed();
		Vector2D diff = getDifferenceVector();
		Vector2D facing = diff.getPerpendicularUnitVector();
		if (Math.abs(leftSpeed - rightSpeed) < 0.001) { // avoid floating point errors
			facing.multiply((leftSpeed + rightSpeed) * dt / 2);
			positionLeft.add(facing);
			positionRight.add(facing);
		} else {
			double theta = dt * (rightSpeed - leftSpeed) / Constants.ROBOT_WIDTH;
			Vector2D center = diff;
			center.normalize();
			center.multiply(Constants.ROBOT_WIDTH * rightSpeed / (rightSpeed - leftSpeed));
			center.multiply(-1);
			center.add(positionRight);
			positionLeft.rotateAbout(theta, center);
			positionRight.rotateAbout(theta, center);
		}
	}
	
	public Vector2D getDifferenceVector() {
		Vector2D diff = positionRight.copy();
		diff.subtract(positionLeft);
		return diff;
	}
	
	protected void paintComponent(Graphics g) {
		int lx = (int) (positionLeft.x * PIXELS_PER_INCH),
			ly = (int) (FRAME_HEIGHT - positionLeft.y * PIXELS_PER_INCH),
			rx = (int) (positionRight.x * PIXELS_PER_INCH),
			ry = (int) (FRAME_HEIGHT - positionRight.y * PIXELS_PER_INCH),
			x = (lx + rx) / 2,
			y = (ly + ry) / 2,
			w = (int) (Constants.ROBOT_WIDTH * PIXELS_PER_INCH),
			h = (int) (ROBOT_HEIGHT * PIXELS_PER_INCH);
		Graphics2D gg = (Graphics2D) g;
		AffineTransform oldXForm = gg.getTransform();
		Vector2D diffVec = getDifferenceVector();
		gg.rotate(-diffVec.getAngle(), x, y);
		if (!imageFound) {
			gg.setColor(Color.BLACK);
		    gg.fillRect(x - w/2, y - h/2, w, h);
		    gg.setColor(Color.WHITE);
		    line(gg, x - w / 2.5, y - h / 2.5, x + w / 2.5, y - h / 2.5);
		} else {
			g.drawImage(image, x - w/2, y - h/2, w, h, observer);
		}
	    gg.setTransform(oldXForm);
	    gg.fillOval(lx - 3, ly - 3, 6, 6);
	    gg.fillOval(rx - 3, ry - 3, 6, 6);
	}

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() 
	    {
	        public void run() 
	        {
	        	startSim();
	        }
	    });
	}
}
