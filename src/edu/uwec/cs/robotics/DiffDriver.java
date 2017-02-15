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
		left.synchronizeWith(new RegulatedMotor[] {right});

		left.startSynchronization();
		left.rotate( (int)((360 * distance) / (Math.PI * wheelDiam)), true);
		right.rotate( (int)((360 * distance) / (Math.PI * wheelDiam)), true);
		left.endSynchronization();
	}
	
	// Make a sweeping turn around a pivot point
	// radius: mm (>= 0, but possibly less than wheelBase)
	// angle: degrees (positive)
	// speed: degrees/second 
	public void turn(int radius, int angle, Boolean leftTurn, int speed) {
	}
	
	public static void main(String[] args) throws InterruptedException {
		DiffDriver d = new DiffDriver(150, 56);
		d.forward(100, 500);
	}
}
