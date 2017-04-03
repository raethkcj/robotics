package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class Follow implements Behavior {

	public static int turnAngle = -90;
	public static int direction = 0; // 0, 1, 2, 3 -> right, up, left, down
	public static float myTilesHeight = Robot.floorTilesHeight/2f;
	public static float myTilesWidth = Robot.floorTilesWidth/2f;

	@Override
	public boolean takeControl() {
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		Robot.lastDistance = sample[0];
		return true;
	}

	public static void updateSamples(float[] samples) {
		float left, front, right = 0;
		if(turnAngle > 0) {
			// samples are counter-clockwise
			right = samples[0];
			front = samples[1];
			left = samples[2];
		} else {
			// samples are clockwise
			right = samples[2];
			front = samples[1];
			left = samples[0];
		}
	}

	public static float[] scan() {
		turnAngle *= -1;
		float[] samples = new float[3];
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		samples[0] = sample[0];
		System.out.println("SCAN0: " + sample[0]);
		Robot.motor.A.rotate(turnAngle);
		Robot.distanceProvider.fetchSample(sample, 0);
		samples[1] = sample[0];
		System.out.println("SCAN1: " + sample[0]);
		Robot.motor.A.rotate(turnAngle);
		Robot.distanceProvider.fetchSample(sample, 0);
		samples[2] = sample[0];
		System.out.println("SCAN2: " + sample[0]);
		return samples;
	}

	@Override
	public void action() {
		scan();
		Robot.pilot.travel(152);
		float[] sample = new float[Robot.distanceProvider.sampleSize()];
		Robot.distanceProvider.fetchSample(sample, 0);
		Robot.lastDistance = sample[0];
	}

	@Override
	public void suppress() {
		Robot.pilot.stop();
	}

}
