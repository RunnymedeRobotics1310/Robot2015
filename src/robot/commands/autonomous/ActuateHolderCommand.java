package robot.commands.autonomous;

import robot.Robot;
import robot.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class ActuateHolderCommand extends Command {

	boolean state;
	boolean teleop = false;
	Timer timeout = new Timer(0.5);
	
	public ActuateHolderCommand(boolean state) {
		this.state = state;
		if(!DriverStation.getInstance().isAutonomous()){
			teleop = true;
		}
		requires(Robot.passiveContainerArmSubsystem);
	}
	
	
	
	@Override
	protected void initialize() {
		timeout.disable();
		timeout.start();
	}

	@Override
	protected void execute() {
		Robot.passiveContainerArmSubsystem.actuateHolderSolenoid(state);
	}

	@Override
	protected boolean isFinished() {
		if(!teleop) {
			return !DriverStation.getInstance().isAutonomous();
		} else {
			return timeout.isExpired();
		}
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
