package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousHoldCommand extends Command {

	boolean state;
	
	public AutonomousHoldCommand(boolean state) {
		this.state = state;
		requires(Robot.passiveContainerArmSubsystem);
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.passiveContainerArmSubsystem.actuateHolderSolenoid(state);
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
