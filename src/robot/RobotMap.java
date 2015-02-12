package robot;

import edu.wpi.first.wpilibj.DigitalSource;

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
	public static final int ROBOT_TEST       = 3;

	public static int currentRobot = ROBOT_TEST;

	/*
	 * Test Robot - 2010
	 */
	// PWM Components
	
	public static int FRONT_LEFT_MOTOR  = 2;
	public static int REAR_LEFT_MOTOR   = 3;
	public static int FRONT_RIGHT_MOTOR = 1;
	public static int REAR_RIGHT_MOTOR  = 0;
	
	public static final int ELEVATOR_MOTOR = 4;
	
	public static final int LEFT_PICKUP_MOTOR_PORT = 9;
	public static final int RIGHT_PICKUP_MOTOR_PORT = 0;
	
	// Analog I/O

	public static final int GYRO_PORT = 0;
	
	// Digital Inputs
	
	public static final int FRONT_LEFT_ENCODER_ONE = 4;
	public static final int FRONT_LEFT_ENCODER_TWO = 5;
	
	public static final int REAR_LEFT_ENCODER_ONE = 6;
	public static final int REAR_LEFT_ENCODER_TWO = 7;
	
	public static final int FRONT_RIGHT_ENCODER_ONE = 2;
	public static final int FRONT_RIGHT_ENCODER_TWO = 3;
	
	public static final int REAR_RIGHT_ENCODER_ONE = 0;
	public static final int REAR_RIGHT_ENCODER_TWO = 1;
	
	public static final int ELEVATOR_ENCODER_ONE = 0;
	public static final int ELEVATOR_ENCODER_TWO = 1;
	
	public static final int TOTE_SENSOR_PORT = 10;
	
	// Pneumatic Ports
	
	public static final int DROP_DOWN_SOLENOID_ONE = 0;
	public static final int DROP_DOWN_SOLENOID_TWO = 1;
	
	public static final int EYEBROW_SOLENOID_LEFT = 4;
	public static final int EYEBROW_SOLENOID_RIGHT = 5;
	
	public static final int BRAKE_SOLENOID = 0;
	
	// Hardware related constants

	public static final double ENCODER_COUNTS_PER_ELEVATOR_LEVEL = 1400;
	
	public static final double ENCODER_COUNTS_PER_INCH = 24;//6.7 per cm;
	
	public static final double ENCODER_RESOLUTION_CORRECTION = 1.3;
	public static final double MAX_ANUGLAR_VELOCITY = 120;
	public static final double MAX_ENCODER_RATE = 1300;
	
	public static final long EYEBROW_DEPLOY_PULSE_TIME = 800;
	public static final long EYEBROW_RETRACT_PULSE_TIME = 1200;	


	/*
	 * Production/Test robot port mappings
	 */
	// PWM Components
	/*
	public static int FRONT_LEFT_MOTOR  = 7;
	public static int REAR_LEFT_MOTOR   = 6;
	public static int FRONT_RIGHT_MOTOR = 3;
	public static int REAR_RIGHT_MOTOR  = 5;
	
	public static final int LEFT_PICKUP_MOTOR_PORT = 9;
	public static final int RIGHT_PICKUP_MOTOR_PORT = 0;
	
	// Analog I/O

	public static final int GYRO_PORT = 0;
	
	// Digital Inputs
	
	public static final int ELEVATOR_ENCODER_ONE = 0;
	public static final int ELEVATOR_ENCODER_TWO = 1;
	
	public static final int FRONT_LEFT_ENCODER_ONE = 6;
	public static final int FRONT_LEFT_ENCODER_TWO = 7;
	
	public static final int REAR_LEFT_ENCODER_ONE = 8;
	public static final int REAR_LEFT_ENCODER_TWO = 9;
	
	public static final int FRONT_RIGHT_ENCODER_ONE = 4;
	public static final int FRONT_RIGHT_ENCODER_TWO = 5;
	
	public static final int REAR_RIGHT_ENCODER_ONE = 2;
	public static final int REAR_RIGHT_ENCODER_TWO = 3;
	
	// Pneumatic Ports
	
	public static final int DROP_DOWN_SOLENOID_ONE = 1;
	public static final int DROP_DOWN_SOLENOID_TWO = 2;
	
	public static final int EYEBROW_SOLENOID_LEFT = 3;
	public static final int EYEBROW_SOLENOID_RIGHT = 4;
	
	public static final int ELEVATOR_BRAKE_SOLENOID = 0;
	// Hardware related constants

	public static final double ENCODER_COUNTS_PER_INCH = 24;//6.7 per cm;
	
	public static final double ENCODER_RESOLUTION_CORRECTION = 1.3;
	public static final double MAX_ANUGLAR_VELOCITY = 120;
	public static final double MAX_ENCODER_RATE = 1300;
	
	public static final long EYEBROW_DEPLOY_PULSE_TIME = 800;
	public static final long EYEBROW_RETRACT_PULSE_TIME = 1200;
*/
}
