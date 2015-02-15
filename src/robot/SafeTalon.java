package robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

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
	
	public static double ENCODER_DISTANCE_NO_LIMIT = 0.00001d;
	
	DigitalInput negativeLimitSwitch = null;
	DigitalInput positiveLimitSwitch = null;

	Encoder limitEncoder = null;
	double lowerLimitEncoderDistance = ENCODER_DISTANCE_NO_LIMIT;
	double upperLimitEncoderDistance = ENCODER_DISTANCE_NO_LIMIT;
	
	int powerDistributionPort = -1;
	double currentLimit = 0;
	double currentLimitFuseDelay = 0;
	Timer currentLimitTimer = null;
	
	public SafeTalon(int channel) {
		super(channel);
	}
	
	@Override
	public void pidWrite(double output) {
        set(output);
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
	 * @param limitEncoder encoder to use.  This routine uses the getDistance() value returned from the encoder.
	 * @param lowerLimit lower limit or {@link #ENCODER_DISTANCE_NO_LIMIT}
	 * @param upperLimit upper limit or {@link #ENCODER_DISTANCE_NO_LIMIT}
	 */
	public void setEncoderLimit(Encoder limitEncoder, double lowerLimit, double upperLimit) {
		this.limitEncoder = limitEncoder;
		this.lowerLimitEncoderDistance = lowerLimit;
		this.upperLimitEncoderDistance = upperLimit;
	}
	
	public void setNegativeLimitSwitch(DigitalInput negativeLimitSwitch) {
		this.negativeLimitSwitch = negativeLimitSwitch;
	}
	
	public void setPositiveLimitSwitch(DigitalInput positiveLimitSwitch) {
		this.positiveLimitSwitch = positiveLimitSwitch;
	}
	
	private boolean checkOverCurrent(double speed) {
		
		// The port and current limit must be specified for this check.
		if (powerDistributionPort < 0 || currentLimit == 0) { return false; }
		
		if (currentLimitFuseDelay == 0) {
			
			if (Robot.powerSubsystem.getCurrent(powerDistributionPort) > currentLimit) {
				return true;
			} else {
				return false;
			}
		}
		
		
		return false;
	}

	private double safeSpeed(double speed) {
		
		// Check for a negative speed limit switch
		if (negativeLimitSwitch != null) {
			if (speed < 0 && negativeLimitSwitch.get()) {
				return 0.0d;
			}
		}
		
		// Check for a positive speed limit switch
		if (positiveLimitSwitch != null) {
			if (speed > 0 && positiveLimitSwitch.get()) {
				return 0.0d;
			}
		}

		// Check Limit encoder counts
		if (limitEncoder != null) {
			if (upperLimitEncoderDistance != ENCODER_DISTANCE_NO_LIMIT) {
				if (limitEncoder.getDistance() > upperLimitEncoderDistance) {
					return 0.0d;
				}
			}
			if (lowerLimitEncoderDistance != ENCODER_DISTANCE_NO_LIMIT) {
				if (limitEncoder.getDistance() < lowerLimitEncoderDistance) {
					return 0.0d;
				}
			}
		}
		
		// Check for over current
		if (checkOverCurrent(speed)) { 
			return 0.0d;
		}
		
		return speed;
	}
}
