package org.usfirst.frc.team1683.preGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.usfirst.frc.team1683.driveTrain.PathPoint;

public class Draw extends JPanel {
	private static final long serialVersionUID = 1L;
	private static ArrayList<PathPoint> array = new ArrayList<PathPoint>();

	public Draw() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Graphics g = getGraphics();
				g.drawOval(e.getX() - 5, e.getY() - 5, 10, 10);
				array.add(new PathPoint(e.getX(), 300-e.getY()));
				g.dispose();
			}
		});
		addMouseMotionListener(new MouseInputAdapter() {
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                System.out.println(e.getX()+","+(300-e.getY()));
            }
        });
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("Draw Robot Path");
		frame.getContentPane().add(new Draw(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				File file = new File("graphpoints.txt");
				try {
					if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation has failed.");
		    		}
			        FileWriter writer = new FileWriter("graphpoints.txt", true);
			        for (PathPoint point : array) {
			        	writer.write("pathPoints.add(new PathPoint("+point.getX()+","+point.getY()+"));\n");
					}
			        writer.close();
			    } catch (IOException error) {
			        error.printStackTrace();
			    }
				try {
					File file2 = new File("graphpoints.txt");
					System.out.println(file2.exists());
					BufferedReader bufferReader = new BufferedReader(new FileReader(file2));
			        String line = bufferReader.readLine();
			        
			        while (line != null) {
			        	System.out.println(line);
			            line = bufferReader.readLine();
			        }
			        bufferReader.close();
					
				} catch (IOException readerError) {
					readerError.printStackTrace();
				} 
			}
		});
	}
}