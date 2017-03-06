package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallFollower {
	public static void main(String[] args) {
		Behavior[] behaviors = { new Follow(), new TurnRight(), new TurnLeft() };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
}