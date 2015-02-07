package robot.commands.autonomous;

import robot.PolarCoordinate;
import robot.Robot;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDelayCommand extends Command {

	long delay;
	long initTime;

	public AutonomousDelayCommand(long delay) {
		requires(Robot.chassisSubsystem);
		this.delay = delay;
	}

	@Override
	protected void initialize() {
		initTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.DISABLED, PIDEnable.DISABLED);
	}

	@Override
	protected boolean isFinished() {
		return (System.currentTimeMillis() - initTime > delay);
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
