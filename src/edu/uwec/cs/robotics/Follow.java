package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class Follow implements Behavior {
	
	boolean parallelish = true;

	@Override
	public boolean takeControl() {
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		WallFollower.lastDistance = sample[0];
		return true;
	}

	@Override
	public void action() {
		WallFollower.pilot.travel(1000);
		float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		WallFollower.distanceProvider.fetchSample(sample, 0);
		WallFollower.lastDistance = sample[0];
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
