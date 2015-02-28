package robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Robot
	public static final int ROBOT_PRODUCTION = 1;
	public static final int ROBOT_PRACTICE   = 2;

	public static int currentRobot = ROBOT_PRACTICE;

	/*
	 * Production/Test robot port mappings
	 */
	// PWM Components
	
	public static int FRONT_LEFT_MOTOR  = 7;
	public static int REAR_LEFT_MOTOR   = 6;
	public static int FRONT_RIGHT_MOTOR = 3;
	public static int REAR_RIGHT_MOTOR  = 5;
	
	public static final int TOTE_ELEVATOR_MOTOR = 4;
	public static final int CONTAINER_ELEVATOR_MOTOR = 8;
	
	public static final int LEFT_PICKUP_MOTOR_PORT = 9;
	public static final int RIGHT_PICKUP_MOTOR_PORT = 0;
	
	public static final int LEFT_CONTAINER_MOTOR_PORT = 1;
	public static final int RIGHT_CONTAINER_MOTOR_PORT = 2;
	
	// Analog I/O

	public static final int GYRO_PORT = 0;
	
	// Digital Inputs
	
	public static final int TOTE_ELEVATOR_ENCODER_ONE = 0;
	public static final int TOTE_ELEVATOR_ENCODER_TWO = 1;
	
	public static final int CONTAINER_ELEVATOR_ENCODER_ONE = 20;
	public static final int CONTAINER_ELEVATOR_ENCODER_TWO = 21;
	
	public static final int FRONT_LEFT_ENCODER_ONE = 6;
	public static final int FRONT_LEFT_ENCODER_TWO = 7;
	
	public static final int REAR_LEFT_ENCODER_ONE = 8;
	public static final int REAR_LEFT_ENCODER_TWO = 9;
	
	public static final int FRONT_RIGHT_ENCODER_ONE = 4;
	public static final int FRONT_RIGHT_ENCODER_TWO = 5;
	
	public static final int REAR_RIGHT_ENCODER_ONE = 2;
	public static final int REAR_RIGHT_ENCODER_TWO = 3;
	
	public static final int TOTE_SENSOR_PORT = 15;
	
	public static final int CONTAINER_SENSOR_PORT = 14;
	
	public static final int TOTE_ELEVATOR_UPPER_LIMIT_SWITCH = 11;
	public static final int TOTE_ELEVATOR_LOWER_LIMIT_SWITCH = 10;
	
	public static final int COMPRESSOR_SPIKE_PORT = 0;
	public static final int PRESSURE_SWICH_PORT = 19;
	
	public static final int DISTANCE_SENSOR_LEFT = 12;
	public static final int DISTANCE_SENSOR_RIGHT = 13;
	
	// Pneumatic Ports
	
	public static final int DROP_DOWN_SOLENOID_ONE = 1;
	public static final int DROP_DOWN_SOLENOID_TWO = 2;
	
	public static final int EYEBROW_SOLENOID_LEFT = 3;
	public static final int EYEBROW_SOLENOID_RIGHT = 4;
	
	public static final int BRAKE_SOLENOID = 0;
	
	public static final int CONTAINER_PINCHER_PORT = 5;
	public static final int CONTAINER_DEPLOY_PORT_ONE = 6;
	public static final int CONTAINER_DEPLOY_PORT_TWO = 7;
	
	// Power Distribution Ports
	public static final int TOTE_ELEVATOR_POWER_DISTRIBUTION_PORT = 13;
	
	// Hardware related constants

	public static final double ENCODER_COUNTS_PER_INCH = 24;//6.7 per cm;
	
	public static final double ENCODER_RESOLUTION_CORRECTION = 1.3;
	public static final double MAX_ANUGLAR_VELOCITY = 120;
	public static final double MAX_ENCODER_RATE = 2500;//900;
	
	public static final long EYEBROW_DEPLOY_PULSE_TIME = 1200;
	public static final long EYEBROW_RETRACT_PULSE_TIME = 2000;
	
	public static final double TOTE_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE = 2000;
	public static final double TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL = 850;
	public static final double TOTE_ELEVATOR_MAX_DISTANCE = 3350;
	public static final double TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL = 1300; //2200 3000
	
	public static final double CONTAINER_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE = 100;
	
	public static final double MAX_DRIVE_ACCELERATION = .5; // change in motor speed control / sec
	public static final double MAX_TELEOP_DRIVE_ACCELERATION = 1.0; // change in motor speed control / sec
	
	public static final double PICKUP_ROLLER_SPEED = 0.5;
	
}
