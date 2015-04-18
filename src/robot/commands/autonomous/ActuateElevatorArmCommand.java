package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class ActuateElevatorArmCommand extends Command {

	boolean state;
	
	public ActuateElevatorArmCommand(boolean state) {
		requires(Robot.toteElevatorSubsystem);
		this.state = !state;
	}
	
	@Override
	protected void initialize() {
		Robot.toteElevatorSubsystem.setArm(state);
	}

	@Override
	protected void execute() {
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
