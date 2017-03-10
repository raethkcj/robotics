package edu.uwec.cs.robotics;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class TurnRight implements Behavior {
	private SensorModes sensor;
	private SampleProvider touch;

	public TurnRight() {
		sensor = new EV3TouchSensor(SensorPort.S2);
		touch = sensor.getMode("Touch");
	}

	@Override
	public boolean takeControl() {
		float[] sample = new float[touch.sampleSize()];
		touch.fetchSample(sample, 0);
		return sample[0] == 1f;
	}

	@Override
	public void action() {
		// Back up (to avoid turning into the wall)
		WallFollower.pilot.travel(-160);
		// Turn right
		WallFollower.pilot.rotate(-90);
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
