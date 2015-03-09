package robot.subsystems;

import robot.Timer;
import robot.LinearRamp;
import robot.MockSpeedController;
import robot.OffsetableGyro;
import robot.PolarCoordinate;
import robot.RobotMap;
import robot.RunnymedeMecanumDrive;
import robot.Timer;
import robot.commands.TeleopDriveCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The chassis subsystem contains the drive motors and uses the gyro sensor.
 */
public class ChassisSubsystem extends RunnymedeSubsystem {

	public enum DriveMode { FIELD_RELATIVE, ROBOT_RELATIVE }
	public enum PIDEnable { ENABLED, DISABLED }
	private enum Units     { INCHES, ENCODER_COUNTS };

	private boolean subsystemEnabled = false;

	// MOTORS 

	// Motor definitions for arrays
	private static final int FRONT_LEFT  = 0;
	private static final int REAR_LEFT   = 1;
	private static final int FRONT_RIGHT = 2;
	private static final int REAR_RIGHT  = 3;
	private static final int MOTOR_COUNT = 4;

	private boolean MOTOR_INVERTED     = false;
	private boolean MOTOR_NOT_INVERTED = true;

	// Motors

	private Talon [] talonArr = {
			new Talon(RobotMap.FRONT_LEFT_MOTOR), 
			new Talon(RobotMap.REAR_LEFT_MOTOR),
			new Talon(RobotMap.FRONT_RIGHT_MOTOR),
			new Talon(RobotMap.REAR_RIGHT_MOTOR)   };
	
	private boolean [] motorInversionArr = {
			MOTOR_NOT_INVERTED,
			MOTOR_NOT_INVERTED,
			MOTOR_INVERTED,
			MOTOR_INVERTED };

	// Mecanum Drive for the robot configuration.

	private RunnymedeMecanumDrive mecanumDrive = new RunnymedeMecanumDrive(motorInversionArr);
	
	// SENSORS

	// Encoders
	// PID Input and Output are between -1 and 1, so we divide the rate by the max rate
	// to normalize the encoder output so it can be compared with the motor drive input and output range
	private Encoder [] encoderArr = {
			new Encoder(RobotMap.FRONT_LEFT_ENCODER_ONE, RobotMap.FRONT_LEFT_ENCODER_TWO, true){
				public double pidGet() {
					return this.getRate() / RobotMap.MAX_ENCODER_RATE;
				}
			},
			new Encoder(RobotMap.REAR_LEFT_ENCODER_ONE,  RobotMap.REAR_LEFT_ENCODER_TWO,  true){ //CORRECTION ON TEST ROBOT
				public double pidGet() {
					return this.getRate() / RobotMap.MAX_ENCODER_RATE;
				}
			},
			new Encoder(RobotMap.FRONT_RIGHT_ENCODER_ONE,RobotMap.FRONT_RIGHT_ENCODER_TWO,true){
				public double pidGet() {
					return this.getRate() / RobotMap.MAX_ENCODER_RATE;
				}
			},
			new Encoder(RobotMap.REAR_RIGHT_ENCODER_ONE, RobotMap.REAR_RIGHT_ENCODER_TWO, true){
				public double pidGet() {
					return this.getRate() / RobotMap.MAX_ENCODER_RATE;
				}
			}};

	// Gyro
	private OffsetableGyro gyro = new OffsetableGyro(RobotMap.GYRO_PORT);

	// PIDS and PID outputs

	// AnglePID

	private MockSpeedController anglePIDOutput    = new MockSpeedController();

	private PIDController anglePID = new PIDController(0.02, 0.0, 0.0, 0.0,
			new PIDSource() {
		public double pidGet() {
			return gyro.getAngle();
		}
	}, anglePIDOutput);

	public static final double ANGLE_PID_ABSOLUTE_TOLERANCE = 5.0d;

	private static final double ANGLE_PID_PRODUCTION_P = 0.02d;
	private static final double ANGLE_PID_PRODUCTION_I = 0.0d;
	private static final double ANGLE_PID_PRODUCTION_D = -0.001d;

	private static final double ANGLE_PID_PRACTICE_P = 0.02d;
	private static final double ANGLE_PID_PRACTICE_I = 0.0d;
	private static final double ANGLE_PID_PRACTICE_D = -0.001d;

	// DriveAnglePID

	private MockSpeedController holdAnglePIDOutput  = new MockSpeedController();

	private PIDController holdAnglePID = new PIDController(0.04, 0.004, 0.0, 0.0,
			new PIDSource() {
		public double pidGet() {
			return gyro.getAngle();
		}
	}, holdAnglePIDOutput);

