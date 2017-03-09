package edu.uwec.cs.robotics;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallFollower {
	public static MovePilot pilot;

	private static void setupPilot(double wheelBase, double wheelDiameter) {
		Wheel left = WheeledChassis.modelWheel(Motor.B, wheelDiameter).offset(wheelBase / 2.0);
		Wheel right = WheeledChassis.modelWheel(Motor.C, wheelDiameter).offset(wheelBase / -2.0);
		Chassis chassis = new WheeledChassis(new Wheel[] {left, right}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(pilot.getLinearSpeed() / 2);
		pilot.setAngularSpeed(pilot.getAngularSpeed() / 2);
	}

	public static void main(String[] args) {

		Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
			}

			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});

		setupPilot(121, 56);
		Behavior[] behaviors = { new Follow(), new TurnLeft(), new TurnRight() };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
}