package robot.subsystems;

import robot.Robot;
import robot.RobotMap;
import robot.commands.TeleopPickupCommand;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class ToteIntakeSubsystem extends RunnymedeSubsystem {

	DoubleSolenoid dropDownSolenoid = new DoubleSolenoid(RobotMap.DROP_DOWN_SOLENOID_ONE, RobotMap.DROP_DOWN_SOLENOID_TWO);
	Solenoid eyebrowSolenoidLeft = new Solenoid(RobotMap.EYEBROW_SOLENOID_LEFT);
	Solenoid eyebrowSolenoidRight = new Solenoid(RobotMap.EYEBROW_SOLENOID_RIGHT);

	Talon leftPickupMotor = new Talon(RobotMap.LEFT_PICKUP_MOTOR_PORT);
	Talon rightPickupMotor = new Talon(RobotMap.RIGHT_PICKUP_MOTOR_PORT);

	/**
	 * 
	 * @param state False is open, true is closed
	 */
	public void actuateEyebrows(boolean state) {
		eyebrowSolenoidLeft.set(state);
		eyebrowSolenoidRight.set(state);
	}

	public void deploy() {
		dropDownSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	@Override
	public void disableSubsystem() {
		leftPickupMotor.set(0.0);
		rightPickupMotor.set(0.0);
	}

	public void driveIntakeMotors(boolean direction) {
		if(direction) {
			leftPickupMotor.set(0.75);
			rightPickupMotor.set(0.75);
		} else {
			leftPickupMotor.set(-0.75);
			rightPickupMotor.set(-0.75);
		}

		eyebrowSolenoidLeft.set(true);
		eyebrowSolenoidRight.set(true);
	}

	@Override
	public void enableSubsystem() {
	}

	@Override
	public void initSubsystem() {
	}

	public void intake() {
		dropDownSolenoid.set(DoubleSolenoid.Value.kForward);

		leftPickupMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
		rightPickupMotor.set(RobotMap.PICKUP_ROLLER_SPEED);

		eyebrowSolenoidLeft.set(true);
		eyebrowSolenoidRight.set(true);
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
				eyebrowSolenoidLeft.set(false);
				eyebrowSolenoidRight.set(false);
			}
			if(rollers) {
				if (rollerDirection) {
					// Intake
					leftPickupMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
					rightPickupMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
				} else {
					// Eject
					leftPickupMotor.set(RobotMap.PICKUP_ROLLER_SPEED);
					rightPickupMotor.set(-RobotMap.PICKUP_ROLLER_SPEED);
				}
				eyebrowSolenoidLeft.set(true);
				eyebrowSolenoidRight.set(true);
			} else {
				leftPickupMotor.set(0.0);
				rightPickupMotor.set(0.0);
			}
		}

		if(deploy) {
			dropDownSolenoid.set(DoubleSolenoid.Value.kForward);
		} else {
			dropDownSolenoid.set(DoubleSolenoid.Value.kReverse);
		}

	}

	@Override
	public void updateDashboard() {
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopPickupCommand());
	}

}
