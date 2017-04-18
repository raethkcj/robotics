package edu.uwec.cs.robotics.wavefront;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.Motor;

public class WavefrontPlanner {
	public static int[][] map;
	public static int turnAngle = -90;
	public static int direction = 0; // 0, 1, 2, 3 -> right, up, left, down
	public static int[] goal = new int[] { 12, 16, 2 };
	public static Point currentPosition = new Point(0, 0);
	public static int nFloorTilesX = 9;
	public static int nFloorTilesY = 7;
	public static int nGridSquaresX = nFloorTilesX * 2;
	public static int nGridSquaresY = nFloorTilesY * 2;
	public static final float FLOOR_TILES_SIDE_LENGTH = 12 * 2.54f / 100; // 12 inches to meters
	public static float gridSquareSideLength = FLOOR_TILES_SIDE_LENGTH / 2;
	public static Queue<int[]> q = new LinkedList<>();
	public static Robot r;

	public static float[] scan() {
		turnAngle *= -1;
		float[] samples = new float[3];
		float[] sample = new float[r.distanceProvider.sampleSize()];
		r.distanceProvider.fetchSample(sample, 0);
		samples[0] = sample[0];
		System.out.println("SCAN0: " + sample[0]);
		Motor.A.rotate(turnAngle);
		r.distanceProvider.fetchSample(sample, 0);
		samples[1] = sample[0];
		System.out.println("SCAN1: " + sample[0]);
		Motor.A.rotate(turnAngle);
		r.distanceProvider.fetchSample(sample, 0);
		samples[2] = sample[0];
		System.out.println("SCAN2: " + sample[0]);
		return samples;
	}

	public static int getMeasurementDir(int measureIndex) {
		int measurementDir = 0;
		if (direction == 0) {
			if (measureIndex == 0) { measurementDir = 1; }
			if (measureIndex == 1) { measurementDir = 0; }
			if (measureIndex == 2) { measurementDir = 3; }
		}
		if (direction == 1) {
			if (measureIndex == 0) { measurementDir = 2; }
			if (measureIndex == 1) { measurementDir = 1; }
			if (measureIndex == 2) { measurementDir = 0; }
		}
		if (direction == 2) {
			if (measureIndex == 0) { measurementDir = 3; }
			if (measureIndex == 1) { measurementDir = 2; }
			if (measureIndex == 2) { measurementDir = 1; }
		}
		if (direction == 3) {
			if (measureIndex == 0) { measurementDir = 0; }
			if (measureIndex == 1) { measurementDir = 3; }
			if (measureIndex == 2) { measurementDir = 2; }
		}
		return measurementDir;
	}

	public static void lookForObstacles() {
		float[] samples = scan();
		float[] measurements = new float[3];
		if (turnAngle > 0) {
			// samples are counter-clockwise
			measurements[0] = samples[0];
			measurements[1] = samples[1];
			measurements[2] = samples[2];
		} else {
			// samples are clockwise
			measurements[0] = samples[2];
			measurements[1] = samples[1];
			measurements[2] = samples[0];
		}

		for (int i = 0; i < measurements.length; i++) {
			int measurementDir = getMeasurementDir(i);
			if (Float.isInfinite(measurements[i])) {
				measurements[i] = 999;
			}
			if (measurements[i] < gridSquareSideLength) {
				measurements[i] = gridSquareSideLength;
			}
			int gridDistance = Math.round(measurements[i] / gridSquareSideLength);
			Point potentialObstacle = currentPosition.getLocation();
			switch (measurementDir) {
			case 0:
				potentialObstacle.y += gridDistance;
				break;
			case 1:
				potentialObstacle.x += gridDistance;
				break;
			case 2:
				potentialObstacle.y -= gridDistance;
				break;
			case 3:
				potentialObstacle.x -= gridDistance;
				break;
			default:
				break;
			}
			if (potentialObstacle.x >= 0 && potentialObstacle.x < nGridSquaresY && potentialObstacle.y >= 0
					&& potentialObstacle.y < nGridSquaresX) {
				map[potentialObstacle.x][potentialObstacle.y] = 1;
			}

		}
	}

	public static int realMod(int a, int b) {
		return ((a % b) + b) % b;
	}

	public static double angleTo(Point p) {

		int newVectorX = p.x - currentPosition.x;
		int newVectorY = p.y - currentPosition.y;

		int dirToNewPoint = 0;

		if (newVectorX == 1) {
			dirToNewPoint = 0;
		}
		if (newVectorX == -1) {
			dirToNewPoint = 2;
		}
		if (newVectorY == 1) {
			dirToNewPoint = 1;
		}
		if (newVectorY == -1) {
			dirToNewPoint = 3;
		}

		double angle = 0;
		int diff = dirToNewPoint - direction;
		if (direction == dirToNewPoint) {
			angle = 0;
		} else if (realMod(diff, 4) == 1) {
			angle = 90;
		} else if (realMod(diff, 4) == 2) {
			angle = 180;
		} else if (realMod(diff, 4) == 3) {
			angle = -90;
		}

		return angle;
	}

