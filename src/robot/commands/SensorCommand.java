package robot.commands;

import robot.Robot;
import robot.Toggle;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class SensorCommand extends Command {

	Toggle autoPickupToggle = new Toggle(true);
	
	public SensorCommand() {
		requires(Robot.sensorSubsystem);
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		//autoPickupToggle.update();
		// Execute a pickup if the eyebrows are down.
		// Otherwise the operator is attempting to unload the totes.
		if (Robot.toteIntakeSubsystem.isDeployed()) {
			if (   Robot.sensorSubsystem.getToteSensor()
				&& Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.ONE) {
				Scheduler.getInstance().add(new ToteElevatorWithoutIntakeCommandGroup());
			}
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
