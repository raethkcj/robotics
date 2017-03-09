package edu.uwec.cs.robotics;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallFollower {
	public static DiffDriver driver;

	public static void main(String[] args) {
		driver = new DiffDriver(121, 56);
		Behavior[] behaviors = { new Follow(), new TurnRight(), new TurnLeft() };

		Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
			}

			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});

		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
}