	public static void travelTo(Point p) {
		double angle = angleTo(p);
		if(angle > 89.0 && angle < 91.0) {
			direction = (direction + 1) % 4;
		} else if(angle > 179.0 && angle < 181.0) {
			direction = (direction + 2) % 4;
		} else if(angle > -91.0 && angle < -89.0) {
			direction = (direction + 3) % 4;
		}
		r.pilot.rotate(angle);
		if(!currentPosition.equals(p)) {
			r.pilot.travel(gridSquareSideLength * 1000, false);
		}
		currentPosition = p;
	}

	public static void wavefront() {
		q.add(goal);
		while (q.peek() != null) {
			wavefrontQueue();
		}
	}

	public static void wavefrontQueue() {
		int[] data = q.remove();
		int x = data[0];
		int y = data[1];
		int newVal = data[2] + 1;
		if (isValid(x + 1, y)) {
			map[x + 1][y] = newVal;
			q.add(new int[] { x + 1, y, newVal });
		}
		if (isValid(x - 1, y)) {
			map[x - 1][y] = newVal;
			q.add(new int[] { x - 1, y, newVal });
		}
		if (isValid(x, y + 1)) {
			map[x][y + 1] = newVal;
			q.add(new int[] { x, y + 1, newVal });
		}
		if (isValid(x, y - 1)) {
			map[x][y - 1] = newVal;
			q.add(new int[] { x, y - 1, newVal });
		}
	}

	public static Point getNextDirection(Point curr) {
		Point next = new Point(curr.x, curr.y);
		if (isValidMove(curr.x, curr.y + 1)) {
			next = new Point(curr.x, curr.y + 1);
		}
		if (isValidMove(curr.x, curr.y - 1) && map[curr.x][curr.y - 1] < map[next.x][next.y]) {
			next = new Point(curr.x, curr.y - 1);
		}

		if (isValidMove(curr.x + 1, curr.y) && map[curr.x + 1][curr.y] < map[next.x][next.y]) {
			next = new Point(curr.x + 1, curr.y);
		}
		if (isValidMove(curr.x - 1, curr.y) && map[curr.x - 1][curr.y] < map[next.x][next.y]) {
			next = new Point(curr.x - 1, curr.y);
		}
		return next;
	}

	public static void printMap(int[][] mapLocal) {
		for (int i = 0; i < mapLocal.length; i++) {
			for (int j = 0; j < mapLocal[0].length; j++) {
				System.out.printf("%2d ", mapLocal[i][j]);
			}
			System.out.println();
		}
		System.out.println("\n");
	}

	public static void printQueue() {
		for (int[] a : q) {
			System.out.println(Arrays.toString(a));
		}
	}

	public static boolean isValidMove(int x, int y) {
		boolean valid = true;
		if (x >= nGridSquaresX || x < 0) {
			return false;
		}
		if (valid && y >= nGridSquaresY || y < 0) {
			return false;
		}
		if (valid && map[x][y] == 1) {
			valid = false;
		}
		return valid;
	}

	public static boolean isValid(int x, int y) {
		boolean valid = true;
		if (x >= map.length || x < 0) {
			return false;
		}
		if (valid && y >= map[0].length || y < 0) {
			return false;
		}

		if (valid) {
		} else {
		}
		if (valid && map[x][y] != 0) {
			valid = false;
		}
		return valid;
	}

	public static void initMap() {
		map = new int[nGridSquaresY][nGridSquaresX];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] != 1 || map[x][y] != 2) {
					map[x][y] = 0;
				}
			}
		}
		map[goal[0]][goal[1]] = goal[2];
	}

	public static void zeroMap() {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] != 1 || map[x][y] != 2) {
					map[x][y] = 0;
				}
			}
		}
		map[goal[0]][goal[1]] = goal[2];
	}

	public static void addQuitButton() {
		Button.ENTER.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
			}

			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		r = new Robot();
		addQuitButton();
		initMap();
		Point goalPoint = new Point(goal[0], goal[1]);
		while (!currentPosition.equals(goalPoint)) {
			lookForObstacles();
			wavefront();
//			printMap(map);
			System.out.println("current position: [" + currentPosition.x + "," + currentPosition.y + "]");
			Point next = getNextDirection(currentPosition);
			travelTo(next);
		}
	}
}