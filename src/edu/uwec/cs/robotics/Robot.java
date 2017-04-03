package edu.uwec.cs.robotics;

import java.awt.Point;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Robot {
	public static MovePilot pilot;
	public static SensorModes ultrasonicSensor;
	public static SampleProvider distanceProvider;
	public static Motor motor;
	public static int[][] map;
	public static Point goal = new Point(6,8);
	public static float floorTilesHeight = 12 * 2.54f / 100; // 12 inches to meters
	public static float floorTilesWidth = 12 * 2.54f / 100;
	public static int nFloorTilesX = 9;
	public static int nFloorTilesY = 7;
	public static float myTilesHeight = Robot.floorTilesHeight/2f;
	public static float myTilesWidth = Robot.floorTilesWidth/2f;
	
	public static boolean moving = false;
	
	public static float lastDistance;
	
	public static final float INNER_THRESHOLD = 0.150f;
	public static final float OUTER_THRESHOLD = 0.200f;
	public static final float MAX_THRESHOLD = 0.250f;

	private static void setupSensors() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
		distanceProvider = ultrasonicSensor.getMode("Distance");
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0);
		lastDistance = sample[0];
	}

	private static void setupPilot(double wheelBase, double wheelDiameter) {
		Wheel left = WheeledChassis.modelWheel(Motor.B, wheelDiameter).offset(wheelBase / 2.0);
		Wheel right = WheeledChassis.modelWheel(Motor.C, wheelDiameter).offset(wheelBase / -2.0);
		Chassis chassis = new WheeledChassis(new Wheel[] {left, right}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(pilot.getLinearSpeed() / 2);
		pilot.setAngularSpeed(pilot.getAngularSpeed() / 2);
		Robot.map = new int[nFloorTilesX][nFloorTilesY];
	}

	public static void main(String[] args) {

		Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
			}

			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});

		setupPilot(121, 56);
		setupSensors();
		initMap();
		Behavior[] behaviors = { new Follow() };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.go();
	}
	
	
	public static void initMap() {
		for(int[] row : map) {
			Arrays.fill(row, 0);
		}
		map[goal.y][goal.x] = 2;
	}
}