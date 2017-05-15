package edu.uwec.cs.robotics.kinsim;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Robot {

	RobotPanel rp;
	KinSim ks;

	ArrayList<Link> links;

	public Robot(RobotPanel rp, KinSim ks) {
		// Remember the robot panel and the kin sim GUI for repainting and slider adjustment
		this.rp = rp;
		this.ks = ks;
		
		// Define the links of the robot
		links = new ArrayList<Link>();
		links.add(new Link(0, Color.black));  // There is no link 0 so we just put a 0 length Link in this position to represent the base
		
		// Add the actual links (hard-coded)
		links.add(new Link(30, Color.red));
		links.add(new Link(30, Color.black));
		links.add(new Link(30, Color.yellow));
		links.add(new Link(30, Color.red));
		links.add(new Link(30, Color.black));
		links.add(new Link(30, Color.yellow));
	}
	
	// Callback for the GUI sliders to perform forward kin
	public void adjustRotation(int joint, double value) {
		if (joint < links.size()) {
			links.get(joint).setJointValue(value);
		
			// Draw the result on the screen
			rp.repaint();
		}
	}
	
	private double calculateDistance(List<Double> angles, Point2D goal) {
		AffineTransform origin = AffineTransform.getTranslateInstance(250, 250);
		for(int i = 0; i < links.size(); i++) {
			origin.rotate(Math.toRadians(angles.get(i)));
			origin.translate(links.get(i).getLength(), 0);
		}
		Point2D tip = null;
		tip = origin.transform(new Point(0, 0), tip);
		return tip.distance(goal);
	}
	
	private List<Double> getLinkAngles() {
		return links.stream().map(Link::getJointValue).collect(Collectors.toList());
	}
	
	// Should be coded using gradient following
	// The following link for inverse kin gradient following is useful:
	// http://freespace.virgin.net/hugo.elias/models/m_ik2.htm
	//
	// Remember that the call to this method is threaded so you can
	// repaint the robotPanel and update the kinsim GUI's sliders as you
	// update the joint positions.
	// You might also want to include a slight delay (Thread.sleep) to make
	// the motion look smoother.
	public void inverseKin(Point2D goal) {
		double dist = calculateDistance(getLinkAngles(), goal);
		
		ArrayList<Double> oldGradients = new ArrayList<Double>(Collections.nCopies(links.size(), 0.0));
		double maxGradient = Double.MAX_VALUE;
		do {
			ArrayList<Double> speeds = new ArrayList<Double>(Collections.nCopies(links.size(), 0.0));
			for(int i = 1; i < links.size(); i++) {
				List<Double> angles = getLinkAngles();
				angles.set(i, angles.get(i) + 1);
				double up = calculateDistance(angles, goal);
				angles.set(i, angles.get(i) - 2);
				double down = calculateDistance(angles, goal);
				angles.set(i, angles.get(i) + 1);

				double gradient = up - down;
				
//				if sign(old_gradient_a) != sign(gradient_a) then
//			    	a -= speeda * old_gradient_a / (gradient_a-old_gradient_a)
//			        speeda = 0
//			    else
//			        speeda += ga
				if(oldGradients.get(i) * gradient < 0) {
					double newAngle = links.get(i).getJointValue() - (speeds.get(i) * oldGradients.get(i)) / (gradient - oldGradients.get(i));
					ks.changeSliderValue(i, Math.round(newAngle));
					speeds.set(i, 0.0);
				} else {
					speeds.set(i, speeds.get(i) + gradient);
				}
				
				oldGradients.set(i, gradient);
				
//				a -= speed_a
				int newAngle = (int) Math.round(links.get(i).getJointValue() - speeds.get(i));
				ks.changeSliderValue(i, newAngle);
			}
			
			dist = calculateDistance(getLinkAngles(), goal);
			maxGradient = oldGradients.stream().map((g) -> Math.abs(g)).max(Double::compare).get();

			try {
				Thread.sleep(5);
			} catch(InterruptedException e) {}
		} while(dist > 3 && maxGradient > 0.5);
	}
	

	// Draw is called by the robotPanel when it needs to display the links on the screen
	// You need to setup the proper AffineTransform and pass it to each link as the chain gets drawn
	// Note that these AffineTransforms are only planar (3x3 rather than 4x4 matrices).
	// You may hard-code that the base of the robot is at (250,250)
	// Note that you do not need to draw link 0, but may need its information for forming the transforms
	public void draw(Graphics g) {
		AffineTransform currentOrigin = AffineTransform.getTranslateInstance(250, 250);
		for(Link link : links) {
			currentOrigin.rotate(Math.toRadians(link.getJointValue()));
			link.draw(g, currentOrigin);
			currentOrigin.translate(link.getLength(), 0);
		}
	}
		
}