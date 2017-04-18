package edu.uwec.cs.robotics.wallfollower;

import lejos.robotics.subsumption.Behavior;

public class CorrectRight implements Behavior {

	@Override
	public boolean takeControl() {
		float[] sample = new float[WallFollower.distance.sampleSize()];
		WallFollower.distance.fetchSample(sample, 0);
		return sample[0] < WallFollower.INNER_THRESHOLD;
	}

	@Override
	public void action() {
		WallFollower.pilot.rotate(-2.0);
		double before_acc = WallFollower.pilot.getLinearAcceleration();
		WallFollower.pilot.setLinearAcceleration(before_acc / 2);
		WallFollower.pilot.travel(25);
		WallFollower.pilot.setLinearAcceleration(before_acc);
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
