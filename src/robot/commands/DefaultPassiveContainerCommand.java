package robot.commands;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class DefaultPassiveContainerCommand extends Command {

	public DefaultPassiveContainerCommand() {
		requires(Robot.passiveContainerArmSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if(!DriverStation.getInstance().isAutonomous()) {
			Robot.passiveContainerArmSubsystem.update(Robot.oi.getContainerArmToggle(),
					Robot.oi.getElevatorArmButton(),
						Robot.oi.getContainerHolderDeployRequest());
		}
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