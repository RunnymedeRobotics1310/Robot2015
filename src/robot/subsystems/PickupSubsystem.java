package robot.subsystems;

import robot.RobotMap;
import robot.commands.TeleopPickupCommand;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PickupSubsystem extends Subsystem {

	DoubleSolenoid dropDownSolenoid = new DoubleSolenoid(RobotMap.DROP_DOWN_SOLENOID_ONE, RobotMap.DROP_DOWN_SOLENOID_TWO);
	Solenoid eyebrowSolenoidLeft = new Solenoid(RobotMap.EYEBROW_SOLENOID_ONE);
	Solenoid eyebrowSolenoidRight = new Solenoid(RobotMap.EYEBROW_SOLENOID_TWO);

	Talon leftPickupMotor = new Talon(RobotMap.LEFT_PICKUP_MOTOR_PORT);
	Talon rightPickupMotor = new Talon(RobotMap.RIGHT_PICKUP_MOTOR_PORT);

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopPickupCommand());
	}

	public void update(boolean deploy, long lastDeployTime, boolean leftEyebrowState, boolean rightEyebrowState, boolean rollers) {

		// FIXME: Make these delays into a Command or a CommandGroup
		if((System.currentTimeMillis() - lastDeployTime < RobotMap.EYEBROW_DEPLOY_PULSE_TIME && !deploy) ||
				(System.currentTimeMillis() - lastDeployTime < RobotMap.EYEBROW_RETRACT_PULSE_TIME && deploy)) {
			eyebrowSolenoidLeft.set(true);
			eyebrowSolenoidRight.set(true);
		} else {
			eyebrowSolenoidLeft.set(leftEyebrowState);
			eyebrowSolenoidRight.set(rightEyebrowState);
		}

		if(rollers) {
			// FIXME FLIPPED IN TESTING, REVERSE DIRECTIONS
			leftPickupMotor.set(-0.75);
			rightPickupMotor.set(0.75);
		} else {
			leftPickupMotor.set(0.0);
			rightPickupMotor.set(0.0);
		}

		if(deploy) {
			dropDownSolenoid.set(DoubleSolenoid.Value.kForward);
		} else {
			dropDownSolenoid.set(DoubleSolenoid.Value.kReverse);
		}

	}
}
