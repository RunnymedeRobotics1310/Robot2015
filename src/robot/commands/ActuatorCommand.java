package robot.commands;

import robot.Robot;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Command;

/**
 * The Actuator Command is the default command for the actuator subsystem.
 */
public class ActuatorCommand extends Command {

	public ActuatorCommand() {
		requires(Robot.actuatorSubsystem);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		setRelay();
		setServo();

	}

	private void setRelay() {

		// The relay will only be on if the user is holding a button on the user interface.
		if (Robot.oi.getRelayOn()) {
			Robot.actuatorSubsystem.setRelay(Value.kOn);
			return;
		}

		if (Robot.oi.getRelayForward()) {
			Robot.actuatorSubsystem.setRelay(Value.kForward);
			return;
		}

		if (Robot.oi.getRelayReverse()) {
			Robot.actuatorSubsystem.setRelay(Value.kReverse);
			return;
		}

		Robot.actuatorSubsystem.setRelay(Value.kOff);

	}

	private void setServo() {

		// The servo will be set based on the value of the input from the joystick.
		// The input will be a number from -1.0 to 1.0
		double servoSetpoint = Robot.oi.getServoSetpoint();
		
		// A servo setpoint of 0 will be .5, which will be the natural setpoint of the servo.
		// Moving the joystick left or right will cause the servo setpoint to move to 0 ro 1.0.
		Robot.actuatorSubsystem.setServoSetpoint((servoSetpoint + 1.0) / 2.0);

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
