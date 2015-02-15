package robot.commands;

import robot.Robot;
import robot.Toggle;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopContainerElevatorCommand extends Command {
	
	public TeleopContainerElevatorCommand() {
		requires(Robot.containerElevatorSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.containerElevatorSubsystem.updatePickup(Robot.oi.getContainerPickupToggle(), Robot.oi.getContainerDeployToggle());
		Robot.containerElevatorSubsystem.holdLevel();
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
