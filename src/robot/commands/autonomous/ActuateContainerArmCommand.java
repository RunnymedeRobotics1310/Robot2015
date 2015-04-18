package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class ActuateContainerArmCommand extends Command {

	boolean state;
	
	public ActuateContainerArmCommand(boolean state) {
		requires(Robot.passiveContainerArmSubsystem);
		this.state = state;
	}
	
	@Override
	protected void initialize() {
		Robot.passiveContainerArmSubsystem.actuateArmSolenoid(state);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