	private double holdAngle = -1.0d;
	private double lastAngleSetpoint = -1.0d;
	private Timer  holdAngleTimer = new Timer();

	private static final double HOLD_ANGLE_PID_PRODUCTION_P = 0.04d;
	private static final double HOLD_ANGLE_PID_PRODUCTION_I = 0.004d;
	private static final double HOLD_ANGLE_PID_PRODUCTION_D = 0.0d;

	private static final double HOLD_ANGLE_PID_PRACTICE_P = 0.04d;
	private static final double HOLD_ANGLE_PID_PRACTICE_I = 0.004d;
	private static final double HOLD_ANGLE_PID_PRACTICE_D = 0.0d;

	// RotationPID - angular velocity

	private MockSpeedController rotationPIDOutput    = new MockSpeedController();

	private PIDController rotationPID = new PIDController(0.025, 0, 0, RobotMap.MAX_ANUGLAR_VELOCITY / 190.0, 
			new PIDSource() {
		public double pidGet() {
			return getGyroRotation() / RobotMap.MAX_ANUGLAR_VELOCITY;
		}
	},	rotationPIDOutput);

	private static final double ROTATION_PID_PRODUCTION_P = 0.025d;
	private static final double ROTATION_PID_PRODUCTION_I = 0.0d;
	private static final double ROTATION_PID_PRODUCTION_D = 0.0d;

	private static final double ROTATION_PID_PRACTICE_P = 0.025d;
	private static final double ROTATION_PID_PRACTICE_I = 0.0d;
	private static final double ROTATION_PID_PRACTICE_D = 0.0d;

	// Distance PID

	private Timer distancePIDTimer = new Timer(5.0);
	private boolean        distanceOnTarget = false;
	private LinearRamp     distanceSlowDownRamp = new LinearRamp();
	private Timer      distancePIDOnTargetTimer = new Timer(0.2);
	
	private MockSpeedController distancePIDOutput = new MockSpeedController();

	private PIDController distancePID = new PIDController(0.007, 0.0, -0.0005, 0.0, 
			new PIDSource() {
		public double pidGet() {
			return getDistance(Units.ENCODER_COUNTS);
		}
	}, distancePIDOutput);

	private static final double DISTANCE_PID_PRODUCTION_P = 0.007d;
	private static final double DISTANCE_PID_PRODUCTION_I = 0.0d;
	private static final double DISTANCE_PID_PRODUCTION_D = -0.0005d;

	private static final double DISTANCE_PID_PRACTICE_P = 0.007d;
	private static final double DISTANCE_PID_PRACTICE_I = 0.0d;
	private static final double DISTANCE_PID_PRACTICE_D = -0.0005d;

	// Wheel Speed PID

	private MockSpeedController [] wheelSpeedPIDOutputArr = {
			new MockSpeedController(),
			new MockSpeedController(),
			new MockSpeedController(),
			new MockSpeedController()  };

	private PIDController [] wheelSpeedPIDArr = {
			new PIDController(0.4, 0.0, -0.0,	1.0, 
					encoderArr[FRONT_LEFT], wheelSpeedPIDOutputArr[FRONT_LEFT]),
			new PIDController(0.4, 0.0, -0.0,	1.0, 
					encoderArr[REAR_LEFT],  wheelSpeedPIDOutputArr[REAR_LEFT]),
			new PIDController(0.4, 0.0, -0.0,	1.0, 
					encoderArr[FRONT_RIGHT],wheelSpeedPIDOutputArr[FRONT_RIGHT]),
			new PIDController(0.4, 0.0, -0.0,	1.0, 
					encoderArr[REAR_RIGHT], wheelSpeedPIDOutputArr[REAR_RIGHT])	};

	private static final double WHEEL_SPEED_PID_PRODUCTION_P = 0.4;
	private static final double WHEEL_SPEED_PID_PRODUCTION_I = 0.0d;
	private static final double WHEEL_SPEED_PID_PRODUCTION_D = 0.0d;

	private static final double WHEEL_SPEED_PID_PRACTICE_P = 0.4d;
	private static final double WHEEL_SPEED_PID_PRACTICE_I = 0.0d;
	private static final double WHEEL_SPEED_PID_PRACTICE_D = 0.0d;

	/**
	 * Is the angle on target for the specified drive angle driveToAngle which enables the anglePID.
	 * @return true if on target, false otherwise
	 */
	public boolean angleOnTarget() {
		
		if (!anglePID.isEnable()) { return true; }
		
		// Get the current angle and look for an difference of less than 3 degrees
		double difference = anglePID.getSetpoint() - gyro.getAngle();
		if (difference > 180) { difference -= 360; }
		
		if (difference < -180) { difference += 360; }

		SmartDashboard.putNumber("Angle difference", difference);
		
		return Math.abs(difference) < 15;
	}

