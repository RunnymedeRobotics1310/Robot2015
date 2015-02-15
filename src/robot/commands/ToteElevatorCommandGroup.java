package robot.commands;

import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ToteElevatorCommandGroup extends CommandGroup {

	public ToteElevatorCommandGroup() {
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
	}
	
}