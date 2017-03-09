package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class Follow implements Behavior {

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		WallFollower.pilot.travel(1000);;
	}

	@Override
	public void suppress() {
		WallFollower.pilot.stop();
	}

}
