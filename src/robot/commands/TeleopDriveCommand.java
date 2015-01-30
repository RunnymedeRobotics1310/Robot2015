package robot.commands;

import robot.Robot;
import robot.Toggle;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleopDriveCommand extends Command {

	Toggle togglePID = new Toggle(true);

	public TeleopDriveCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.chassisSubsystem);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		Robot.chassisSubsystem.driveJoystick(Robot.oi.getDriverPolarCoordinate(),
				Robot.oi.getDriverRotation(), DriveMode.FIELD_RELATIVE, PIDEnable.ENABLED, PIDEnable.ENABLED);

		int directionPointer = Robot.oi.getDriverPov();
		if (directionPointer != -1) {
			Robot.chassisSubsystem.resetGyro(directionPointer);
		}
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
