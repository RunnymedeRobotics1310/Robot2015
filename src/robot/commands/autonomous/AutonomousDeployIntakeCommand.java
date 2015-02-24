package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDeployIntakeCommand extends Command{

	public AutonomousDeployIntakeCommand() {
		requires(Robot.toteIntakeSubsystem);
	}
	
	@Override
	protected void initialize() {
		Robot.toteIntakeSubsystem.deploy();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {		
	}

	@Override
	protected void interrupted() {
	}

}
