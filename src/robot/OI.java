package robot;

import robot.Joystick_F310.F310Button;
import robot.Joystick_F310.F310Stick;
import robot.commands.TeleopDriveToAngleCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * <p>
 * This class does not generate any commands.  The operator input is used in 
 * the default command of subsystems in order to run the subsystems.
 * 
 */
public class OI {
	
	private enum ButtonMap {
		
		// Driver Joystick button mapping
		SOUTH  (F310Button.A),
		NORTH  (F310Button.Y),
		EAST   (F310Button.B),
		WEST   (F310Button.X),
		
		PICKUP_MOTORS   (F310Button.LB),
		DEPLOY_PICKUP   (F310Button.RB),
		LEFT_EYEBROW    (F310Button.LT),
		RIGHT_EYEBROW   (F310Button.RT),
		
		RELAY_ON      (F310Button.Y),
		RELAY_FORWARD (F310Button.B),
		RELAY_REVERSE (F310Button.X);
		
		

		F310Button button;
		
		ButtonMap(F310Button button) {
			this.button = button;
		}
		
		F310Button getButton() { return this.button; }
	}
	private enum StickMap {
		
		// Driver Joystick stick mapping
		DRIVE_STICK    (F310Stick.LEFT),
		ROTATION_STICK (F310Stick.RIGHT);
		
		F310Stick stick;
		
		StickMap(F310Stick stick) {
			this.stick = stick;
		}
		
		F310Stick getStick() { return this.stick; }
	}

	private PIDEnable motorPIDEnable    = PIDEnable.ENABLED;
	
	private PIDEnable rotationPIDEnable = PIDEnable.ENABLED;

	private Joystick_F310 driverJoystick = new Joystick_F310(0);
	
	private NetworkTableOI networkTableOI = new NetworkTableOI();

	public int getDirectionPointer() { 
 		
 		if (   driverJoystick.getButton(ButtonMap.NORTH.getButton()) 
 			&& driverJoystick.getButton(ButtonMap.EAST .getButton())) { return 45; }
 		
 		if (   driverJoystick.getButton(ButtonMap.NORTH.getButton()) 
 			&& driverJoystick.getButton(ButtonMap.WEST .getButton())) { return 315; }

 		if (   driverJoystick.getButton(ButtonMap.SOUTH.getButton()) 
 	 		&& driverJoystick.getButton(ButtonMap.EAST .getButton())) { return 135; }

 		if (   driverJoystick.getButton(ButtonMap.SOUTH.getButton()) 
 	 		&& driverJoystick.getButton(ButtonMap.WEST .getButton())) { return 225; }

 		if (driverJoystick.getButton(ButtonMap.NORTH.getButton())) { return   0; }
 		if (driverJoystick.getButton(ButtonMap.EAST .getButton())) { return  90; }
 		if (driverJoystick.getButton(ButtonMap.SOUTH.getButton())) { return 180; }
 		if (driverJoystick.getButton(ButtonMap.WEST .getButton())) { return 270; }
 		
 		return -1;
 	}
	
	public PolarCoordinate getDriverPolarCoordinate() { 
		// Square the coordinates to reduce joystick sensitivity.
		return driverJoystick.getPolarCoordinate(StickMap.DRIVE_STICK.getStick()).square(); 
	}
	
	public int getDriverPov() { 
		return driverJoystick.getPOV(); }

	public double getDriverRotation() { 
		// Square the coordinates to reduce joystick sensitivity.
		return driverJoystick.getCartesianCoordinate(StickMap.ROTATION_STICK.getStick()).square().getX(); 
	}

	public PIDEnable getMotorPIDEnable() { return motorPIDEnable; }

	public CartesianCoordinate getMouseEvent() {
		return networkTableOI.getMouseEvent();
	}
	
 	public PIDEnable getRotationPIDEnable() { return rotationPIDEnable; }

 	public boolean getPickupButton() {
		return driverJoystick.getButton(ButtonMap.DEPLOY_PICKUP.getButton());
	}
	
	public boolean getLeftEyebrowButton() {
		return driverJoystick.getButton(ButtonMap.LEFT_EYEBROW.getButton());
	}
	
	public boolean getRightEyebrowButton() {
		return driverJoystick.getButton(ButtonMap.RIGHT_EYEBROW.getButton());
	}
	
	public boolean getPickupRollerButton() {
		return driverJoystick.getButton(ButtonMap.PICKUP_MOTORS.getButton());
	}

	// FIXME: What button is #12?
	public boolean getTogglePIDButton() { 
		return driverJoystick.getRawJoystick().getRawButton(12); }
	
	public void isNewMouseEvent() {
		networkTableOI.isNewMouseEvent();
	}
	
 	public void periodic() {
		
 		// Rotate to the requested angle.
		if (getDirectionPointer() >= 0) {
			Scheduler.getInstance().add(new TeleopDriveToAngleCommand(getDirectionPointer(), DriveMode.FIELD_RELATIVE));
			return;
		}
		
		// Update the relative angle
		if (getDriverPov() >= 0) {
			Robot.chassisSubsystem.resetGyro(getDriverPov());
		}
		
	}

	public void updateDashboard() {

 		networkTableOI.updateDashboard();
 		
		SmartDashboard.putString("Driver Joystick Buttons", 
				driverJoystick.getPolarCoordinate    (StickMap.DRIVE_STICK.getStick())   .square().toString() + " " +
				driverJoystick.getCartesianCoordinate(StickMap.ROTATION_STICK.getStick()).square().toString() + " " +
				driverJoystick.getButtonsPressedString()
				+ ((getDirectionPointer() >= 0) ? " D(" + getDirectionPointer() + ")" : "") );
	}
}

