package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class CorrectRight implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		float lastD = WallFollower.lastDistance;
		//WallFollower.lastDistance = sample[0];
		return sample[0] < lastD;// && .001 >= Math.abs(sample[0] - WallFollower.lastDistance);
	}

	@Override
	public void action() {
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		float lastD = WallFollower.lastDistance;
		
		do {
			WallFollower.pilot.rotate(-5.0);
			lastD = sample[0];
			WallFollower.distanceProvider.fetchSample(sample, 0);
		} while (sample[0] <= lastD);
		WallFollower.lastDistance = lastD;
		//WallFollower.pilot.rotate(-5.0);
		//WallFollower.pilot.travel(5);
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
