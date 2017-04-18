package edu.uwec.cs.robotics.wallfollower;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class DiffDriver {
	// Both in mm
	private int wheelBase;
	private int wheelDiam;

	private EV3LargeRegulatedMotor left;
	private EV3LargeRegulatedMotor right;
	
	public DiffDriver(int wheelBase, int wheelDiam) {
		this.wheelBase = wheelBase;
		this.wheelDiam = wheelDiam;
		left = new EV3LargeRegulatedMotor(MotorPort.B);
		right = new EV3LargeRegulatedMotor(MotorPort.C);
	}
	
	/**
	 * Drive in a straight line
	 * @param distance in mm
	 * @param speed in degrees/second
	 */
	public void forward(int distance, int speed) {
		left.setSpeed(speed);
		right.setSpeed(speed);

		int degrees = (int) ((360 * distance) / (Math.PI * wheelDiam)) * Integer.signum(speed);

		left.rotate(degrees, true);
		right.rotate(degrees, true);
		left.waitComplete();
		right.waitComplete();
	}
	
	/**
	 * Make a sweeping turn around a pivot point
	 * @param radius in mm (>= 0, but possibly less than half the wheelBase)
	 * @param angle in degrees (positive)
	 * @param leftTurn
	 * @param speed in degrees/second
	 */
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
		// Original measured wheelbase = 121mm
		// Ran calibration with 126
		DiffDriver d = new DiffDriver(121, 56);
		d.forward(500, 300);
		d.turn(0, 90, false, 300);
		d.turn(500, 180, true, 300);
		d.turn(500, 180, false, 300);
		d.turn(0, 180, true, 300);
		d.turn(500, 180, true, 300);
		d.turn(500, 180, false, 300);
		d.turn(0, 90, true, 300);
		d.forward(500, 300);
	}
}
