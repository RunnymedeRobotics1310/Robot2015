package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.Command;

public class DriveToteElevatorCommand extends Command {
	
	ToteElevatorLevel level;
	
	public DriveToteElevatorCommand(ToteElevatorLevel level) {
		requires(Robot.toteElevatorSubsystem);
		this.level = level;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.actuateEyebrows(false);
		Robot.toteElevatorSubsystem.driveToLevel(level);
	}

	@Override
	protected boolean isFinished() {
		return Robot.toteElevatorSubsystem.onTarget() || level == Robot.toteElevatorSubsystem.getPrevLevel();
	}

	@Override
	protected void end() {
		Robot.toteElevatorSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
	}

}
