package robot.commands;

import robot.Robot;
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
		if(Robot.oi.getStartButton()) {//Robot.sensorSubsystem.getToteSensor()) {
			Scheduler.getInstance().add(new ToteElevatorCommandGroup());
		}
		if(Robot.oi.getBackButton()) {
			Scheduler.getInstance().add(new ContainerElevatorCommandGroup());
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
	}

}
