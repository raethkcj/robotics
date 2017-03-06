package edu.uwec.cs.robotics;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallFollower {
	public static DiffDriver driver;

	public static void main(String[] args) {
		driver = new DiffDriver(121, 56);
		Behavior[] behaviors = { new Follow(), new TurnRight(), new TurnLeft() };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
}