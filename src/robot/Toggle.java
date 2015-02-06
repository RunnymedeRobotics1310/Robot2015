package robot;

public class Toggle {
	
	boolean state, previousControlValue;
	long time = System.currentTimeMillis();
	
	/**
	 * Create a Toggle and initialize it to this state.
	 * @param initialState - boolean value used to initialze the Toggle
	 */
	public Toggle(boolean initialState) {
		this.state = initialState;
	}
	
	/**
	 * This method is used to control the state of the Toggle and should be called
	 * every loop.  
	 * @param controlValue - a boolean indicating whether the toggle control is pressed.  The
	 * toggle will register transitions from false to true and will use the transitions to 
	 * toggle the state of the Toggle.
	 * @return boolean - the current state of the Toggle
	 */
	public boolean update(boolean controlValue) {
		
		// Change the state when the control value goes from off to on.  The state changes
		// on the transition only. 
		if (controlValue && !previousControlValue) {
			state = !state;
			time = System.currentTimeMillis();
		}
		
		// Save the control value in order to do proper edge detection.
		previousControlValue = controlValue;
		
		return state;
	}
	
	/**
	 * Get the current state of the Toggle
	 * @return boolean - indicating state of the toggle.
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * Set the current state of the Toggle
	 * @param state - the new state of the Toggle
	 * @return boolean - indicating state of the toggle after the change.
	 */
	public boolean setState(boolean state) {
		this.state = state;
		return this.state;
	}
	
	public long lastStateChangeTime() {
		return time;
	}

}
