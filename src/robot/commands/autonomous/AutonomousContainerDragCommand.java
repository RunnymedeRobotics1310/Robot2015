package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousContainerDragCommand extends Command {

	boolean deployed;
	
	public AutonomousContainerDragCommand(boolean deployed) {
		requires(Robot.toteIntakeSubsystem);
		this.deployed = deployed;
	}
	
	@Override
	protected void initialize() {
		Robot.toteIntakeSubsystem.setContainerDragState(deployed);
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.setContainerDragState(deployed);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		Robot.toteIntakeSubsystem.setContainerDragState(deployed);
	}

	@Override
	protected void interrupted() {
		Robot.toteIntakeSubsystem.setContainerDragState(deployed);
	}

}
