package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.Level;
import edu.wpi.first.wpilibj.command.Command;

public class DriveElevatorCommand extends Command {
	
	Level level;
	
	public DriveElevatorCommand(Level level) {
		requires(Robot.elevatorSubsystem);
		this.level = level;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.elevatorSubsystem.driveToLevel(level);
	}

	@Override
	protected boolean isFinished() {
		return Robot.elevatorSubsystem.onTarget();
	}

	@Override
	protected void end() {
		Robot.elevatorSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
		Robot.elevatorSubsystem.disableSubsystem();
	}

}
