/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package robot;


/**
 * Runnymede mecannum drive calculates the wheel speeds required for a mecannum drive.
 * <p>
 * The methods all return an array of doubles scaled between 0 and 1.0 that can be used to 
 * set speed controllers or can be used as the input to a 
 * <p>
 * Utility class for handling Robot drive based on a definition of the wheel configuration.
 * The robot drive class handles basic driving for a robot. Currently, 2 and 4 wheel tank and 
 * mecanum drive trains are supported. In the future other drive types like swerve might
 * be implemented. wheel channel numbers are supplied on creation of the class. Those are
 * used for either the drive function (intended for hand created drive code, such as autonomous)
 * or with the Tank/Arcade functions intended to be used for Operator Control driving.
 */
public class RunnymedeMecanumDrive {

	// wheel definitions for arrays
	private static int FRONT_LEFT  = 0;
	private static int REAR_LEFT   = 1;
	private static int FRONT_RIGHT = 2;
	private static int REAR_RIGHT  = 3;
	private static int MOTOR_COUNT = 4;

	private final boolean [] motorInverted;
	
	// Value below which the motors will be shut off instead of driven.  The motors will
	// drive at this value.
	private double MIN_DRIVE_VALUE = 0.02;

	/**
     * Constructor for RobotDrive with 4 wheels specified as SpeedController objects.
     * Speed controller input version of RobotDrive (see previous comments).
     * @param rearLeftMotorInverted  - true if the wheel is inverted, false otherwise
     * @param frontLeftMotorInverted - true if the wheel is inverted, false otherwise
     * @param rearRightMotorInverted  - true if the wheel is inverted, false otherwise
     * @param frontRightMotorInverted - true if the wheel is inverted, false otherwise
     */
    public RunnymedeMecanumDrive(boolean frontLeftMotorInverted, boolean rearLeftMotorInverted,
                      boolean frontRightMotorInverted, boolean rearRightMotorInverted) {

    	motorInverted = new boolean [] { frontLeftMotorInverted,
    									 rearLeftMotorInverted,
    			                         frontRightMotorInverted,
    			                         rearRightMotorInverted };
    }
    
    /**
     * Calculate the mecanum wheel speeds to drive the mecanum drive.
     *
     * A method for driving with Mecanum wheeled robots. There are 4 wheels
     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
     * When looking at the wheels from the top, the roller axles should form an X across the robot.
     *
     * @param p - PolarCoordinate (r, theta) containing the required magnitude and direction for the  
     * mecanum drive
     * @param rotation The rate of rotation for the robot that is completely independent of
     * the magnitute or direction. [-1.0..1.0]
     * @return double [] of wheel speeds that can be used to set the wheels.
     */
    public double [] drivePolar(PolarCoordinate p, double rotation) {
    	
    	double [] wheelSpeeds = new double [] { 0.0, 0.0, 0.0, 0.0 };

    	// If there is no movement requested, then return zeros.
    	if (p.getR() < MIN_DRIVE_VALUE && Math.abs(rotation) < MIN_DRIVE_VALUE) { return wheelSpeeds; }
    	
    	// Calculate the non-roational normalized wheel speeds required to 
    	// obtain a vector at angle theta.
    	// The rollers are at 45 degree angles.

    	double angleRad = (p.getTheta() + 45.0) * Math.PI / 180.0;
        double cosD = Math.cos(angleRad);
        double sinD = Math.sin(angleRad);

        wheelSpeeds[FRONT_LEFT]  = sinD;
        wheelSpeeds[FRONT_RIGHT] = cosD;
        wheelSpeeds[REAR_LEFT]   = cosD;
        wheelSpeeds[REAR_RIGHT]  = sinD;

		/*
		 * Scale the wheel speeds so that the maximum wheel speed is at the
		 * r value of the polar coordinate r, theta. 
		 */
		scaleWheelSpeeds(wheelSpeeds, p.getR());
		
		/* 
		 * Add the rotation to the wheels on the left and subtract it from the 
		 * wheels on the right so that the robot spins in place.
		 * Positive rotations will be clockwise and negative rotations will be
		 * counter clockwise.
		 */ 

		wheelSpeeds[FRONT_LEFT]  += rotation;
		wheelSpeeds[REAR_LEFT]   += rotation;
		wheelSpeeds[FRONT_RIGHT] -= rotation;
		wheelSpeeds[REAR_RIGHT]  -= rotation;
		
		// Check the wheel speeds are not above 1.0, and scale them appropriately.
        normalizeWheelSpeeds(wheelSpeeds);
        
        // Invert wheel speeds
        invertWheelSpeeds(wheelSpeeds);
        
        return wheelSpeeds;
    }

    private double getMaxWheelSpeedMagnitude(double [] wheelSpeeds) {

    	double maxMagnitude = Math.abs(wheelSpeeds[0]);

        for (int i=1; i<MOTOR_COUNT; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) maxMagnitude = temp;
        }

        return maxMagnitude;
    }

    /**
     * Invert wheel speeds for any motors that are inverted.
     */
    private void invertWheelSpeeds(double wheelSpeeds[]) {
    	
        for (int i=0; i<MOTOR_COUNT; i++) {
        	if (motorInverted[i]) {
                wheelSpeeds[i] = - wheelSpeeds[i];
        	}
        }
    }
    
	/**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
     */
    private void normalizeWheelSpeeds(double wheelSpeeds[]) {

    	double maxMagnitude = getMaxWheelSpeedMagnitude(wheelSpeeds);
        
        if (maxMagnitude > 1.0) {
            for (int i=0; i<MOTOR_COUNT; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }

    /**
	 * Scale the wheel speeds so that they maintain the same relative magnitude, but
	 * the maximum value is the passed in maxValue.
	 */
	private void scaleWheelSpeeds(double [] wheelSpeeds, double maxValue) {
		
		// If the maxValue is 0, then all of the values will be scaled to 0.
		if (maxValue < MIN_DRIVE_VALUE) {
			for (int i=0; i<wheelSpeeds.length; i++) { 
				wheelSpeeds[i] = 0; 
			}
			return;
		}
		
		// If the maxValue is > 1.0, then scale the values to a maximum of 1.0
		if (maxValue > 1.0) { maxValue = 1.0; }
		
		double maxMagnitude = getMaxWheelSpeedMagnitude(wheelSpeeds);
		
		// If the maximum value is 0, then there is nothing to do for the scaling.
		if (maxMagnitude < 0.02) { 
			for (int i=0; i<wheelSpeeds.length; i++) { 
				wheelSpeeds[i] = 0; 
			}
			return;
		}
		
		// Get the scaling factor for the wheels
		double scalingFactor = maxValue / maxMagnitude;
		
		// Scale all of the wheel values
		for (int i=0; i<wheelSpeeds.length; i++) {
			wheelSpeeds[i] = wheelSpeeds[i] * scalingFactor;
		}
		
	}

}
