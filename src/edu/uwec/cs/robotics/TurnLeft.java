package edu.uwec.cs.robotics;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class TurnLeft implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[WallFollower.distance.sampleSize()];
		WallFollower.distance.fetchSample(sample, 0);
		return sample[0] > WallFollower.MAX_THRESHOLD;
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
