package robot.commands;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DriveElevatorCommand extends Command {
	
	double setpoint;
	
	public DriveElevatorCommand(double setpoint) {
		requires(Robot.elevatorSubsystem);
		this.setpoint = setpoint;
	}
	
	@Override
	protected void initialize() {
		Robot.elevatorSubsystem.enableSubsystem();
	}

	@Override
	protected void execute() {
		Robot.elevatorSubsystem.driveToLevel(setpoint);
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
