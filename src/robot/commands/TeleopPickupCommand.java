package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.Toggle;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopPickupCommand extends Command {

	private Toggle pickupToggle = new Toggle(true);

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
				Robot.oi.getPickupRollerButton(), Robot.oi.getPickupRollerDirection());
		PolarCoordinate p = Robot.oi.getDriverPolarCoordinate();
		double direction = p.getTheta();
		double r = p.getR();
		if(r > 0.1) {
			if(direction > 135 && direction < 225) {
				Robot.toteIntakeSubsystem.rollInnerWheels(-0.25);
			} else if(direction > 315 || direction < 45) {
				Robot.toteIntakeSubsystem.rollInnerWheels(0.25);
			}
		}
	}

	public void overrideDeploy() {
		pickupToggle.setState(true);
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
