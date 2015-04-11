package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutoContainerIntakeCommand extends Command {

	boolean state;
	
	public AutoContainerIntakeCommand(boolean state) {
		requires(Robot.passiveContainerArmSubsystem);
		requires(Robot.toteElevatorSubsystem);
		this.state = state;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.toteElevatorSubsystem.setArm(state);
		Robot.passiveContainerArmSubsystem.update(state, false, false);
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
