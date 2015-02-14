package robot;

import robot.Joystick_Extreme3DPro.Extreme3DProButton;
import robot.Joystick_F310.F310Button;
import robot.Joystick_F310.F310Stick;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToClickCommand;
import robot.commands.ResetGyroCommand;
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

	private PIDEnable motorPIDEnable    = PIDEnable.ENABLED;
	private PIDEnable rotationPIDEnable = PIDEnable.ENABLED;
	private Joystick_F310 driverJoystick = new Joystick_F310(0);
	private Joystick_Extreme3DPro operatorJoystick = new Joystick_Extreme3DPro(1);
	private NetworkTableOI networkTableOI = new NetworkTableOI();
	private Toggle robotRelativeToggle = new Toggle(false);
	
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
	
	private enum Driver_ButtonMap {
		
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
		RELAY_REVERSE (F310Button.X),
		
		DRIVE_MODE    (F310Button.L_STICK);

		F310Button button;
		
		Driver_ButtonMap(F310Button button) {
			this.button = button;
		}
		
		F310Button getButton() { return this.button; }
	}
	
	private enum Operator_ButtonMap {
		
		// Operator Joystick button mapping
		OVERRIDE             (Extreme3DProButton.TRIGGER),
		ELEVATOR_LEVEL_FLOOR (Extreme3DProButton.THUMB),
		ELEVATOR_LEVEL_ONE   (Extreme3DProButton.BUTTON_3),
		ELEVATOR_LEVEL_TWO   (Extreme3DProButton.BUTTON_5),
		ELEVATOR_LEVEL_THREE (Extreme3DProButton.BUTTON_4),
		ELEVATOR_LEVEL_FOUR  (Extreme3DProButton.BUTTON_6),
		
		CLAW_TIPPER          (Extreme3DProButton.BUTTON_9),
		CLAW_UP_OVERRIDE     (Extreme3DProButton.BUTTON_10),
		CLAW_DOWN_OVERRIDE   (Extreme3DProButton.BUTTON_12);

		
		Extreme3DProButton button;
		
		Operator_ButtonMap(Extreme3DProButton button) {
			this.button = button;
		}
		
		Extreme3DProButton getButton() { return this.button; }
	}
	
	public int getDirectionPointer() { 
 		
 		if (   driverJoystick.getButton(Driver_ButtonMap.NORTH.getButton()) 
 			&& driverJoystick.getButton(Driver_ButtonMap.EAST .getButton())) { return 45; }
 		
 		if (   driverJoystick.getButton(Driver_ButtonMap.NORTH.getButton()) 
 			&& driverJoystick.getButton(Driver_ButtonMap.WEST .getButton())) { return 315; }

 		if (   driverJoystick.getButton(Driver_ButtonMap.SOUTH.getButton()) 
 	 		&& driverJoystick.getButton(Driver_ButtonMap.EAST .getButton())) { return 135; }

 		if (   driverJoystick.getButton(Driver_ButtonMap.SOUTH.getButton()) 
 	 		&& driverJoystick.getButton(Driver_ButtonMap.WEST .getButton())) { return 225; }

 		if (driverJoystick.getButton(Driver_ButtonMap.NORTH.getButton())) { return   0; }
 		if (driverJoystick.getButton(Driver_ButtonMap.EAST .getButton())) { return  90; }
 		if (driverJoystick.getButton(Driver_ButtonMap.SOUTH.getButton())) { return 180; }
 		if (driverJoystick.getButton(Driver_ButtonMap.WEST .getButton())) { return 270; }
 		
 		return -1;
 	}
	
	/*
	 * DRIVER GETTERS
	 */
	
	public DriveMode getDriveMode() {
		if (!robotRelativeToggle.getState()) {
			return DriveMode.FIELD_RELATIVE;
		}
		return DriveMode.ROBOT_RELATIVE;
	}
	
	public PolarCoordinate getDriverPolarCoordinate() { 
		// Square the coordinates to reduce joystick sensitivity.
		return driverJoystick.getPolarCoordinate(StickMap.DRIVE_STICK.getStick()).square(); 
	}
	
	public double getDriverRotation() { 
		// Square the coordinates to reduce joystick sensitivity.
		return driverJoystick.getCartesianCoordinate(StickMap.ROTATION_STICK.getStick()).square().getX(); 
	}
	
	public int getDriverPov() { return driverJoystick.getPOV(); }
	
	public boolean getLeftEyebrowButton() { return driverJoystick.getButton(Driver_ButtonMap.LEFT_EYEBROW.getButton()); }
	
 	public PIDEnable getMotorPIDEnable() { return motorPIDEnable; }
 	
 	public CartesianCoordinate getMouseEvent() { return networkTableOI.getMouseEvent(); }
 	
	public boolean getToteIntakeDeployButton() { return driverJoystick.getButton(Driver_ButtonMap.DEPLOY_PICKUP.getButton()); }
	
	public boolean getRightEyebrowButton() { return driverJoystick.getButton(Driver_ButtonMap.RIGHT_EYEBROW.getButton()); }
	
	public PIDEnable getRotationPIDEnable() { return rotationPIDEnable; }

	public boolean getPickupRollerButton() { return driverJoystick.getButton(Driver_ButtonMap.PICKUP_MOTORS.getButton()); }

	public boolean isNewMouseEvent() { return networkTableOI.isNewMouseEvent();	}
	
	public boolean getStartButton() { return driverJoystick.getButton(F310Button.START); }
	
	/*
	 * OPERATOR GETTERS
	 */
	
	public boolean getOverrideButton() {
		return operatorJoystick.getButton(Operator_ButtonMap.OVERRIDE.getButton());
	}
	
 	public void periodic() {
		
 		// Update all toggles
 		robotRelativeToggle.update(driverJoystick.getButton(Driver_ButtonMap.DRIVE_MODE.getButton()));
 		
 		// Rotate to the requested angle.
		if (getDirectionPointer() >= 0) {
			Scheduler.getInstance().add(new DriveToAngleCommand(getDirectionPointer(), DriveMode.FIELD_RELATIVE));
			return;
		}
		
		// Update the relative angle
		if (getDriverPov() >= 0) {
			Scheduler.getInstance().add(new ResetGyroCommand(getDriverPov()));
		return;
		}
		
		// Drive to a click
		if (isNewMouseEvent()) {
			Scheduler.getInstance().add(new DriveToClickCommand(getMouseEvent()));
		}
		
	}

	public void updateDashboard() {

 		networkTableOI.updateDashboard();
 
 		SmartDashboard.putString("DriveMode", getDriveMode().toString());
 		
		SmartDashboard.putString("Driver Joystick Buttons", 
				driverJoystick.getPolarCoordinate    (StickMap.DRIVE_STICK.getStick())   .square().toString() + " " +
				driverJoystick.getCartesianCoordinate(StickMap.ROTATION_STICK.getStick()).square().toString() + " " +
				driverJoystick.getButtonsPressedString()
				+ ((getDirectionPointer() >= 0) ? " D(" + getDirectionPointer() + ")" : "") );
	}

}

