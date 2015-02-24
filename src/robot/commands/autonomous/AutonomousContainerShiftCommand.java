package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousContainerShiftCommand extends Command {

	public AutonomousContainerShiftCommand() {
		requires(Robot.toteIntakeSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.driveIntakeMotors(true);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
