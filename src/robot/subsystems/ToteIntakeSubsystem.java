package robot.subsystems;

import robot.Robot;
import robot.RobotMap;
import robot.commands.TeleopPickupCommand;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class ToteIntakeSubsystem extends RunnymedeSubsystem {

	DoubleSolenoid dropDownSolenoid = new DoubleSolenoid(RobotMap.DROP_DOWN_SOLENOID_ONE, RobotMap.DROP_DOWN_SOLENOID_TWO);
	Solenoid eyebrowSolenoid = new Solenoid(RobotMap.EYEBROW_SOLENOID);

	//DoubleSolenoid containerDrag = new DoubleSolenoid(17, 18);//RobotMap.CONTAINER_DRAG_PORT_ONE, RobotMap.CONTAINER_DRAG_PORT_TWO);

	Talon leftIntakeMotor = new Talon(RobotMap.LEFT_PICKUP_MOTOR_PORT);
	Talon rightIntakeMotor = new Talon(RobotMap.RIGHT_PICKUP_MOTOR_PORT);

	Talon leftSecondaryIntakeMotor = new Talon(RobotMap.LEFT_SECONDARY_PICKUP_MOTOR); 
	Talon rightSecondaryIntakeMotor = new Talon(RobotMap.RIGHT_SECONDARY_PICKUP_MOTOR); 
	
	boolean passiveContainerArmSaftey = false;

	/**
	 * 
	 * @param state False is open, true is closed
	 */
	public void actuateEyebrows(boolean state) {
		eyebrowSolenoid.set(state);
	}

	public void deploy() {
		if(!Robot.toteElevatorSubsystem.armState) {
			dropDownSolenoid.set(DoubleSolenoid.Value.kForward);
		}
	}

	public void setContainerDragState(boolean deployed) {
		if(deployed) {
			//containerDrag.set(DoubleSolenoid.Value.kForward);
		} else {
			//containerDrag.set(DoubleSolenoid.Value.kReverse);
		}
	}

	@Override
	public void disableSubsystem() {
		leftIntakeMotor.set(0.0);
		rightIntakeMotor.set(0.0);

		leftSecondaryIntakeMotor.set(0.0);
		rightSecondaryIntakeMotor.set(0.0);

		eyebrowSolenoid.set(false);
	}

	public void driveIntakeMotors(double speed, boolean eyebrows) {
		leftIntakeMotor.set(-speed);
		rightIntakeMotor.set(-speed);

		leftSecondaryIntakeMotor.set(speed);
		rightSecondaryIntakeMotor.set(speed);

		eyebrowSolenoid.set(eyebrows);
	}
	
	public void rollOuterWheels(double speed) {
		leftIntakeMotor.set(-speed);
		rightIntakeMotor.set(speed);
	}
	
	public void rollInnerWheels(double speed) {
		
		leftSecondaryIntakeMotor.set(speed);
		rightSecondaryIntakeMotor.set(-speed);
		
	}
	
	@Override
	public void enableSubsystem() {
	}

	@Override
	public void initSubsystem() {
	}

	public void intake() {
		dropDownSolenoid.set(DoubleSolenoid.Value.kForward);

		leftIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
		rightIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);

		leftSecondaryIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
		rightSecondaryIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);

		eyebrowSolenoid.set(true);
	}

	public boolean isDeployed() {
		return (dropDownSolenoid.get() == DoubleSolenoid.Value.kForward);
	}

	public void update(boolean deploy, long lastDeployTime, boolean rollers, boolean rollerDirection) {

		if(!Robot.toteElevatorSubsystem.isEnabled() && Robot.toteElevatorSubsystem.getEncoderDistance() < RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				- RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL/2) {
			// FIXME: Make these delays into a Command or a CommandGroup
			if((System.currentTimeMillis() - lastDeployTime < RobotMap.EYEBROW_RETRACT_PULSE_TIME && !deploy) ||
					(System.currentTimeMillis() - lastDeployTime < RobotMap.EYEBROW_DEPLOY_PULSE_TIME && deploy)) {
				eyebrowSolenoid.set(false);
				passiveContainerArmSaftey = true;
			} else  {
				passiveContainerArmSaftey = false;
			}
			if(rollers && deploy) {
				if (rollerDirection) {
					// Intake
					if(Robot.toteElevatorSubsystem.getLevel() != ToteElevatorLevel.FLOOR) {
						leftIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
						rightIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
					}

					leftSecondaryIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
					rightSecondaryIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
				} else {
					// Eject
					leftIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
					rightIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);

					leftSecondaryIntakeMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
					rightSecondaryIntakeMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
				}
				if(Robot.toteElevatorSubsystem.getLevel() != ToteElevatorLevel.FLOOR) {
					eyebrowSolenoid.set(true);
				}
			} else {
				leftIntakeMotor.set(0.0);
				rightIntakeMotor.set(0.0);

				leftSecondaryIntakeMotor.set(0.0);
				rightSecondaryIntakeMotor.set(0.0);

				eyebrowSolenoid.set(false);
			}
		}
		if(!Robot.toteElevatorSubsystem.armState) {
			if(deploy) {
				dropDownSolenoid.set(DoubleSolenoid.Value.kForward);
			} else {
				dropDownSolenoid.set(DoubleSolenoid.Value.kReverse);
			}
		}

	}

	@Override
	public void updateDashboard() {
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopPickupCommand());
	}

	public boolean getPassiveContainerSaftey() {
		return passiveContainerArmSaftey;
	}

}
