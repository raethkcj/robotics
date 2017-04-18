package edu.uwec.cs.robotics.kinsim;

import java.awt.*;

import javax.swing.*;

public class RobotPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Robot r;
	
	public RobotPanel() { 
	}
	
	public void setRobot(Robot r) {
		this.r = r;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (r != null) {
			r.draw(g);
		}
	}
}
