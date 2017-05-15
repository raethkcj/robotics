package edu.uwec.cs.robotics.segway;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Robot {
	public static SensorModes gyroSensor;
	public static SampleProvider gyro;
	public static SensorModes ultrasonicSensor;
	public static SampleProvider distance;
	public static EV3LargeRegulatedMotor left;
	public static EV3LargeRegulatedMotor right;
	public static final float Kp = 1.2f;
	public static final float Kd = 0.25f;
	
	private static void setupMotors() {
		left = new EV3LargeRegulatedMotor(MotorPort.D);
		right = new EV3LargeRegulatedMotor(MotorPort.A);
	}

	private static void setupSensors() {
		gyroSensor = new EV3GyroSensor(SensorPort.S2);
		gyro = gyroSensor.getMode("Rate and Angle");
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
		distance = ultrasonicSensor.getMode("Distance");
	}
	
	// PD controller
	public static float pd(float p, float d) {
		return -(p * Kp + d * Kd);
	}
	
	public static void main(String[] args) {
		setupMotors();
		setupSensors();
		while(true) {
			float[] gyroReadings = new float[gyro.sampleSize()];
			gyro.fetchSample(gyroReadings, 0);
			float rate = gyroReadings[0];
			float angle = gyroReadings[1];
			float thrust = pd(angle, rate);
			left.setSpeed(thrust);
		}
	}
}