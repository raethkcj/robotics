package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class CorrectLeft implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		return sample[0] > WallFollower.lastDistance && sample[0] - WallFollower.lastDistance > 0.005;
	}

	@Override
	public void action() {
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		float lastD = WallFollower.lastDistance;
		
		// Try it once, continue while getting closer to parallel
		do {
			WallFollower.pilot.rotate(5.0);
			lastD = sample[0];
			WallFollower.distanceProvider.fetchSample(sample, 0);
		} while (sample[0] < lastD);
		WallFollower.lastDistance = lastD;
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
