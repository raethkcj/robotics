package edu.uwec.cs.robotics.kinsim;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class KinSim {
	
	private ArrayList<JSlider> sliders;

	public KinSim() {
		JFrame f = new JFrame("Kin Sim");
		f.setSize(1000, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		f.setContentPane(p);
		
		final RobotPanel rp = new RobotPanel();
		p.add(rp, BorderLayout.CENTER);
		
		final Robot r = new Robot(rp, this);
		rp.setRobot(r);
		
		rp.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {				
			}
			public void mouseEntered(MouseEvent e) {	
			}
			public void mouseExited(MouseEvent e) {	
			}
			public void mousePressed(MouseEvent e) {
				
				final Point2D p = e.getPoint();
				
				// This call has been placed in a Thread so it can
				// update the display as it moves
				Thread t = new Thread(new Runnable() {
					
					public void run() {
						// Run the inverse kin computations
						r.inverseKin(p);			
					}
				});
				t.start();	
			}
			public void mouseReleased(MouseEvent e) {	
			}
		});
	
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setPreferredSize(new Dimension(500, 500));
		int numSliders = 6;
		sliderPanel.setLayout(new GridLayout(numSliders*2, 1));
		p.add(sliderPanel, BorderLayout.EAST);
		
		// Add the joint sliders
		sliders = new ArrayList<JSlider>();
		sliders.add(null);  // add a fake one at the start since it is 1 based
		
		for (int i=1; i<=numSliders; i++) {
			sliders.add(addSlider(sliderPanel, r, rp, i));
		}
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private JSlider addSlider(JPanel sliderPanel, final Robot r, final RobotPanel rp, final int jointNumber) {
		
		JLabel jointLabel = new JLabel("Joint " + jointNumber);
		sliderPanel.add(jointLabel);
	
		JSlider jointSlider = new JSlider(JSlider.HORIZONTAL, -180, +180, 0);
		sliderPanel.add(jointSlider);		
		jointSlider.addChangeListener(new ChangeListener() {
		
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				int rotationAngle = (int)source.getValue();	
				
				// Tell the robot to change the joint angle
				r.adjustRotation(jointNumber, rotationAngle);
			}
		});

		//Turn on labels at major tick marks.
		jointSlider.setMajorTickSpacing(45);
		jointSlider.setPaintTicks(true);
		jointSlider.setPaintLabels(true);
		
		return jointSlider;
	}
	
	// Callback for the robot to adjust the slider value as it does inverse kin
	public void changeSliderValue(int sliderNumber, double value) {
		if (value < -180) {
			value = 180 - (-180 - value);
		}
		if (value > 180) {
			value = -180 + (value - 180);
		}
		sliders.get(sliderNumber).setValue((int)value);
	}

	public static void main(String[] args) {
		new KinSim();
	}

}
