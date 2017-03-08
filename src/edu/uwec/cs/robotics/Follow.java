package edu.uwec.cs.robotics;

import java.util.Arrays;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class Follow implements Behavior {
	private SensorModes sensor = new EV3UltrasonicSensor(SensorPort.S1);
	private SampleProvider distance = sensor.getMode("Distance");

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Follow f = new Follow();
		float[] sample = new float[f.distance.sampleSize()];
		while (true) {
			f.distance.fetchSample(sample, 0);
			System.out.println(Arrays.toString(sample));
		}
	}

}
