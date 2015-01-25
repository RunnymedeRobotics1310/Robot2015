package robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joystick_F310 {

	// Each of the sticks has 2 axis
	private enum Axis { X, Y };
	
	// The following buttons are defined for the F310 Joystick.
	public enum F310Button { 
		A     (1),
		B     (2), 
		X     (3),
		Y     (4),
		LB    (5),
		RB    (6),
		BACK  (7),
		START (8);
		int buttonNumber;
		F310Button(int buttonNumber) {
			this.buttonNumber = buttonNumber;
		}
		
		static F310Button toEnum(int buttonNumber) {
			for(F310Button button: F310Button.values()) {
				if (button.buttonNumber == buttonNumber) {
					return button;
				}
			}
			return null;
		}
	}
	
	// The F310 has 2 sticks
	public enum F310Stick { LEFT, RIGHT }

	private static int LEFT_X_AXIS = 0;
	private static int LEFT_Y_AXIS = 1;

	private static int RIGHT_X_AXIS = 4;
	private static int RIGHT_Y_AXIS = 5;

	private final Joystick joystick;
	
	public Joystick_F310(int port) {
		joystick = new Joystick(port);
	}

	/**
	 * Get the Cartesian Coordinate for the specified stick.  
	 * <p>
	 * The x and y axis values will be retured as CartesianCoordinate.
	 * @param stick - Stick.LEFT, Stick.RIGHT
	 * @return - CartesianCoordinate for this stick.
	 */
	public CartesianCoordinate getCartesianCoordinate(F310Stick stick) {

		CartesianCoordinate xy = new CartesianCoordinate();
		
		xy.setX(getRawAxis(stick, Axis.X));
		
		// NOTE: The y axis values are inverted on the stick. A negative Y
		// value is north.
		xy.setY( - getRawAxis(stick, Axis.Y) );
		
		return xy;
	}

	/**
	 * Get the Polar Coordinate for the specified stick.  
	 * <p>
	 * Return the polar coordinate (r, theta) for this joystick.
	 * @param stick - Stick.LEFT, Stick.RIGHT
	 * @return - PolarCoordinate (r, theta) for this joystick
	 */
	public PolarCoordinate getPolarCoordinate(F310Stick stick) {

		return PolarCoordinate.valueOf(getCartesianCoordinate(stick));

	}

	public boolean getButton(F310Button button) {
		return joystick.getRawButton(button.buttonNumber);
	}

	public String getButtonsPressedString() {
		
		String buttonString = "";
		
		for (int i=1; i <= joystick.getButtonCount(); i++) {
			if (joystick.getRawButton(i)) {
				if (F310Button.toEnum(i) != null) {
					buttonString += F310Button.toEnum(i);
				}
				buttonString += "(" + i + ") ";
			}
		}
		
		if (isPOVPressed()) {
			buttonString += "POV(" + joystick.getPOV() + ")";
		}
		
		return buttonString;
	}
	
	/**
	 * Get the underlying Joystick object used for this Joystick_F310.
	 * @return Joystick - underlying Joystick object.
	 */
	public Joystick getRawJoystick() { return joystick; }
	
	/** 
	 * Get the Point of View control value
	 * @param stick - LEFT_STICK or RIGHT_STICK
	 * @return double - point of view value, 0, 45, 90, 135, 180, 225, 270, 315 or -1 if no POV buttons
	 * are pressed.
	 */
	public int getPOV() { return joystick.getPOV(); }

	/** 
	 * Get the Point of View control value
	 * @param stick - LEFT_STICK or RIGHT_STICK
	 * @return double - point of view value, 0, 45, 90, 135, 180, 225, 270, 315 or -1 if no POV buttons
	 * are pressed.
	 */
	public boolean isPOVPressed() { return joystick.getPOV() > -1; }
	
	/**
	 * Get the specified axis of the control stick
	 * @param axis - LEFT_X_AXIS, LEFT_Y_AXIS, RIGHT_X_AXIS, RIGHT_Y_AXIS
	 * @return - double representing the axis value from 0 to 1.0
	 */
	private double getRawAxis(F310Stick stick, Axis axis) {
		
		double axisValue = 0.0;
		
		if (stick == F310Stick.LEFT) {
			if (axis == Axis.X) {
				axisValue = joystick.getRawAxis(LEFT_X_AXIS);
			} else {
				axisValue = joystick.getRawAxis(LEFT_Y_AXIS);
			}
		} else {
			if (axis == Axis.X) {
				axisValue = joystick.getRawAxis(RIGHT_X_AXIS);
			} else {
				axisValue = joystick.getRawAxis(RIGHT_Y_AXIS);
			}
		}
		return axisValue;
	}
	
}
