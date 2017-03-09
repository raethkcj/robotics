package edu.uwec.cs.robotics;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class TurnLeft implements Behavior {
	private SensorModes sensor;
	private SampleProvider distance;

	public TurnLeft() {
		sensor = new EV3UltrasonicSensor(SensorPort.S1);
		distance = sensor.getMode("Distance");
	}

	@Override
	public boolean takeControl() {
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		return sample[0] > 0.2286f;
	}

	@Override
	public void action() {
		WallFollower.pilot.arc(100, 95);
		WallFollower.pilot.travel(250);
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
