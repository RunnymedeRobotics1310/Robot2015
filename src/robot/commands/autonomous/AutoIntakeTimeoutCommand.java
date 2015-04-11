package robot.commands.autonomous;

import robot.Robot;
import robot.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutoIntakeTimeoutCommand extends Command {

	Timer timeoutTimer = new Timer(1.0);
	double timeout;
	
	public AutoIntakeTimeoutCommand(double timeout) {
		requires(Robot.toteIntakeSubsystem);
		this.timeout = timeout;
	}
	
	@Override
	protected void initialize() {
		timeoutTimer.disable();
		timeoutTimer.start(timeout);
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.intake();
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous() || timeoutTimer.isExpired();
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
