package robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Safe Talon
 * 
 * This class extends the Talon drive class and implements safety features around the 
 * talon drive.
 * 
 * Safety features implemented include
 * <br>Limit switches - a switch which stops the motor turning in a specified direction.
 * <br>Encoder limit - stop motor turning in a specified direction when the encoder limit changes
 * <br>Power output limit - set the maximum current for this motor
 * <br>Encoder limit - stop motor turning in a specified direction when the encoder limit changes
 * 
 */
public class SafeTalon extends Talon {
	
	public enum TalonState { ENABLED, DISABLED, 
		NEGATIVE_LIMIT_SWITCH, POSITIVE_LIMIT_SWITCH,
		LOWER_ENCODER_LIMIT, UPPER_ENCODER_LIMIT, NO_ENCODER,
		OVER_CURRENT, NO_POWER_PORT }

	public static double ENCODER_DISTANCE_NO_LIMIT = 0.00001d;;
	public static double CURRENT_NO_LIMIT = 0d;
	
	private TalonState talonState = (super.isAlive() ? TalonState.ENABLED : TalonState.DISABLED);
	
	private int channel = -1;
	
	private DigitalInput negativeLimitSwitch = null;
	private DigitalInput positiveLimitSwitch = null;
	
	private boolean negativeLimitSwitchDefaultState = false;
	private boolean positiveLimitSwitchDefaultState = false;
	
	private Counter positiveLimitSwitchCounter = null;
	private Counter negativeLimitSwitchCounter = null;

	private Encoder limitEncoder = null;
	private double prevEncoderDistance = 0.0d;
	private double lowerLimitEncoderDistance = ENCODER_DISTANCE_NO_LIMIT;
	private double upperLimitEncoderDistance = ENCODER_DISTANCE_NO_LIMIT;
	private Timer encoderTimer = new Timer(1.0d);
	
	private int powerDistributionPort = -1;
	private double currentLimit = 0;
	private double currentLimitFuseDelay = 0;
	private Timer currentLimitTimer = null;
	private double current = 0;
	private double peakCurrent = 0;
	
	public SafeTalon(int channel) {
		super(channel);
		this.channel = channel;
	}
	
	/**
	 * Get the current state of this Talon
	 * @return TalonState - the current state of this talon
	 */
	public TalonState getState() { return talonState; }

	@Override
	public void pidWrite(double output) {
        set(output);
    }

	/**
	 * Reset this Talon
	 * The reset command is used to reset the motor from an overcurrent condition or 
	 * other latching condition that causes the Talon to be unusable.
	 */
	public void reset() {
		talonState = (super.isAlive() ? TalonState.ENABLED : TalonState.DISABLED);
		peakCurrent = 0;
	}

	@Override
    public void set(double speed) {
    	super.set(safeSpeed(speed));
    }
	
	@Override
	@Deprecated
    public void set(double speed, byte syncGroup) {
		super.set(safeSpeed(speed), syncGroup);
    }
	
	/**
	 * Set a safe encoder limit on this motor.
	 * <p>
	 * This method will associate the encoder with the motor and will create an encoder fault if the 
	 * speed on the motor is set greater than 0.1 for 1 second and the motor encoder counts do not move.
	 * @param limitEncoder encoder to use.  This routine uses the getDistance() value returned from the encoder.
	 * @param lowerLimit lower limit or {@link #ENCODER_DISTANCE_NO_LIMIT}
	 * @param upperLimit upper limit or {@link #ENCODER_DISTANCE_NO_LIMIT}
	 */
	public void setEncoder(Encoder limitEncoder, double lowerLimit, double upperLimit) {
		this.limitEncoder = limitEncoder;
		this.lowerLimitEncoderDistance = lowerLimit;
		this.upperLimitEncoderDistance = upperLimit;
	}
	
	/**
	 * Set the negative limit switch for this Talon.  
	 * <p>
	 * This limit switch will stop the motor (set the talon to zero) if the limit switch
	 * is engaged and the motor set value is negative (<0).  Any calls to {@link #pidWrite(double)}
	 * or {@link #set(double)} that contain a negative value will be overridden to zero.  Positive
	 * motor speeds will still be accepted.
	 * <p>
	 * @param negativeLimitSwitch
	 */
	public void setNegativeLimitSwitch(DigitalInput negativeLimitSwitch) {
		setNegativeLimitSwitch(negativeLimitSwitch, true);
	}

