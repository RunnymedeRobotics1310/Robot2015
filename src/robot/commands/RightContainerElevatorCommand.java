package robot.commands;

import robot.Robot;
import robot.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class RightContainerElevatorCommand extends Command {
	
	public RightContainerElevatorCommand() {
//		requires(Robot.toteElevatorSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.toteElevatorSubsystem.override(-0.40);
	}

	@Override
	protected boolean isFinished() {
		return Robot.toteElevatorSubsystem.getEncoderDistance() < -1050;
	}

	@Override
	protected void end() {
		Robot.toteElevatorSubsystem.override(0.0);

	}

	@Override
	protected void interrupted() {
		Robot.toteElevatorSubsystem.override(0.0);
	}

}
