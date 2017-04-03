package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class RotateRight implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		//return sample[0] > Robot.MAX_THRESHOLD;
		return false;
	}

	@Override
	public void action() {
		Robot.pilot.rotate(-95);
		Robot.pilot.travel(Robot.myTilesHeight);
	}

	@Override
	public void suppress() {
		Robot.pilot.stop();
	}

}