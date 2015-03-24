package robot;

import edu.wpi.first.wpilibj.Joystick;

public class Joystick_Extreme3DPro {
	public enum Extreme3DProButton { 
		TRIGGER  ( 1),
		THUMB    ( 2), 
		BUTTON_3 ( 3),
		BUTTON_4 ( 4),
		BUTTON_5 ( 5),
		BUTTON_6 ( 6),
		BUTTON_7 ( 7),
		BUTTON_8 ( 8),
		BUTTON_9 ( 9),
		BUTTON_10(10),
		BUTTON_11(11),
		BUTTON_12(12);

		int buttonNumber;
		Extreme3DProButton(int buttonNumber) {
			this.buttonNumber = buttonNumber;
		}
	}
	
	private final Joystick joystick;
	/**
	 * @param port The usb port the Extreme3DPro will be plugged into (see driver station USB Devices tab)
	 */
	public Joystick_Extreme3DPro(int port) {
		joystick = new Joystick(port);
	}
	
	/**
	 * Determine whether the requested button was pressed.
	 * @param button to check
	 * @return true if pressed, false if not pressed.
	 */
	public boolean getButton(Extreme3DProButton button) {
		return joystick.getRawButton(button.buttonNumber);
	}
	
	public Joystick getRawJoystick() {
		return joystick;
	}
	
	public boolean getSlider() {
		if(joystick.getRawAxis(3) < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getPOV() {
		return joystick.getPOV();
	}
	
}
