package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class CorrectRight implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		return sample[0] < Robot.lastDistance && Robot.lastDistance - sample[0] > .005;
	}

	@Override
	public void action() {
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		float lastD = Robot.lastDistance;
		
		// Try it once, continue while getting closer to parallel
		do {
			Robot.pilot.rotate(-5.0);
			lastD = sample[0];
			Robot.distanceProvider.fetchSample(sample, 0);
		} while (sample[0] < lastD);
		Robot.lastDistance = lastD;
	}

	@Override
	public void suppress() {
		Robot.pilot.stop();
	}

}