	public void disablePIDs() {

		disableAnglePID();
		disableRotationPID();
		disableDistancePID();
		disableWheelSpeedPIDs();
		disableHoldAnglePID();

	}

	@Override
	public void disableSubsystem() {

		subsystemEnabled = false;

		disablePIDs();

		// Stop all the motors
		stopMotors();
	}

	/**
	 * Is the distance on target for the specified drive distance after calling driveToDistance
	 * which enables the distancePID.
	 * @return true if on target, false otherwise
	 */
	public boolean distanceOnTarget() {
		if(distancePID.onTarget() || (getDistance(Units.ENCODER_COUNTS) > distancePID.getSetpoint())) {
			disableDistancePID();
			return true;
		}
		return false;
//		if (distancePID.onTarget()) {
//			distancePIDOnTargetTimer.start(0.2);
//			if (distancePIDOnTargetTimer.isExpired()) {
//				return true;
//			}
//		} else {
//			distancePIDOnTargetTimer.disable();
//		}
//		return false;
//		return distancePID.onTarget();// || distancePIDTimer.isExpired();
	}

	/** theta is the direction to drive in, targetAngle is the angle to rotate to. theta[rad], targetAngle[deg]
	 * @param p - PolarCoordinate (r, theta) used to determine speed and direction
	 * @param targetAngle - The angle to face while driving
	 * @param distanceInches - Distance to travel in inches.
	 * @param driveMode - FIELD_RELATIVE, or ROBOT_RELATIVE.
	 */
	public void driveDistance(PolarCoordinate p, double targetAngle, double distanceInches, DriveMode driveMode) {

		double pMin = 0.0015;
		double pMax = 0.0025;
		
		double iMin = 0.0002;
		double iMax = 0.0005;
		
		PolarCoordinate drivePolarCoordinate = getDrivePolarCoordinate(p, driveMode);
		
		double pDrive = pMin + (Math.abs(Math.sin(Math.toRadians(drivePolarCoordinate.getTheta()))) * (pMax - pMin));
		double iDrive = iMin + (Math.abs(Math.sin(Math.toRadians(drivePolarCoordinate.getTheta()))) * (iMax - iMin));
		
		SmartDashboard.putNumber("DISTANCE P VALUE", pDrive * 1000);
		SmartDashboard.putNumber("DISTANCE I VALUE", iDrive * 1000);
		if(distanceInches - getDistance(Units.INCHES) > 4.0) {
			distancePID.setPID(pDrive, 0.0, 0.0);
		} else {
			distancePID.setPID(pDrive, iDrive, 0.0);
		}
		
//		// If the drive distance is past the target, then stop the robot.
//		// A new PolarCoordinate is set to stopped by default.
//		
//		if (getDistance(Units.INCHES) > distanceInches) { 
//			driveToAngle(new PolarCoordinate(), targetAngle, driveMode, PIDEnable.ENABLED, PIDEnable.ENABLED);
//			distanceOnTarget = true;
//			distanceSlowDownRamp.disable();
//			return;
//		}
//		
//		// Drive at the requested power until the distance is close, and then 
//		// turn on the deceleration.
//		// When decelerating linearly, we know the distance travelled will be 
//		// d = 1/2 a t^2
//		// The initial velocity in encoder counts is given by 
//		// v0 = getSpeed();
//		//
//		// The time to decelerate (given a constant speed) is given by
//		// tdec = (setpointVelocity / RobotMap.MAX_DRIVE_ACCELERATION) 
//		//
//		// The actual acceleration in encoder counts / sec^2
//		// a = V0 / tdec
//		//
//		// Given the acceleration, we need to start slowing down at some distance before we are stopped.
//		//
//		// The distance it will take to stop is
//		// d = 1/2 a t^2
//		// d = 1/2 (V0/tdec) tdec^2
//		// d = 1/2 V0 tdec
//		
//	
//		// If the robot has not yet got to the deceleration point, then continue driving.
//		if (!distanceSlowDownRamp.isEnable()) {
//		
//			// Deceleration time
//			double decelerationTime = p.getR() / RobotMap.MAX_DRIVE_ACCELERATION;
//
//			// d = 1/2 V0 tdec
//			double stoppingDistance = getSpeed() * decelerationTime / 2.0d; // in encoderCounts
//
//			if (getDistance(Units.ENCODER_COUNTS) + stoppingDistance < distanceInches*RobotMap.ENCODER_COUNTS_PER_INCH) {
//	
//				driveToAngle(p, targetAngle, driveMode, PIDEnable.ENABLED, PIDEnable.ENABLED);
//				
//				distanceOnTarget = false;
//	
//				return;
//			}
//			
//			// Initiate the distance slow down based on the current velocity and deceleration time
//			distanceSlowDownRamp.start(decelerationTime, p.getR(), 0.0);
//		}
//		
//		// The drive distance command determines the speed ramp down based on the current distance from the 
//		// target and the speed of the robot.
//
//		double r = distanceSlowDownRamp.getValue();
//
//		SmartDashboard.putNumber("R", r);
//
//		if (distanceSlowDownRamp.isComplete()) {
//			distanceOnTarget = true;
//		} else {
//			distanceOnTarget = false;
//		}
		
		if(!distancePID.isEnable()) {
			distancePID.enable();
		}
		
		distancePID.setSetpoint(distanceInches * RobotMap.ENCODER_COUNTS_PER_INCH);
		
		distancePID.setOutputRange(-p.getR(), p.getR());
		
		double r = distancePIDOutput.get();
		
		driveToAngle(new PolarCoordinate(r, p.getTheta()), targetAngle, driveMode, PIDEnable.ENABLED, PIDEnable.ENABLED);
	}

