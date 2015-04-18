package robot.commands;

import robot.Robot;
import robot.Toggle;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class DefaultToteElevatorCommand extends Command {

	private Toggle releaseToggle = new Toggle(false);

	public DefaultToteElevatorCommand() {
		requires(Robot.toteElevatorSubsystem);
	}

	@Override
	protected void initialize() {
	}

	private ToteElevatorLevel decrementLevel(ToteElevatorLevel level) {

		if(level == ToteElevatorLevel.FIVE) {
			return ToteElevatorLevel.FOUR;
		} else if(level == ToteElevatorLevel.FOUR) {
			return ToteElevatorLevel.THREE;
		} else if(level == ToteElevatorLevel.THREE) {
			return ToteElevatorLevel.TWO;
		} else if(level == ToteElevatorLevel.TWO) {
			return ToteElevatorLevel.ONE;
		} else if(level == ToteElevatorLevel.ONE) {
			return ToteElevatorLevel.FLOOR;
		}

		return level;
	}

	@Override
	protected void execute() {

		releaseToggle.update(Robot.oi.getElevatorArmReleaseButton());

		if(Robot.oi.getElevatorDecrementButton()) {
			Scheduler.getInstance().add(new DriveToteElevatorCommand(decrementLevel(Robot.toteElevatorSubsystem.getLevel()), false));
		}

		if(Robot.oi.getElevatorArmButton() && Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.ARM_LEVEL) {
			if(Robot.toteIntakeSubsystem.getCurrentCommand() instanceof TeleopPickupCommand) {
				((TeleopPickupCommand)Robot.toteIntakeSubsystem.getCurrentCommand()).overrideDeploy();
			}
			Robot.toteElevatorSubsystem.setArm(true);

		} else if (Robot.oi.getElevatorArmButton() && Robot.toteElevatorSubsystem.getLevel() != ToteElevatorLevel.ARM_LEVEL) {
			if(Robot.toteIntakeSubsystem.getCurrentCommand() instanceof TeleopPickupCommand) {
				((TeleopPickupCommand)Robot.toteIntakeSubsystem.getCurrentCommand()).overrideDeploy();
			}
			Scheduler.getInstance().add(new DriveToteElevatorCommand(ToteElevatorLevel.ARM_LEVEL, false));
		} else if(releaseToggle.getState() && !DriverStation.getInstance().isAutonomous()) { 
			Robot.toteElevatorSubsystem.setArm(true);
		} else if(!DriverStation.getInstance().isAutonomous()) {
			Robot.toteElevatorSubsystem.setArm(false);
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
		releaseToggle.setState(false);
	}

}
