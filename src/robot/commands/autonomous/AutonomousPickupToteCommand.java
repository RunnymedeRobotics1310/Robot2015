package robot.commands.autonomous;

import robot.Robot;
import robot.commands.ToteElevatorCommandGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousPickupToteCommand extends Command {
	
	public AutonomousPickupToteCommand() {
		requires(Robot.toteIntakeSubsystem);
		requires(Robot.sensorSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if(!Robot.sensorSubsystem.getToteSensor()) {
			Robot.toteIntakeSubsystem.intake();
		}
	}

	@Override
	protected boolean isFinished() {
		return Robot.sensorSubsystem.getToteSensor();
	}

	@Override
	protected void end() {
		Robot.toteIntakeSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
		Robot.toteIntakeSubsystem.disableSubsystem();
	}

}