	/**
	 * Set the motors to drive in the direction and magnitude of the PolarCoordinate and 
	 * with the specified rotation.
	 * 
	 * @param p - PolarCoordinate (r, theta) used to determine speed and direction
	 * @param rotation - the requested rotational speed
	 * @param driveMode - FIELD_RELATIVE, or ROBOT_RELATIVE.
	 * @param rotationPIDEnable - ENABLE to use rotation PIDS for driving the motor speeds
	 * @param motorPIDEnable - ENABLE to use motor PIDS for driving the motor speeds
	 */
	public void driveJoystick(PolarCoordinate p, double rotation, DriveMode driveMode, 
			PIDEnable rotationPIDEnable, PIDEnable motorPIDEnable) {

		// Disable unused PIDs
		disableAnglePID();
		disableDistancePID();

		// Calculate the direction of travel relative to the robot heading.
		PolarCoordinate drivePolarCoordinate = getDrivePolarCoordinate(p, driveMode);

		if (rotation != 0.0d) { 
			lastAngleSetpoint = -1.0;
		}
		
		drivePolar(drivePolarCoordinate, rotation, -1, rotationPIDEnable, motorPIDEnable);

	}

	/**
	 * Drive while rotating to the target angle.
	 * <p>
	 * The robot direction and speed drive are still active.
	 * 
	 * @param p - PolarCoordinate (r, theta) used to determine speed and direction
	 * @param targetAngle
	 * @param motorPIDEnable
	 */
	public void driveToAngle(PolarCoordinate p, double angleSetpoint, DriveMode driveMode,
			PIDEnable rotationPIDEnable, PIDEnable motorPIDEnable) {

		double angleRelativeSetpoint = angleSetpoint;
		
		if (driveMode == DriveMode.ROBOT_RELATIVE) {
			angleRelativeSetpoint = gyro.getAngle() + angleSetpoint;
			if (angleRelativeSetpoint > 360.0) {
				angleRelativeSetpoint -= 360.0d;
			}
		}

		enableAnglePID();

		SmartDashboard.putNumber("Angle difference", angleRelativeSetpoint - gyro.getAngle());

		// Use the output of the gyro angle PID to set the rotational velocity of the robot.
		anglePID.setSetpoint(angleRelativeSetpoint);

		PolarCoordinate drivePolarCoordinate = getDrivePolarCoordinate(p, driveMode);

		SmartDashboard.putNumber("drivePolarAngle", drivePolarCoordinate.getTheta());

		lastAngleSetpoint = angleRelativeSetpoint;
		
		// Drive the robot using the input p and use the anglePID to set the rotation.
		drivePolar(drivePolarCoordinate, anglePIDOutput.get(), angleRelativeSetpoint, 
				rotationPIDEnable, motorPIDEnable);

	}

	@Override
	public void enableSubsystem() {

		subsystemEnabled = true;

	}

