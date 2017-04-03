package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Behavior;

public class WavefrontPlanner implements Behavior {
	
	@Override
	public boolean takeControl() {
		return false;
	}

	@Override
	public void action() {
	}

	@Override
	public void suppress() {
	}
}
