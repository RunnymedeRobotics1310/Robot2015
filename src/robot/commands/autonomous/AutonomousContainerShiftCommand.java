package robot.commands.autonomous;

import robot.Robot;
import robot.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousContainerShiftCommand extends Command {

	Timer timeout = new Timer(0.5);
	double time;
	
	public AutonomousContainerShiftCommand(double time) {
		requires(Robot.toteIntakeSubsystem);
		this.time = time;
	}
	
	@Override
	protected void initialize() {
		timeout.start(time);
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.driveIntakeMotors(0.75);
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous() || timeout.isExpired();
	}

	@Override
	protected void end() {
		Robot.toteIntakeSubsystem.driveIntakeMotors(0.0);
		Robot.toteIntakeSubsystem.actuateEyebrows(false);
		timeout.disable();
	}

	@Override
	protected void interrupted() {
		Robot.toteIntakeSubsystem.driveIntakeMotors(0.0);
		Robot.toteIntakeSubsystem.actuateEyebrows(false);
		timeout.disable();
	}

}
