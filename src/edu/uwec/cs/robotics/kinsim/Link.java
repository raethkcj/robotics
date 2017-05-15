package edu.uwec.cs.robotics.kinsim;

// Revolute joint around the z axis

import java.awt.*;
import java.awt.geom.*;

public class Link {
	
	private int width;
	private int length;
	private Color color;
	private double jointValue;
	
	public Link(int length, Color color) {
		this.width = 10; // default
		this.length = length;
		this.color = color;
		this.jointValue = 0; // default
	}
	
	public double getJointValue() {
		return jointValue;
	}

	public void setJointValue(double jointValue) {
		this.jointValue = jointValue;
	}

	public int getLength() {
		return length;
	}

	public void draw(Graphics g, AffineTransform at) {
		
		// Transform the link points
		Point2D jointPoint = null;
		jointPoint = at.transform(new Point(0, 0), jointPoint);
		
		Point2D upperLeft = null;
		upperLeft = at.transform(new Point(0, -width/2), upperLeft);
		
		Point2D lowerLeft = null;
		lowerLeft =at.transform(new Point(0, +width/2), lowerLeft);
		
		Point2D lowerRight = null;
		lowerRight =at.transform(new Point(length, +width/2), lowerRight);
		
		Point2D upperRight = null;
		upperRight =at.transform(new Point(length, -width/2), upperRight);
	
		Polygon p = new Polygon();
		p.addPoint((int)upperLeft.getX(), (int)upperLeft.getY());
		p.addPoint((int)lowerLeft.getX(), (int)lowerLeft.getY());
		p.addPoint((int)lowerRight.getX(), (int)lowerRight.getY());
		p.addPoint((int)upperRight.getX(), (int)upperRight.getY());
		
		// Draw the link
		g.setColor(color);
		g.fillPolygon(p);
		
		// Draw the joint point
		g.setColor(Color.black);
		// The -3s are to center the oval on the (x,y)
		g.fillOval((int)jointPoint.getX()-3, (int)jointPoint.getY()-3, 6, 6);
	}
	
	@Override
	public String toString() {
		return "Length: " + length + ", Angle: " + jointValue;
	}
}
