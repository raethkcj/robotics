package edu.uwec.cs.robotics.wallfollower;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallFollower {
	public static MovePilot pilot;
	public static SensorModes sensor;
	public static SampleProvider distance;
	
	public static final float INNER_THRESHOLD = 0.150f;
	public static final float OUTER_THRESHOLD = 0.200f;
	public static final float MAX_THRESHOLD = 0.250f;

	private static void setupSensors() {
		sensor = new EV3UltrasonicSensor(SensorPort.S1);
		distance = sensor.getMode("Distance");
	}

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
		setupSensors();
		Behavior[] behaviors = { new Follow(), new CorrectLeft(), new CorrectRight(), new TurnLeft(), new TurnRight() };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
}