	/**
	 *  Get the Gyro angle
	 *  
	 *  @return double - Gyro angle.
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	/**
	 *  Get the Gyro rotational speed
	 *  
	 *  @return double - Gyro rotational speed.
	 */
	public double getGyroRotation() {
		return gyro.getRate();
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDriveCommand());
	}

	public void initSubsystem() {

		// Initialize Sensors
		//gyro.setSensitivity(0.0125);
		gyro.setSensitivity(0.00165 * (745.0 / 720.0));
//		gyro.initGyro();

		// Initialize PID parameters
		// Angle tolerance to determine if the PID is on target in degrees.
		// Set the PID Constants based on the robot that is executing.  The default is the 
		// test robot
		anglePID.setAbsoluteTolerance(2.7d);
		anglePID.setInputRange(0.0d, 360.0d);
		anglePID.setContinuous(true);
		anglePID.setOutputRange(-1.0d, 1.0d);
		switch (RobotMap.currentRobot) {
		case RobotMap.ROBOT_PRODUCTION:anglePID.setPID(ANGLE_PID_PRODUCTION_P, ANGLE_PID_PRODUCTION_I, ANGLE_PID_PRODUCTION_D); break;
		case RobotMap.ROBOT_PRACTICE:  anglePID.setPID(ANGLE_PID_PRACTICE_P,   ANGLE_PID_PRACTICE_I,   ANGLE_PID_PRACTICE_D);   break;
		default: break;	}

		// Angle tolerance to determine if the PID is on target in degrees.
		holdAnglePID.setInputRange(0.0d, 360.0d);
		holdAnglePID.setContinuous(true);
		holdAnglePID.setOutputRange(-1.0d, 1.0d);
		switch (RobotMap.currentRobot) {
		case RobotMap.ROBOT_PRODUCTION:holdAnglePID.setPID(HOLD_ANGLE_PID_PRODUCTION_P, HOLD_ANGLE_PID_PRODUCTION_I, HOLD_ANGLE_PID_PRODUCTION_D); break;
		case RobotMap.ROBOT_PRACTICE:  holdAnglePID.setPID(HOLD_ANGLE_PID_PRACTICE_P,   HOLD_ANGLE_PID_PRACTICE_I,   HOLD_ANGLE_PID_PRACTICE_D);   break;
		default: break;	}

		// Rotation PID
		rotationPID.setInputRange(-1.0, 1.0);
		rotationPID.setOutputRange(-1.0d, 1.0d);
		switch (RobotMap.currentRobot) {
		case RobotMap.ROBOT_PRODUCTION:rotationPID.setPID(ROTATION_PID_PRODUCTION_P, ROTATION_PID_PRODUCTION_I, ROTATION_PID_PRODUCTION_D); break;
		case RobotMap.ROBOT_PRACTICE:  rotationPID.setPID(ROTATION_PID_PRACTICE_P,   ROTATION_PID_PRACTICE_I,   ROTATION_PID_PRACTICE_D);   break;
		default: break;	}

		// Distance tolerance to determine if the PID is on target in encoder counts.
		distancePID.setAbsoluteTolerance(90);
		distancePID.setOutputRange(-1.0, 1.0);
		switch (RobotMap.currentRobot) {
		case RobotMap.ROBOT_PRODUCTION:distancePID.setPID(DISTANCE_PID_PRODUCTION_P, DISTANCE_PID_PRODUCTION_I, DISTANCE_PID_PRODUCTION_D); break;
		case RobotMap.ROBOT_PRACTICE:  distancePID.setPID(DISTANCE_PID_PRACTICE_P,   DISTANCE_PID_PRACTICE_I,   DISTANCE_PID_PRACTICE_D);   break;
		default: break;	}

		// WheelSpeedPID
		for (int i=0; i<MOTOR_COUNT; i++) {
			wheelSpeedPIDArr[i].setInputRange(-1.0d, 1.0d);
			wheelSpeedPIDArr[i].setOutputRange(-1.0d, 1.0d);
			switch (RobotMap.currentRobot) {
			case RobotMap.ROBOT_PRODUCTION:wheelSpeedPIDArr[i].setPID(WHEEL_SPEED_PID_PRODUCTION_P, WHEEL_SPEED_PID_PRODUCTION_I, WHEEL_SPEED_PID_PRODUCTION_D, 1.0d); break;
			case RobotMap.ROBOT_PRACTICE:  wheelSpeedPIDArr[i].setPID(WHEEL_SPEED_PID_PRACTICE_P,   WHEEL_SPEED_PID_PRACTICE_I,   WHEEL_SPEED_PID_PRACTICE_D,   1.0d); break;
			default: break;	}
		}

		switch (RobotMap.currentRobot) {
		case RobotMap.ROBOT_PRODUCTION:
		case RobotMap.ROBOT_PRACTICE:
			motorInversionArr = new boolean [] {
					MOTOR_NOT_INVERTED,
					MOTOR_NOT_INVERTED,
					MOTOR_INVERTED,
					MOTOR_INVERTED };
			
			break;
		default: break;
		}
		mecanumDrive.setMotorInversion(motorInversionArr);

		for (int i=0; i<MOTOR_COUNT; i++) {
			talonArr[i].stopMotor();
		}

		// Initialize SmartDashboard objects

		// SmartDashboard.putData("Accel", accel);

		SmartDashboard.putData("Gyro", gyro);

		SmartDashboard.putData("FrontLeftTalon", talonArr[FRONT_LEFT]);
		SmartDashboard.putData("RearLeftTalon",  talonArr[REAR_LEFT]);
		SmartDashboard.putData("FrontRightTalon",talonArr[FRONT_RIGHT]);
		SmartDashboard.putData("RearRightTalon", talonArr[REAR_RIGHT]);

		SmartDashboard.putData("FrontLeftEncoder", encoderArr[FRONT_LEFT]);
		SmartDashboard.putData("RearLeftEncoder",  encoderArr[REAR_LEFT]);
		SmartDashboard.putData("FrontRightEncoder",encoderArr[FRONT_RIGHT]);
		SmartDashboard.putData("RearRightEncoder", encoderArr[REAR_RIGHT]);

		SmartDashboard.putData("FrontLeftPID", wheelSpeedPIDArr[FRONT_LEFT]);
		SmartDashboard.putData("RearLeftPID",  wheelSpeedPIDArr[REAR_LEFT]);
		SmartDashboard.putData("FrontRightPID",wheelSpeedPIDArr[FRONT_RIGHT]);
		SmartDashboard.putData("RearRightPID", wheelSpeedPIDArr[REAR_RIGHT]);

		SmartDashboard.putData("DistancePID", distancePID);
		SmartDashboard.putData("GyroAnglePID", anglePID);
		SmartDashboard.putData("HoldAnglePID", holdAnglePID);
		SmartDashboard.putData("GyroRotationPID", rotationPID);

		disableSubsystem();

	}

	public void print() {
		/*
		 * if (System.currentTimeMillis() - lastPrintTime > 1000) {
		 * System.out.println("Gyro Rate:" + gyro.getRate());
		 * 
		 * System.out.println("PID: " + (frontLeftPID.isEnable() &&
		 * rearLeftPID.isEnable() && frontRightPID.isEnable() &&
		 * rearRightPID.isEnable()));
		 * 
		 * System.out.println("Left Encoders:"); System.out.println("Front: " +
		 * frontLeftEncoder.getDistance() + " Rear: " +
		 * rearLeftEncoderCorrected.getDistance() *
		 * RobotMap.ENCODER_RESOLUTION_CORRECTION);
		 * System.out.println("RightEncoders:"); System.out.println("Front: " +
		 * frontRightEncoder.getDistance() + " Rear: " +
		 * rearRightEncoder.getDistance());
		 * 
		 * lastPrintTime = System.currentTimeMillis();
		 * 
		 * }
		 */
	}

	public void resetEncoders() {

		for (Encoder encoder: encoderArr) {
			encoder.reset();
		}

		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {
			wheelSpeedPID.setSetpoint(0.0);
		}

	}

	/**
	 * Reset the Gyro to the supplied angle.  
	 * <p>
	 * This will allow the driver to reset the gyro to a given angle relative to the field to 
	 * compensate for gyro drift.  The robot should not be moving when the gyro is reset. 
	 *
	 * @param fieldAngle
	 */
	public void resetGyro(int fieldAngle) {

		disableHoldAnglePID();
		gyro.setOffset(fieldAngle);
		lastAngleSetpoint = fieldAngle;

	}

	@Override
	public void updateDashboard() {
		// accel.updateTable();

		gyro.updateTable();

		for (Encoder encoder: encoderArr) {
			encoder.updateTable();
		}

		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {
			wheelSpeedPID.updateTable();
		}

		SmartDashboard.putNumber("Front Left Output", talonArr[FRONT_LEFT].get());
		SmartDashboard.putNumber("Front Right Output", talonArr[FRONT_RIGHT].get());
		SmartDashboard.putNumber("Rear Left Output", talonArr[REAR_LEFT].get());
		SmartDashboard.putNumber("Rear Right Output", talonArr[REAR_RIGHT].get());

		distancePID.updateTable();
		SmartDashboard.putNumber("Distance PID Output", distancePIDOutput.get());
		SmartDashboard.putNumber("Encoder Distance", getDistance(Units.ENCODER_COUNTS));
		SmartDashboard.putNumber("Encoder Distance(in)", getDistance(Units.INCHES));
		SmartDashboard.putNumber("Encoder Speed", getSpeed());
		SmartDashboard.putBoolean("Distance Slow Down Ramp Enabled", distanceSlowDownRamp.isEnable());
		SmartDashboard.putNumber("Distance Slow Down Ramp Output", distanceSlowDownRamp.getValue());

		anglePID.updateTable();
		SmartDashboard.putNumber("Angle PID Output", anglePIDOutput.get());

		holdAnglePID.updateTable();
		SmartDashboard.putNumber("Hold Angle PID Output", holdAnglePIDOutput.get());

		rotationPID.updateTable();
		SmartDashboard.putNumber("Rotation PID Output", rotationPIDOutput.get());

		SmartDashboard.putNumber("Gyro angle",     gyro.getAngle());
		SmartDashboard.putNumber("Gyro rate",      getGyroRotation());

		SmartDashboard.putBoolean("Chassis Subsystem Enabled" , subsystemEnabled);

		// SmartDashboard.putNumber("X pos", xpos);
	}

	/**
	 * Disable the AnglePID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableAnglePID() {
		anglePID.disable();
	}

	/**
	 * Disable the DistancePID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableDistancePID() {
		distancePID.disable();		
		distancePIDTimer.disable();
		
		distanceSlowDownRamp.disable();
		distanceOnTarget = false;
	}


	/**
	 * Disable the HoldAnglePID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableHoldAnglePID() {

		holdAnglePID.disable();
		holdAngle = -1.0d;
		holdAngleTimer.disable();
	}

	/**
	 * Disable the RotationID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableRotationPID() {
		rotationPID.disable();
	}

	/**
	 * Disable the wheelSpeedPIDs
	 * 
	 * If they are already disabled, this routine does nothing.
	 */
	private void disableWheelSpeedPIDs() {

		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {
			wheelSpeedPID.disable();
		}

	}

	/**
	 * Set the motors to drive in the direction and magnitude of the PolarCoordinate and 
	 * with the specified rotation.
	 * 
	 * @param p - PolarCoordinate (r, theta) used to determine speed and direction
	 * @param rotation - speed of rotation of the robot
	 * @param motorPIDEnable - ENABLE to use motor PIDS for driving the motor speeds
	 */
	private void drivePolar(PolarCoordinate p, double rotation, double angle,
			PIDEnable rotationPIDEnable, PIDEnable motorPIDEnable) {
		
		// Determine if the angle should be held constant during this move sequence.
		// if there is no rotational input then try to hold the rotation constant.
		// Override the rotation PID to disabled.
		double angleRotation = rotation;
		if (Math.abs(rotation) < .02d && !anglePID.isEnable()) {

			// Wait 2 seconds after there is zero rotation input before enabling the
			// driveHoldAnglePID.
			holdAngleTimer.start(2.0d);

			// Enable the driveHoldPID after 2 seconds
			if (holdAngleTimer.isExpired()) {
				disableRotationPID();
				rotationPIDEnable = PIDEnable.DISABLED;
				if (holdAngle < 0) { 
					// The angle should be set to the passed in angle unless that 
					// angle is < 0, in which case use the last setpoint if the 
					// user has not pressed the rotation stick, else use
					// the current direction the robot is pointing.
					if (angle >= 0) {
						holdAngle = angle;
					} else {
						if (lastAngleSetpoint >= 0) {
							holdAngle = lastAngleSetpoint;
						} else {
							holdAngle = gyro.getAngle();
						}
					}
					holdAnglePID.reset();
					holdAnglePID.enable();
					holdAnglePID.setSetpoint(holdAngle);
				}
				angleRotation = holdAnglePIDOutput.get();
				
			} else {
				// If the hold angle timer has not expired, then the 
				// hold angle pid should not be running.
				disableHoldAnglePID();
			}
			
		} else {
			disableHoldAnglePID();
			holdAngleTimer.disable();
		}

		// Use a rotation PID if required.
		double mecanumRotation = angleRotation;

		// If the rotationPID is enabled, then put the rotation through the PID.
		if (rotationPIDEnable == PIDEnable.ENABLED) {
			enableRotationPID();
			rotationPID.setSetpoint(angleRotation);
			mecanumRotation = rotationPIDOutput.get();
		} else {
			disableRotationPID();
		}

		/* 
		 * The mecannum drive uses the angle theta of the polar coordinate p(r,theta) 
		 * to set the relative speeds of all of the drive wheels.
		 */
		double [] motorSpeeds = mecanumDrive.drivePolar(p, mecanumRotation);

		// If the motor PIDs are enabled, then use the motor speeds as the setpoint values
		// for the motor PIDs.
		if (motorPIDEnable == PIDEnable.ENABLED) {
			enableWheelSpeedPIDs();

			for (int i=0; i<MOTOR_COUNT; i++) {
				wheelSpeedPIDArr[i].setSetpoint(motorSpeeds[i]);
			}
		} else {
			disableWheelSpeedPIDs();
		}

		// Set the motor speeds.
		if (motorPIDEnable == PIDEnable.ENABLED) {
			for (int i=0; i<MOTOR_COUNT; i++) {
				talonArr[i].set(wheelSpeedPIDOutputArr[i].get());
			}
		} else {
			for (int i=0; i<MOTOR_COUNT; i++) {
				talonArr[i].set(motorSpeeds[i]);
			}
		}
	}

	/**
	 * Enable and reset the AnglePID
	 * 
	 * If already enabled, this routine does nothing.
	 */
	public void enableAnglePID() {

		if (!anglePID.isEnable()) {
			anglePID.reset();
			anglePID.enable();
		}

	}

	/**
	 * Enable and reset the DistancePID
	 * 
	 * If already enabled, this routine does nothing.
	 */
	private void enableDistancePID() {

		if (!distancePID.isEnable()) {

			distancePID.reset();
			distancePID.enable();

			resetEncoders();
		}
	}

	/**
	 * Enable and reset the RotationPID
	 * 
	 * This pid controls the angular velocity of the robot.  The integral portion
	 * of this control helps the robot to move in a straight line by driving the 
	 * angle error (integral of rotation) to zero.
	 * 
	 * If already enabled, this routine does nothing.
	 */
	private void enableRotationPID() {

		if (!rotationPID.isEnable()) {
			rotationPID.reset();
			rotationPID.enable();
		}
	}

	/**
	 * Enable and reset the wheel speed PIDs.
	 * 
	 * If they are already enabled, this routine does nothing.
	 */
	private void enableWheelSpeedPIDs() {

		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {

			if (!wheelSpeedPID.isEnable()) {
				wheelSpeedPID.reset();
				wheelSpeedPID.enable();

			}
		}

	}

	/**
	 * Get the distance travelled since the last encoder reset.  The distance traveled is roughly 
	 * the average distance of all the encoders less the rotational amount.
	 * @param units - the units of measure to return the distance, encoder counts or inches.
	 * @return double - the encoder distance since the last reset.
	 */
	// amount used for rotation of the robot.
	private double getDistance(Units units) {

		// In the mecanum drive, the front right and rear left encoders should be moving
		// in the same direction and at the same speed in order to make the robot move in a 
		// straight line.  
		// Any difference between the encoders indicates a rotational component that 
		// does not contribute to the distance traveled.
		
		double distanceDiagonal1 = 
				( encoderArr[FRONT_LEFT].getDistance() * (motorInversionArr[FRONT_LEFT] ? 1.0d : -1.0d)
				+ encoderArr[REAR_RIGHT].getDistance() * (motorInversionArr[REAR_RIGHT] ? 1.0d : -1.0d)) / 2.0d;
		double distanceDiagonal2 = 
				( encoderArr[REAR_LEFT]  .getDistance() * (motorInversionArr[REAR_LEFT] ? 1.0d : -1.0d)
				+ encoderArr[FRONT_RIGHT].getDistance() * (motorInversionArr[FRONT_RIGHT] ? 1.0d : -1.0d)) / 2.0d;

		double distanceEncoderCounts = 
				(Math.abs(distanceDiagonal1) + Math.abs(distanceDiagonal2)) / 2;

		switch (units) {
		case ENCODER_COUNTS: return distanceEncoderCounts;
		case INCHES:         return distanceEncoderCounts/RobotMap.ENCODER_COUNTS_PER_INCH;
		default:             return distanceEncoderCounts;
		}
	}

	private PolarCoordinate getDrivePolarCoordinate(PolarCoordinate p, DriveMode driveMode) {

		PolarCoordinate drivePolarCoordinate = new PolarCoordinate();
		drivePolarCoordinate.set(p);

		if (driveMode == DriveMode.FIELD_RELATIVE) {
			drivePolarCoordinate.setTheta(p.getTheta() - gyro.getAngle());
		}

		return drivePolarCoordinate;
	}

	/**
	 * The current speed is the average speed of all the encoders
	 * <p> 
	 * This number is the absolute value of all the encoder speeds and cannot be negative.
	 * @return double - average encoder speed for the robot wheels.
	 */
	private double getSpeed() {
		double speedTotal = 0;
		for (Encoder encoder: encoderArr) {
			speedTotal += Math.abs(encoder.getRate());
		}
		return speedTotal / encoderArr.length;
	}

	private void stopMotors() {
		for (Talon talon: talonArr) {
			talon.stopMotor();
		}
	}

}
