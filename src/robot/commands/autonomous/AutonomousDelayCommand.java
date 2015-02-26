package robot.commands.autonomous;

import robot.PolarCoordinate;
import robot.Robot;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDelayCommand extends Command {

	long delay;
	long initTime;

	public AutonomousDelayCommand(long delay) {
		this.delay = delay;
	}

	@Override
	protected void initialize() {
		initTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return (System.currentTimeMillis() - initTime > delay) || !DriverStation.getInstance().isAutonomous();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
