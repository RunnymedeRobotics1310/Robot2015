package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class ToteElevatorCommandGroup extends CommandGroup {

	public ToteElevatorCommandGroup() {
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
	}
	
}
