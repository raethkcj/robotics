package edu.uwec.cs.robotics;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Robot {
	public MovePilot pilot;
	public SensorModes ultrasonicSensor;
	public SampleProvider distanceProvider;
	
	public boolean moving = false;
	
	public float lastDistance;
	
	public final float INNER_THRESHOLD = 0.150f;
	public final float OUTER_THRESHOLD = 0.200f;
	public final float MAX_THRESHOLD = 0.250f;
	
	public Robot() {
		setupPilot(121, 56);
		setupSensors();
	}

	private void setupSensors() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
		distanceProvider = ultrasonicSensor.getMode("Distance");
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0);
		lastDistance = sample[0];
	}

	private void setupPilot(double wheelBase, double wheelDiameter) {
		Wheel left = WheeledChassis.modelWheel(Motor.B, wheelDiameter).offset(wheelBase / 2.0);
		Wheel right = WheeledChassis.modelWheel(Motor.C, wheelDiameter).offset(wheelBase / -2.0);
		Chassis chassis = new WheeledChassis(new Wheel[] {left, right}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(pilot.getLinearSpeed() / 2);
		pilot.setAngularSpeed(pilot.getAngularSpeed() / 2);
	}
	
}