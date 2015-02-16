package robot;

public class Timer {
	
	private double  timerTime = 0.0;
	private boolean isEnable = false;
	
	private long endTime = 0;
	
	/**
	 * Create a timer with the specified default duration. This duration will be used for 
	 * subsequent calls to {@link #start()}.  In order to override the value specified when
	 * the timer was created, use the {@link #start(double)} method to start the timer.
	 * @param seconds - the default number of seconds for this timer. 
	 */
	public Timer(double seconds) {
		this.timerTime = seconds;
	}
	
	/**
	 * Create a new timer.  
	 * 
	 * Use the method {@link #start(double)} to start the timer for a specified time and 
	 * the method {@link #isExpired()} to check if the timer is expired.
	 */
	public Timer() {}
	
	/**
	 * Disable the timer.
	 * <p>
	 * Turn this timer off and disable it.
	 */
	public void disable() { isEnable = false; }
	
	/**
	 * Is the Timer enabled.
	 * 
	 * @return boolean - {@literal true} if enabled, {@literal false} if disabled
	 */
	public boolean isEnable() { return isEnable; }
	
	
	/**
	 * Start the timer for the timer number of seconds specified when this object was created.
	 * <p>
	 * If the timer is already started, this command will not reset the timer.  The timer will
	 * continue as before.  To check the status of the timer use {@link #isExpired()}
	 */
	public void start() {
		start(timerTime);
	}

	/**
	 * Start the timer for the specified number of seconds.
	 * <p>
	 * If the timer is already started, this command will not reset the timer.  The timer will
	 * continue as before.  To check the status of the timer use {@link #isExpired()}
	 * 
	 * @param seconds - a double value indicating the number of seconds before this timer expires.
	 */
	public void start(double seconds) {
	
		if (isEnable) { return; }
		
		isEnable = true;
		endTime = Math.round(seconds*1000) + System.currentTimeMillis();
	}
	
	/**
	 * Is this timer expired
	 * <p>
	 * This routine will return false if the timer is not yet enabled.  A timer that is not
	 * started is also not yet expired.
	 * <p>
	 * Timers are enabled using the {@link #start()} or {@link #start(seconds)} method.
	 * 
	 * @return {@literal true} if the timer has expired, {@literal false otherwise}
	 */
	public boolean isExpired() {
		
		if (! isEnable) { return false; }
		
		return System.currentTimeMillis() > endTime;
	}
	
}
