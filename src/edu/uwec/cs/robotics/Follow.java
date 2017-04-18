package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class Follow implements Behavior {

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
	}

	@Override
	public void suppress() {
		Robot.pilot.stop();
	}

}
