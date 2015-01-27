package robot.subsystems;

import robot.MockSpeedController;
import robot.PolarCoordinate;
import robot.RobotMap;
import robot.RunnymedeMecanumDrive;
import robot.commands.TeleopDriveCommand;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
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
	
	private boolean subsystemEnabled = false;

	private static double MAX_ROTATION_SPEED = 0.25;
	
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
	
	Talon [] talonArr = {
			new Talon(RobotMap.FRONT_LEFT_MOTOR), 
	        new Talon(RobotMap.REAR_LEFT_MOTOR),
	        new Talon(RobotMap.FRONT_RIGHT_MOTOR),
	        new Talon(RobotMap.REAR_RIGHT_MOTOR)   };
	
	// Motor Inverted indicator
	RunnymedeMecanumDrive mecanumDrive = 
			new RunnymedeMecanumDrive(MOTOR_NOT_INVERTED,
					                  MOTOR_INVERTED,
					                  MOTOR_NOT_INVERTED,
					                  MOTOR_INVERTED);
	
	// SENSORS
	
	// Encoders
	
	Encoder [] encoderArr = {
			new Encoder(RobotMap.FRONT_LEFT_ENCODER_ONE, RobotMap.FRONT_LEFT_ENCODER_TWO, true),
			new Encoder(RobotMap.REAR_LEFT_ENCODER_ONE,  RobotMap.REAR_LEFT_ENCODER_TWO,  true),
			new Encoder(RobotMap.FRONT_RIGHT_ENCODER_ONE,RobotMap.FRONT_RIGHT_ENCODER_TWO,true),
			new Encoder(RobotMap.REAR_RIGHT_ENCODER_ONE, RobotMap.REAR_RIGHT_ENCODER_TWO, true)   };

	// Gyro
	
	Gyro gyro = new Gyro(RobotMap.GYRO_PORT);
	private double gyroCalibrationOffset = 0.0;

	// PIDS and PID outputs

	// AnglePID
	
	MockSpeedController anglePIDOutput    = new MockSpeedController();

	PIDController anglePID = new PIDController(0.0125, 0.0, -0.00075, 0.0,
			new PIDSource() {
				public double pidGet() {
					return getGyroAngle();
				}
			}, anglePIDOutput);
	
	double angleRelativeSetpoint = 0.0;
	double angleSetpointPrev     = 0.0;

	// RotationPID - angular velocity
	
	MockSpeedController rotationPIDOutput    = new MockSpeedController();

	PIDController rotationPID = new PIDController(0.0005, 0.0015, 0.001, 0.0, 
			new PIDSource() {
				public double pidGet() {
					return getGyroRotation();
				}
			},	rotationPIDOutput);
	

	// Distance PID
	
	MockSpeedController distancePIDOutput = new MockSpeedController();

	PIDController distancePID = new PIDController(0.007, 0.0, -0.0005, 0.0, 
			new PIDSource() {
				public double pidGet() {
					return encoderAverageDistance();
				}
			}, distancePIDOutput);

	// Wheel Speed PID

	MockSpeedController [] wheelSpeedPIDOutputArr = {
			new MockSpeedController(),
			new MockSpeedController(),
			new MockSpeedController(),
			new MockSpeedController()  };

	// FIXME: Don't understand these PID parameters.
	PIDController [] wheelSpeedPIDArr = {
			new PIDController(0.001, 0.0, -0.0005,	1.0 / RobotMap.MAX_ENCODER_RATE, 
					encoderArr[FRONT_LEFT], wheelSpeedPIDOutputArr[FRONT_LEFT]),
			new PIDController(0.001, 0.0, -0.0005,	1.0 / RobotMap.MAX_ENCODER_RATE, 
					encoderArr[REAR_LEFT],  wheelSpeedPIDOutputArr[REAR_LEFT]),
			new PIDController(0.001, 0.0, -0.0005,	1.0 / RobotMap.MAX_ENCODER_RATE, 
					encoderArr[FRONT_RIGHT],wheelSpeedPIDOutputArr[FRONT_RIGHT]),
			new PIDController(0.001, 0.0, -0.0005,	1.0 / RobotMap.MAX_ENCODER_RATE, 
					encoderArr[REAR_RIGHT], wheelSpeedPIDOutputArr[REAR_RIGHT])	};

	
	/**
	 * Is the angle on target for the specified drive angle driveToAngle which enables the anglePID.
	 * @return true if on target, false otherwise
	 */
	public boolean angleOnTarget() {
		return anglePID.onTarget();
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
		return distancePID.onTarget();
	}

	/**
	 * Set the motors to drive in the direction and magnitude of the PolarCoordinate and 
	 * with the specified rotation.
	 * 
	 * @param p - PolarCoordinate (r, theta) used to determine speed and direction
	 * @param rotation - the requested rotational speed
	 * @param driveMode - FIELD_RELATIVE, or ROBOT_RELATIVE.
	 * @param motorPIDEnable - ENABLE to use motor PIDS for driving the motor speeds
	 */
	public void driveJoystick(PolarCoordinate p, double rotation, DriveMode driveMode, 
			PIDEnable rotationPIDEnable, PIDEnable motorPIDEnable) {

		// Disable unused PIDs
		disableAnglePID();
		disableDistancePID();
		
		// Scale the rotational speed from [0..1.0] to the max rotational speed.
		double scaledRotation = rotation * MAX_ROTATION_SPEED;

		// Calculate the angle of travel relative to the robot heading.
		PolarCoordinate drivePolarCoordinate = getDrivePolarCoordinate(p, driveMode);

		drivePolar(drivePolarCoordinate, scaledRotation, rotationPIDEnable, motorPIDEnable);

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

		// Disable unused PIDs
		disableDistancePID();
		
		// Update the angle setpoint based on the target angle specified.  If the target angle is 
		// not specified (-1) then do not update the setpoint angle.
		
		if (angleSetpoint >= 0) {

			// In FIELD_RELATIVE mode, this command will try to line the robot up with the angle
			// specified.
			// In ROBOT_RELATIVE mode, this command will rotate the robot to face in the new
			// direction indicated relative to the current direction.  
			if (driveMode == DriveMode.ROBOT_RELATIVE) {
				
				// If the PID is not yet enabled, or the angle changes, then set the target
				// based on the current direction in ROBOT_RELATIVE.
				if (!anglePID.isEnable() || angleSetpoint != angleSetpointPrev) {
				
					angleRelativeSetpoint = getGyroAngle() + angleSetpoint;
					while (angleRelativeSetpoint > 360.0) {
						angleRelativeSetpoint -= 360.0d;
					}
				}
				
			} else {
				
				// Drive mode is field relative.
				angleRelativeSetpoint = angleSetpoint;
			}
			
			// Keep track of the input setpoint to track when it changes which is important for 
			// ROBOT_RELATIVE mode.
			angleSetpointPrev = angleSetpoint;
		}

		enableAnglePID();
		
		SmartDashboard.putNumber("Angle difference", angleRelativeSetpoint - getGyroAngle());
		
		// Use the output of the gyro angle PID to set the rotational velocity of the robot.
		anglePID.setSetpoint(angleRelativeSetpoint);
		
		// Drive the robot using the input p and use the anglePID to set the rotation.
		drivePolar(p, anglePIDOutput.get(), rotationPIDEnable, motorPIDEnable);
		
	}
	
	@Override
	public void enableSubsystem() {

		subsystemEnabled = true;
		
	}
	
	/**
	 *  Get the calibrated Gyro angle
	 *  
	 *  @return double - Gyro angle between 0 and 360 where 0 represents directly away
	 *  from the driver station.
	 */
	public double getGyroAngle() {
		
		double angle = gyro.getAngle() + gyroCalibrationOffset;
		
		while (angle < 0)    { angle += 360; }
		while (angle >= 360) { angle -= 360; }
		
		return angle;
	}

	/**
	 *  Get the Gyro rotational speed
	 *  
	 *  @return double - Gyro rotational speed.
	 */
	public double getGyroRotation() {
		return gyro.getRate();
	}


	/**
	 * Gyro Angle on target
	 * 
	 * @return - true if the gyro angle is at the gyro setpoint after calling
	 * driveToAngle.
	 */
	public boolean gyroAngleOnTarget() {
		return anglePID.onTarget();
	}


	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDriveCommand());
	}
	
	public void initSubsystem() {
		
		// Initialize Sensors
		gyro.setSensitivity(0.0125);
		gyro.initGyro();

		// Initialize PID parameters
		// Angle tolerance to determine if the PID is on target in degrees.
		anglePID.setAbsoluteTolerance(2.75d);
		anglePID.setInputRange(0.0d, 360.0d);
		anglePID.setContinuous(false);
		anglePID.setOutputRange(-1.0d, 1.0d);

		// Rotation PID
		rotationPID.setInputRange(-RobotMap.MAX_ANUGLAR_VELOCITY,
			 	RobotMap.MAX_ANUGLAR_VELOCITY);
		rotationPID.setOutputRange(-1.0d, 1.0d);

		// Distance tolerance to determine if the PID is on target in encoder counts.
		distancePID.setAbsoluteTolerance(8d);
		distancePID.setOutputRange(-1.0, 1.0);

		// WheelSpeedPID
		// FIXME:  Look at theory behind wheel speed PID.
		for (int i=0; i<MOTOR_COUNT; i++) {
			wheelSpeedPIDArr[i].setInputRange(-RobotMap.MAX_ENCODER_RATE,
								     		   RobotMap.MAX_ENCODER_RATE);
			wheelSpeedPIDArr[i].setOutputRange(-1.0d, 1.0d);
		}

		// Initialize SmartDashboard objects

		// SmartDashboard.putData("Accel", accel);

		SmartDashboard.putData("Gyro", gyro);

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
		SmartDashboard.putData("GyroRotationPID", rotationPID);

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

	/**
	 * Reset the Gyro to the supplied angle.  
	 * <p>
	 * This will allow the driver to reset the gyro to a given angle relative to the field to 
	 * compensate for gyro drift.  The robot should not be moving when the gyro is reset. 
	 *
	 * @param fieldAngle
	 */
	public void resetGyro(int fieldAngle) {

		gyro.reset();
		anglePID.reset();
		
		gyroCalibrationOffset = fieldAngle;
		
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
		
		anglePID.updateTable();
		SmartDashboard.putNumber("Angle PID Output", anglePIDOutput.get());

		rotationPID.updateTable();
		SmartDashboard.putNumber("Rotation PID Output", rotationPIDOutput.get());

		SmartDashboard.putNumber("Gyro angle",      getGyroAngle());
		SmartDashboard.putNumber("EncoderAverager", encoderAverageDistance());

		SmartDashboard.putBoolean("Chassis Subsystem Enabled" , subsystemEnabled);

		// SmartDashboard.putNumber("X pos", xpos);
	}
	
	/**
	 * Disable the AnglePID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableAnglePID() {

		if (anglePID.isEnable()) {
			anglePID.disable();
		}
		
	}
	
	/**
	 * Disable the DistancePID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableDistancePID() {

		if (distancePID.isEnable()) {
			distancePID.disable();
		}
	}

	private void disablePIDs() {

		disableAnglePID();
		disableRotationPID();
		disableDistancePID();
		disableWheelSpeedPIDs();
		
	}
	
	
	/**
	 * Disable the RotationID
	 * 
	 * If already disabled, this routine does nothing.
	 */
	private void disableRotationPID() {

		if (rotationPID.isEnable()) {
			rotationPID.disable();
		}
	}

	/**
	 * Disable the wheelSpeedPIDs
	 * 
	 * If they are already disabled, this routine does nothing.
	 */
	private void disableWheelSpeedPIDs() {

		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {

			if (wheelSpeedPID.isEnable()) {
				wheelSpeedPID.disable();
			}
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
	private void drivePolar(PolarCoordinate p, double rotation, 
			PIDEnable rotationPIDEnable, PIDEnable motorPIDEnable) {

		// Use a rotation PID if required.
		double mecanumRotation = rotation;
		
		// If the rotationPID is enabled, then put the rotation through the PID.
		if (rotationPIDEnable == PIDEnable.ENABLED) {
			enableRotationPID();
			rotationPID.setSetpoint(rotation);
			mecanumRotation = rotationPIDOutput.get();
		} else {
			disableRotationPID();
		}
		// Limit the rotation speed.
		if (mecanumRotation > MAX_ROTATION_SPEED) {
			mecanumRotation =  MAX_ROTATION_SPEED;
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
	private void enableAnglePID() {

		if (!anglePID.isEnable()) {
			anglePID.reset();
			anglePID.enable();
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
	
	private double encoderAverageDistance() { return 0; }

	private PolarCoordinate getDrivePolarCoordinate(PolarCoordinate p, DriveMode driveMode) {

		PolarCoordinate drivePolarCoordinate = p;
		
		if (driveMode == DriveMode.FIELD_RELATIVE) {
			drivePolarCoordinate = new PolarCoordinate();
			drivePolarCoordinate.setR(p.getR());
			drivePolarCoordinate.setTheta(getGyroAngle() - p.getTheta());
		}
		
		return drivePolarCoordinate;
	}

	private void resetEncoders() {

		for (Encoder encoder: encoderArr) {
			encoder.reset();
		}
		
		for (PIDController wheelSpeedPID: wheelSpeedPIDArr) {
			wheelSpeedPID.setSetpoint(0.0);
		}

	}
	
	private void stopMotors() {
		for (Talon talon: talonArr) {
			talon.stopMotor();
		}
	}
	
}