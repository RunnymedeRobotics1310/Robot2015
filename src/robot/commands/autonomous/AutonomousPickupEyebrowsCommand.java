package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousPickupEyebrowsCommand extends Command {

	boolean state;
	
	public AutonomousPickupEyebrowsCommand(boolean state) {
		requires(Robot.toteIntakeSubsystem);
		this.state  = state;
	}
	
	@Override
	protected void initialize() {
		Robot.toteIntakeSubsystem.actuateEyebrows(state);
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
