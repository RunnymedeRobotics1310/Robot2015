package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class SensorCommand extends Command {

	public SensorCommand() {
		requires(Robot.sensorSubsystem);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
//		if (Robot.sensorSubsystem.getToteSensor()
//				&& Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.ONE
//				&& Robot.toteElevatorSubsystem.getPrevLevel() != ToteElevatorLevel.FLOOR) {
//			Scheduler.getInstance().add(new ToteElevatorCommandGroup());
//		}
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