	/**
	 * Set the negative limit switch for this Talon.  
	 * <p>
	 * This limit switch will stop the motor (set the talon to zero) if the limit switch
	 * is engaged and the motor set value is negative (<0).  Any calls to {@link #pidWrite(double)}
	 * or {@link #set(double)} that contain a negative value will be overridden to zero.  Positive
	 * motor speeds will still be accepted.
	 * 
	 * @param negativeLimitSwitch - a digital input used for this limit switch.
	 * @param defaultState - the default state for this limit switch. The value opposite of the default
	 * state is used to determine if the switch is engaged.  
	 */
	public void setNegativeLimitSwitch(DigitalInput negativeLimitSwitch, boolean defaultState) {
		this.negativeLimitSwitch             = negativeLimitSwitch;
		this.negativeLimitSwitchDefaultState = defaultState;
		
		// If the default state is true, the counter should detect falling edges
		this.negativeLimitSwitchCounter = new Counter(this.negativeLimitSwitch);
		if (defaultState == false) {
			this.negativeLimitSwitchCounter.setUpSourceEdge(true, false);
		} else {
			this.negativeLimitSwitchCounter.setUpSourceEdge(false, true);
		}
		this.negativeLimitSwitchCounter.reset();
	}

	/**
	 * Set the Over Current fuse
	 * <p>
	 * This overcurrent fuse will protect the motors from an overcurrent situation.  The overcurrent
	 * values can be set to be triggered on a time delay fuse by setting the fuse delay to a non-zero value.
	 * <p>
	 * In order to reset the fuse, the {@link #reset()} method must be called.
	 * @param powerDistributionPort - the port on the power distribution panel
	 * @param currentLimit - the current limit in Amps
	 * @param fuseDelay - the time delay for the fuse in seconds.  Zero seconds will trip the fuse
	 * immediately
	 */
	public void setOverCurrentFuse(int powerDistributionPort, double currentLimit, double fuseDelay) {
		this.powerDistributionPort = powerDistributionPort;
		this.currentLimit = currentLimit;
		this.currentLimitFuseDelay = fuseDelay;
		if (this.currentLimitFuseDelay > 0.0d) {
			currentLimitTimer = new Timer(currentLimitFuseDelay);
		} else {
			currentLimitTimer = null;
		}
	}
	
	/**
	 * Set the positive limit switch for this Talon.  
	 * <p>
	 * This limit switch will stop the motor (set the talon to zero) if the limit switch
	 * is engaged and the motor set value is positive (>0).  Any calls to {@link #pidWrite(double)}
	 * or {@link #set(double)} that contain a positive value will be overridden to zero.  Negative
	 * motor speeds will still be accepted.
	 * 
	 * @param positiveLimitSwitch - a digital input used for this limit switch.  Assumes the default
	 * state for the switch is {@literal false} and a switch state of {@literal true} indicates the
	 * limit has been hit.
	 */
	public void setPositiveLimitSwitch(DigitalInput positiveLimitSwitch) {
		setPositiveLimitSwitch(positiveLimitSwitch, true);
	}

	/**
	 * Set the positive limit switch for this Talon.  
	 * <p>
	 * This limit switch will stop the motor (set the talon to zero) if the limit switch
	 * is engaged and the motor set value is positive (>0).  Any calls to {@link #pidWrite(double)}
	 * or {@link #set(double)} that contain a positive value will be overridden to zero.  Negative
	 * motor speeds will still be accepted.
	 * 
	 * @param positiveLimitSwitch - a digital input used for this limit switch.
	 * @param defaultState - the default state for this limit switch. The value opposite of the default
	 * state is used to determine if the switch is engaged.  
	 */
	public void setPositiveLimitSwitch(DigitalInput positiveLimitSwitch, boolean defaultState) {
		this.positiveLimitSwitch             = positiveLimitSwitch;
		this.positiveLimitSwitchDefaultState = defaultState;
		
		// If the default state is true, the counter should detect falling edges
		this.positiveLimitSwitchCounter = new Counter(this.positiveLimitSwitch);
		if (defaultState == false) {
			this.positiveLimitSwitchCounter.setUpSourceEdge(true, false);
		} else {
			this.positiveLimitSwitchCounter.setUpSourceEdge(false, true);
		}
		this.positiveLimitSwitchCounter.reset();
	}

	@Override
	public void updateTable() {
		super.updateTable();
		
		SmartDashboard.putString("Talon(" + channel + ")", this.getState().toString());
		
		if (positiveLimitSwitch != null) {
			SmartDashboard.putBoolean("Talon(" + channel + ") positive limit switch", positiveLimitSwitch.get() );
			SmartDashboard.putNumber("Talon(" + channel + ") positive limit switch counter", positiveLimitSwitchCounter.get() );
		} else {
			SmartDashboard.putBoolean("Talon(" + channel + ") positive limit switch", true );
			SmartDashboard.putNumber("Talon(" + channel + ") positive limit switch counter", 0 );
		}
		
		if (negativeLimitSwitch != null) {
			SmartDashboard.putBoolean("Talon(" + channel + ") negative limit switch", negativeLimitSwitch.get() );
			SmartDashboard.putNumber("Talon(" + channel + ") negative limit switch counter", negativeLimitSwitchCounter.get() );
		} else {
			SmartDashboard.putBoolean("Talon(" + channel + ") negative limit switch", true );
			SmartDashboard.putNumber("Talon(" + channel + ") negative limit switch counter", 0);
		}
		
		if (powerDistributionPort >= 0) {
			SmartDashboard.putNumber("Talon(" + channel + ") current", current );
			SmartDashboard.putNumber("Talon(" + channel + ") peak current", peakCurrent );
		} else {
			SmartDashboard.putNumber("Talon(" + channel + ") current", 0 );
			SmartDashboard.putNumber("Talon(" + channel + ") peak current", 0 );
		}
		
	}
	
