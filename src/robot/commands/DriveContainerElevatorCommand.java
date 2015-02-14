package robot.commands;

import robot.Robot;
import robot.subsystems.ContainerElevatorSubsystem.ContainerElevatorLevel;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.Command;

public class DriveContainerElevatorCommand extends Command {
	
	ContainerElevatorLevel level;
	
	public DriveContainerElevatorCommand(ContainerElevatorLevel level) {
		requires(Robot.containerElevatorSubsystem);
		this.level = level;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.containerElevatorSubsystem.driveToLevel(level);
	}

	@Override
	protected boolean isFinished() {
		return Robot.containerElevatorSubsystem.onTarget();
	}

	@Override
	protected void end() {
		Robot.containerElevatorSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
	}

}
