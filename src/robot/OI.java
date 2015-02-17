package robot;

import robot.Joystick_Extreme3DPro.Extreme3DProButton;
import robot.Joystick_F310.F310Button;
import robot.Joystick_F310.F310Stick;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToClickCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.ToteElevatorCommandGroup;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import robot.subsystems.ContainerElevatorSubsystem.ContainerElevatorLevel;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
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
	Toggle containerPickupToggle = new Toggle(false);
	Toggle containerDeployToggle = new Toggle(false);
	Toggle accelerationOverride = new Toggle(true);
	
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
		
		TOTE_ELEVATOR_ZERO_BUTTON (F310Button.BACK),
		TOTE_FIX_BUTTON (F310Button.START),
		
		DRIVE_MODE    (F310Button.R_STICK),
		ACCELERATION_OVERRIDE (F310Button.L_STICK);

		F310Button button;
		
		Driver_ButtonMap(F310Button button) {
			this.button = button;
		}
		
		F310Button getButton() { return this.button; }
	}
	
	private enum Operator_ButtonMap {
		
		// Operator Joystick button mapping
		//TOTE_OVERRIDE           (Extreme3DProButton.BUTTON_4),
		CONTAINER_OVERRIDE      (Extreme3DProButton.BUTTON_3),
		
		ELEVATOR_LEVEL_FLOOR    (Extreme3DProButton.TRIGGER, ToteElevatorLevel.FLOOR, ContainerElevatorLevel.FLOOR),
		ELEVATOR_LEVEL_HALF     (Extreme3DProButton.BUTTON_4, ToteElevatorLevel.HALF, ContainerElevatorLevel.FLOOR),
		ELEVATOR_LEVEL_ONE      (Extreme3DProButton.BUTTON_11, ToteElevatorLevel.ONE, ContainerElevatorLevel.ONE),
		ELEVATOR_LEVEL_TWO      (Extreme3DProButton.BUTTON_9, ToteElevatorLevel.TWO, ContainerElevatorLevel.TWO),
		ELEVATOR_LEVEL_THREE    (Extreme3DProButton.BUTTON_7, ToteElevatorLevel.THREE, ContainerElevatorLevel.THREE),
		ELEVATOR_LEVEL_FOUR     (Extreme3DProButton.BUTTON_12, ToteElevatorLevel.FOUR, ContainerElevatorLevel.FOUR),
		ELEVATOR_LEVEL_FIVE     (Extreme3DProButton.BUTTON_10, ToteElevatorLevel.FLOOR, ContainerElevatorLevel.FIVE),
		ELEVATOR_LEVEL_SIX     (Extreme3DProButton.BUTTON_8, ToteElevatorLevel.FLOOR, ContainerElevatorLevel.SIX),
		
		CONTAINER_PICKUP_BUTTON (Extreme3DProButton.THUMB),
		CONTAINER_DEPLOY_BUTTON (Extreme3DProButton.BUTTON_5);
		
		Extreme3DProButton button;
		ToteElevatorLevel toteLevel;
		ContainerElevatorLevel containerLevel;
		
		Operator_ButtonMap(Extreme3DProButton button) {
			this.button = button;
		}
		
		Operator_ButtonMap(Extreme3DProButton button, ToteElevatorLevel toteLevel, ContainerElevatorLevel containerLevel) {
			this.button = button;
			this.toteLevel = toteLevel;
			this.containerLevel = containerLevel;
		}
		
		Extreme3DProButton getButton() { return this.button; }
		
		ToteElevatorLevel getToteLevel() { return this.toteLevel; }
		
		ContainerElevatorLevel getContainerLevel() { return this.containerLevel; }
		
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

	public boolean getAccelerationOverride() { return accelerationOverride.getState(); }

	public boolean isNewMouseEvent() { return networkTableOI.isNewMouseEvent();	}
	
	public boolean getToteFixButton() { return driverJoystick.getButton(Driver_ButtonMap.TOTE_FIX_BUTTON.getButton()); }
	
	public boolean getToteElevatorZeroButton() { return driverJoystick.getButton(Driver_ButtonMap.TOTE_ELEVATOR_ZERO_BUTTON.getButton()); }

	/*
	 * OPERATOR GETTERS
	 */
	
	//private boolean getToteOverrideButton() { return operatorJoystick.getButton(Operator_ButtonMap.TOTE_OVERRIDE.getButton()); }
	private boolean getContainerOverrideButton() { return operatorJoystick.getButton(Operator_ButtonMap.CONTAINER_OVERRIDE.getButton()); }
	
	private boolean getFloorLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_FLOOR.getButton()); }
	private boolean getHalfLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_HALF.getButton()); }
	private boolean getFirstLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_ONE.getButton()); }
	private boolean getSecondLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_TWO.getButton()); }
	private boolean getThirdLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_THREE.getButton()); }
	private boolean getFourthLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_FOUR.getButton()); }
	private boolean getFifthLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_FIVE.getButton()); }
	private boolean getSixthLevelButton() { return operatorJoystick.getButton(Operator_ButtonMap.ELEVATOR_LEVEL_SIX.getButton()); }
	
	public boolean getContainerPickupToggle() { return containerPickupToggle.getState(); }
	public boolean getContainerDeployToggle() { return containerDeployToggle.getState(); }
	
	private ToteElevatorLevel getOperatorOverrideToteLevel() {
		
		if(getFloorLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FLOOR.getToteLevel();
		} else if(getHalfLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_HALF.getToteLevel();
		} else if(getFirstLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_ONE.getToteLevel();
		} else if(getSecondLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_TWO.getToteLevel();
		} else if(getThirdLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_THREE.getToteLevel();
		} else if(getFourthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FOUR.getToteLevel();
		} else if(getFifthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FIVE.getToteLevel();
		} else if(getSixthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_SIX.getToteLevel();
		}
	
		return null;
	}
	
	private ContainerElevatorLevel getOperatorOverrideContainerLevel() {
		
		if(getFloorLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FLOOR.getContainerLevel();
		} else if(getFirstLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_ONE.getContainerLevel();
		} else if(getSecondLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_TWO.getContainerLevel();
		} else if(getThirdLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_THREE.getContainerLevel();
		} else if(getFourthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FOUR.getContainerLevel();
		} else if(getFifthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_FIVE.getContainerLevel();
		} else if(getSixthLevelButton()) {
			return Operator_ButtonMap.ELEVATOR_LEVEL_SIX.getContainerLevel();
		}
	
		return null;
	}
	
 	public void periodic() {
		
 		// Update all toggles
 		robotRelativeToggle.update(driverJoystick.getButton(Driver_ButtonMap.DRIVE_MODE.getButton()));
 		containerPickupToggle.update(operatorJoystick.getButton(Operator_ButtonMap.CONTAINER_PICKUP_BUTTON.getButton()));
 		containerDeployToggle.update(operatorJoystick.getButton(Operator_ButtonMap.CONTAINER_DEPLOY_BUTTON.getButton()));
 		accelerationOverride.update(driverJoystick.getButton(Driver_ButtonMap.ACCELERATION_OVERRIDE.getButton()));
 		
 		if(getToteElevatorZeroButton()) {
 			Robot.toteElevatorSubsystem.reset();
 		}
 		
 		// Rotate to the requested angle.
		if (getDirectionPointer() >= 0) {
			Scheduler.getInstance().add(new DriveToAngleCommand(getDirectionPointer(), DriveMode.FIELD_RELATIVE));
			return;
		}
		
		// Drives to operator selected level if they are overriding
		ToteElevatorLevel toteLevel = getOperatorOverrideToteLevel();
		if(/*getToteOverrideButton() &&*/ toteLevel != null &&
				Robot.toteElevatorSubsystem.getLevel() != toteLevel) {
			Scheduler.getInstance().add(new DriveToteElevatorCommand(toteLevel));
		}
		
		if(getToteFixButton()) {
			Scheduler.getInstance().add(new ToteElevatorCommandGroup());
		}
				
//		ContainerElevatorLevel containerLevel = getOperatorOverrideContainerLevel();
//		if(getContainerOverrideButton() && containerLevel != null) {
//			Scheduler.getInstance().add(new DriveContainerElevatorCommand(containerLevel));
//		}
		
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

