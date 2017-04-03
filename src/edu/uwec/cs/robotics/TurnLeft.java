package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class TurnLeft implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
	//	return sample[0] > Robot.MAX_THRESHOLD;
		return false;
	}

	@Override
	public void action() {
		Robot.pilot.arc(100, 95);
		Robot.pilot.travel(250);
		//float[] sample = new float[WallFollower.distanceProvider.sampleSize()];
		//WallFollower.distanceProvider.fetchSample(sample, 0);
		//WallFollower.lastDistance = sample[0];
	}

	@Override
	public void suppress() {
		Robot.pilot.stop();
	}

}
