package robot.commands;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ResetGyroCommand extends Command {

	private boolean gyroReset;
	private int fieldAngle;
	
	public ResetGyroCommand(int fieldAngle) {
		requires(Robot.chassisSubsystem);
		this.fieldAngle = fieldAngle;
		gyroReset = false;
		this.setInterruptible(false);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.chassisSubsystem.resetGyro(fieldAngle);
		gyroReset = true;
	}

	@Override
	protected boolean isFinished() {
		return gyroReset;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {		
	}

}
