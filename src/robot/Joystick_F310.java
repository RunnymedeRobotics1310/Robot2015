package robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joystick_F310 {

	// Each of the sticks has 2 axis
	private enum Axis { X, Y, Z };

	// Each of the sticks has 2 axis
	private enum JoystickMode { D, X };

	// The following buttons are defined for the F310 Joystick.
	public enum F310Button { 
		A       ( 1, 2),
		B       ( 2, 3), 
		X       ( 3, 1),
		Y       ( 4, 4),
		LB      ( 5, 5),
		RB      ( 6, 6),
		LT      (-1, 7),
		RT      (-2, 8),
		BACK    ( 7, 9),
		START   ( 8,10),
		L_STICK ( 9,11),
		R_STICK (10,12);

		int xModeButtonNumber, dModeButtonNumber;
		F310Button(int xModeButtonNumber, int dModeButtonNumber) {
			this.xModeButtonNumber = xModeButtonNumber;
			this.dModeButtonNumber = dModeButtonNumber;
		}

		static F310Button toEnum(int buttonNumber, JoystickMode joystickMode) {
			for(F310Button button: F310Button.values()) {
				if (joystickMode == JoystickMode.D) {
					if (buttonNumber == button.dModeButtonNumber) {
						return button;
					}
				} else {
					if (buttonNumber == button.xModeButtonNumber) {
						return button;
					}
				}
			}
			return null;
		}
	}

	// The F310 has 2 sticks
	public enum F310Stick { LEFT, RIGHT }

	private enum F310StickPorts { 
		LEFT_X_AXIS (0,0),
		LEFT_Y_AXIS (1,1),
		LEFT_Z_AXIS (2,2), //FIXME:z axis doesn't exist

		RIGHT_X_AXIS (4,4),
		RIGHT_Y_AXIS (5,5);

		int xModeStickPort, dModeStickPort;
		F310StickPorts(int xModeStickPort, int dModeStickPort) {
			this.xModeStickPort = xModeStickPort;
			this.dModeStickPort = dModeStickPort;
		}
	}

	private static int LEFT_X_AXIS = 0;
	private static int LEFT_Y_AXIS = 1;
	private static int LEFT_Z_AXIS = 2;

	private static int RIGHT_X_AXIS = 4;
	private static int RIGHT_Y_AXIS = 5;
	private static int RIGHT_Z_AXIS = 3;

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

	/**
	 * Determine whether the requested button was pressed.  In X-configuration, the rear buttons RT and LT
	 * are returned as a button press if the axis reads greater than 0.1.  In D-configuration these 
	 * are always returned as buttons.
	 * @param button to check
	 * @return true if pressed, false if not pressed.
	 */
	public boolean getButton(F310Button button) {
		if (getJoystickMode() == JoystickMode.D) {
			return joystick.getRawButton(button.dModeButtonNumber);
		} else {
			if (button.xModeButtonNumber > 0) {
				return joystick.getRawButton(button.xModeButtonNumber);
			}
			// If this is not a joystick button, then this is one of the 
			// RT buttons, so look for a stick value > .1 on the Z axis instead of a button.
			if (button.xModeButtonNumber == -1) {
				if (getRawAxis(F310Stick.LEFT, Axis.Z) > 0.1d) {
					return true;
				} else {
					return false;
				}
			} else if (button.xModeButtonNumber == -2) {
				if (getRawAxis(F310Stick.RIGHT, Axis.Z) > 0.1d) {
					return true; 
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public String getButtonsPressedString() {

		JoystickMode joystickMode = getJoystickMode();

		String buttonString = "" + joystickMode + " ";

		for (F310Button button : F310Button.values()) {
			if (getButton(button)) {
				if (joystickMode == JoystickMode.X) {
					buttonString += button + "(" + button.xModeButtonNumber + ")";
				} else {
					buttonString += button + "(" + button.dModeButtonNumber + ")";
				}
			}
		}

		if (isPOVPressed()) {
			buttonString += "POV(" + joystick.getPOV() + ")";
		}

		return buttonString;
	}

	private JoystickMode getJoystickMode() {
		if (joystick.getButtonCount() > 10) {
			return JoystickMode.D;
		} else {
			return JoystickMode.X;
		}
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

		switch (stick) {
		case LEFT:
			switch (axis) {
			case X:
				return joystick.getRawAxis(LEFT_X_AXIS);
			case Y:
				return joystick.getRawAxis(LEFT_Y_AXIS);
			case Z:
				return joystick.getRawAxis(LEFT_Z_AXIS);
			}
			break;
		case RIGHT:
			switch (axis) {
			case X:
				return joystick.getRawAxis(RIGHT_X_AXIS);
			case Y:
				return joystick.getRawAxis(RIGHT_Y_AXIS);
			case Z:
				return joystick.getRawAxis(RIGHT_Z_AXIS);
			}
		}
		return 0.0d;
	}

}
