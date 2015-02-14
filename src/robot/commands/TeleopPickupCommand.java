package robot.commands;

import robot.Robot;
import robot.Toggle;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopPickupCommand extends Command {

	private Toggle pickupToggle = new Toggle(false);
	
	public TeleopPickupCommand() {
		requires(Robot.toteIntakeSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		pickupToggle.update(Robot.oi.getToteIntakeDeployButton());
		Robot.toteIntakeSubsystem.update(pickupToggle.getState(), pickupToggle.lastStateChangeTime(),
				Robot.oi.getLeftEyebrowButton(), Robot.oi.getRightEyebrowButton(), Robot.oi.getPickupRollerButton());
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {		
	}

}