	// An encoder fault occurs if the speed setting is > 0.1 for 1 second
	// and the encoder is not moving.
	private boolean checkEncoderFault(double speed) {
		
		// The encoder must be specified for this check.
		if (limitEncoder == null) { return false; }

		// If the encoder is not zero, then there is no fault
		if (limitEncoder.getDistance() != prevEncoderDistance) {
			prevEncoderDistance = limitEncoder.getDistance();
			encoderTimer.disable();
			return false;
		}
		
		// If the speed is > 0.1, then the encoder count should be changing.
		if (Math.abs(speed) > 0.1d) {
			encoderTimer.start();
			if (encoderTimer.isExpired()) { 
				return true; 
			}
		}
		
		return false;
	}

	private boolean checkOverCurrent(double speed) {
		
		// The port and current limit must be specified for this check.
		if (powerDistributionPort < 0) { return false; }

		// Record the peak current
		current = Robot.powerSubsystem.getCurrent(powerDistributionPort);
		
		if (current > peakCurrent) {
			peakCurrent = current;
		}
		
		// If the current limit is not set then there is no over current check
		if (currentLimit == 0) { return false; }

		// If the current is within bounds then there is no overcurrent.
		if (current <= currentLimit) {
			if (currentLimitTimer != null) {
				currentLimitTimer.disable();
			}
			return false;
		}
		
		// If there is no delay on the fuse, then an overcurrent is detected.
		if (currentLimitFuseDelay == 0) { return true; }
		
		// Start a timer for the fuse.
		currentLimitTimer.start();

		if (currentLimitTimer.isExpired()) { return true; }
		
		return false;
	}

	private double safeSpeed(double speed) {

		// Overcurrent is latching and can only be reset by operator input
		switch (talonState) {
		case OVER_CURRENT:
		case NO_ENCODER:
			return 0.0;
		default: break;
		}
		
		// Check the talon is alive
		talonState = (super.isAlive() ? TalonState.ENABLED : TalonState.DISABLED);
		
		// If the talon is not alive, then set the speed to zero and return
		if (talonState != TalonState.ENABLED) { return 0.0d; }
		
		if (checkEncoderFault(speed)) {
			talonState = TalonState.NO_ENCODER;
			return 0.0d;
		}
		
		// Check for over current
		if (checkOverCurrent(speed)) { 
			talonState = TalonState.OVER_CURRENT;
			return 0.0d;
		}
		
		// Check for a negative speed limit switch
		if (negativeLimitSwitch != null) {
			if (negativeLimitSwitchCounter.get() > 0 || negativeLimitSwitch.get() != negativeLimitSwitchDefaultState) {
				talonState = TalonState.NEGATIVE_LIMIT_SWITCH;
				if (speed < 0) {
					return 0.0d;
				}
			}
			// The negative limit switch counter will be reset when the speed is set positive and the limit switch is 
			// no longer set.
			if (       negativeLimitSwitchCounter.get() > 0 
					&& negativeLimitSwitch.get() == negativeLimitSwitchDefaultState 
					&& speed > 0) {
				negativeLimitSwitchCounter.reset();
			}
		}
		
		// Check for a positive speed limit switch
		if (positiveLimitSwitch != null) {
			if (positiveLimitSwitchCounter.get() > 0 || positiveLimitSwitch.get() != positiveLimitSwitchDefaultState) {
				talonState = TalonState.POSITIVE_LIMIT_SWITCH;
				if (speed > 0) {
					return 0.0d;
				}
			}
			// The positive limit switch counter will be reset when the speed is set negative and the limit switch is 
			// no longer set.
			if (       positiveLimitSwitchCounter.get() > 0 
					&& positiveLimitSwitch.get() == positiveLimitSwitchDefaultState 
					&& speed < 0) {
				positiveLimitSwitchCounter.reset();
			}
		}

		// Check Limit encoder counts
		if (limitEncoder != null) {
			if (upperLimitEncoderDistance != ENCODER_DISTANCE_NO_LIMIT) {
				if (limitEncoder.getDistance() > upperLimitEncoderDistance) {
					talonState = TalonState.UPPER_ENCODER_LIMIT;
					return 0.0d;
				}
			}
			if (lowerLimitEncoderDistance != ENCODER_DISTANCE_NO_LIMIT) {
				if (limitEncoder.getDistance() < lowerLimitEncoderDistance) {
					talonState = TalonState.LOWER_ENCODER_LIMIT;
					return 0.0d;
				}
			}
		}
		
		return speed;
	}
}
