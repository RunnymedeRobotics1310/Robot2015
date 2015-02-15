package robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LinearRamp {

	private long startTime = 0;
	private long endTime = 0;
	private double startValue = 0.0d;
	private double targetValue = 0.0d;

	private boolean enable = false;
	
	/**
	 * This routine ramps between two values linearly over time.  
	 * <p>
	 * The ramp cannot be re-started once it is started.  In order to restart
	 * a ramp it must be disabled using the disable command.
	 *  
	 * @param t - the time to ramp between the two values in seconds.
	 * @param curentValue - the current value to start the ramp
	 * @param targetValue - the target value to end the ramp
	 * @return double - current value - returns the start value
	 */
	public double start(double t, double startValue, double targetValue) {
		
		// Do not re-start a LinearRamp that is currently active. 
		if (enable) { return 0.0; }
			
		this.startValue = startValue;
		this.targetValue = targetValue;
		this.startTime = System.currentTimeMillis();
		this.endTime = this.startTime + Math.round(t*1000);
		this.enable = true;
		return this.startValue;
	}
	
	/**
	 * Get the current value of the ramp
	 * @return current value
	 */
	public double getValue() {
		
		long currentTime = System.currentTimeMillis();

		if (currentTime > endTime) { return this.targetValue; }
		
		double completeRatio = ((currentTime - this.startTime) * 1.0d) / ((this.endTime - this.startTime) * 1.0d);

		double rtnVal =  startValue + ((targetValue-startValue)*completeRatio);

		SmartDashboard.putNumber("RampStartValue", startValue);
		SmartDashboard.putNumber("RampEndValue", targetValue);
		
		SmartDashboard.putNumber("RampCompleteRatio", completeRatio);
		SmartDashboard.putNumber("RampValue", rtnVal);
		
		return rtnVal;
		
	}

	/**
	 * Is this LinearRamp enabled
	 * @return {@literal true} if enabled, {@literal false} otherwise
	 */
	public boolean isEnable() {
		return enable;
	}
	
	/**
	 * Is this LinearRamp complete
	 * <p>
	 * This routine returns true if the ramp is disabled.
	 * @return {@literal true} if the ramp is complete, {@literal false} otherwise
	 */
	public boolean isComplete() {
		
		if (!isEnable()) { return true; }
		
		return System.currentTimeMillis() > endTime;
	}
	
	/** 
	 * Disable the LinearRamp.  This will allow the ramp to be re-started.
	 */
	public boolean disable() { 
		enable = false;
		return enable;
	}
}
