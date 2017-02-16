package edu.uwec.cs.robotics;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class DiffDriver {
	// Both in mm
	private int wheelBase;
	private int wheelDiam;

	private EV3LargeRegulatedMotor left;
	private EV3LargeRegulatedMotor right;
	
	private final int ACCELERATION = 500;
	
	public DiffDriver(int wheelBase, int wheelDiam) {
		this.wheelBase = wheelBase;
		this.wheelDiam = wheelDiam;
		left = new EV3LargeRegulatedMotor(MotorPort.B);
		right = new EV3LargeRegulatedMotor(MotorPort.C);
		left.setAcceleration(ACCELERATION);
		right.setAcceleration(ACCELERATION);
	}
	
	// Drive in a straight line
	// distance: mm
	// speed: degrees/second
	public void forward(int distance, int speed) {
		left.setSpeed(speed);
		right.setSpeed(speed);

		left.rotate( (int)((360 * distance) / (Math.PI * wheelDiam)), true);
		right.rotate( (int)((360 * distance) / (Math.PI * wheelDiam)), true);
		left.waitComplete();
		right.waitComplete();
	}
	
	// Make a sweeping turn around a pivot point
	// radius: mm (>= 0, but possibly less than wheelBase)
	// angle: degrees (positive)
	// speed: degrees/second 
	public void turn(int radius, int angle, Boolean leftTurn, int speed) {
		int degrees = (int) ((2 * angle * (radius == 0 ? (wheelBase / 2.0) : radius)) / wheelDiam) * (Integer.signum(speed));
		double ratioInner = (radius - wheelBase / 2.0) / (radius == 0 ? (wheelBase / 2.0) : radius);
		double ratioOuter = (radius + wheelBase / 2.0) / (radius == 0 ? (wheelBase / 2.0) : radius);

		int degreesInner = (int) (ratioInner * degrees);
		int degreesOuter = (int) (ratioOuter * degrees);

		int speedInner = (int) (ratioInner * speed);
		int speedOuter = (int) (ratioOuter * speed);

		if(leftTurn) {
			left.setSpeed(speedInner);
			right.setSpeed(speedOuter);
			left.rotate(degreesInner, true);
			right.rotate(degreesOuter, true);
		} else {
			right.setSpeed(speedInner);
			left.setSpeed(speedOuter);
			right.rotate(degreesInner, true);
			left.rotate(degreesOuter, true);
		}

		left.waitComplete();
		right.waitComplete();
	}
	
	public static void main(String[] args) throws InterruptedException {
		DiffDriver d = new DiffDriver(121, 56);
		d.forward(1000, 500);
		d.turn(0, 90, true, 500);
		d.forward(1000, 500);
		d.turn(0, 90, true, 500);
		d.forward(1000, 500);
		d.turn(0, 90, true, 500);
		d.forward(1000, 500);
		d.turn(0, 90, true, 500);
	}
}
