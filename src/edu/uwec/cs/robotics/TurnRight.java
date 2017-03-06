package edu.uwec.cs.robotics;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class TurnRight implements Behavior {
	private SensorModes sensor = new EV3TouchSensor(SensorPort.S2);
	private SampleProvider touch = sensor.getMode("Touch");

	@Override
	public boolean takeControl() {
		float[] sample = new float[touch.sampleSize()];
		touch.fetchSample(sample, 0);
		return sample[0] == 1f;
	}

	@Override
	public void action() {
		// Back up (to avoid turning into the wall)
		WallFollower.driver.forward(-200, 300);
		// Turn right
		WallFollower.driver.turn(0, 90, false, 300);
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
